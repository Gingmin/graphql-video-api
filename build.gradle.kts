import org.gradle.internal.os.OperatingSystem

plugins {
    id("org.springframework.boot") version "3.5.8"
    id("io.spring.dependency-management") version "1.1.7"
    java
    eclipse
}

group = "com.example"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

val skipFrontend = providers.gradleProperty("skipFrontend").isPresent

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// ---- Test split: unit/slice vs integration (JUnit5 @Tag 기반) ----
// - 기본 test: integration 태그 제외
// - integrationTest: integration 태그만 실행

tasks.named<Test>("test") {
    useJUnitPlatform {
        excludeTags("integration")
    }
}

val integrationTest by tasks.registering(Test::class) {
    group = "verification"
    description = "Runs integration tests (tag: integration)."

    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath

    useJUnitPlatform {
        includeTags("integration")
    }

    shouldRunAfter(tasks.named("test"))
}

tasks.named("check") {
    dependsOn(integrationTest)
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("app.jar")
    // 기본은 프론트를 같이 묶어서 jar로 만듭니다.
    // 프론트 없이 백엔드만 빠르게 빌드하려면:
    //   ./gradlew bootJar -PskipFrontend
    if (!skipFrontend) {
        dependsOn("copyFrontendToStatic")
    }
}

// ---------------------------
// Frontend (Vite + React) build integration
// ---------------------------

val frontendDir = layout.projectDirectory.dir("frontend").asFile
val frontendDistDir = layout.projectDirectory.dir("frontend/dist").asFile
val backendStaticDir = layout.projectDirectory.dir("src/main/resources/static").asFile

val npmCmd =
    if (OperatingSystem.current().isWindows) "npm.cmd" else "npm"

val frontendInstall by tasks.registering(Exec::class) {
    group = "frontend"
    description = "Installs frontend dependencies (npm install)."
    workingDir = frontendDir
    commandLine(npmCmd, "install")
    inputs.file(frontendDir.resolve("package.json"))
    outputs.dir(frontendDir.resolve("node_modules"))
}

val frontendBuild by tasks.registering(Exec::class) {
    group = "frontend"
    description = "Builds frontend for production (npm run build)."
    workingDir = frontendDir
    commandLine(npmCmd, "run", "build")
    dependsOn(frontendInstall)
    inputs.dir(frontendDir.resolve("src"))
    inputs.file(frontendDir.resolve("index.html"))
    inputs.file(frontendDir.resolve("vite.config.ts"))
    inputs.file(frontendDir.resolve("tsconfig.json"))
    outputs.dir(frontendDistDir)
}

val copyFrontendToStatic by tasks.registering(Copy::class) {
    group = "frontend"
    description = "Copies frontend build output into backend resources/static."
    dependsOn(frontendBuild)

    from(frontendDistDir)
    into(backendStaticDir)

    doFirst {
        // Clean static dir to avoid stale hashed assets
        if (backendStaticDir.exists()) {
            backendStaticDir.deleteRecursively()
        }
    }
}

tasks.named<ProcessResources>("processResources") {
    // processResources가 읽는 디렉토리(src/main/resources/static)를 copyFrontendToStatic이 생성하므로,
    // Gradle task validation을 위해 명시적으로 의존성을 선언합니다.
    if (!skipFrontend) {
        dependsOn(copyFrontendToStatic)
    }
}

tasks.register<Exec>("frontendDev") {
    group = "frontend"
    description = "Runs Vite dev server (npm run dev)."
    workingDir = frontendDir
    commandLine(npmCmd, "run", "dev")
    dependsOn(frontendInstall)
}

tasks.register("dev") {
    group = "application"
    description = "Convenience task: run backend (bootRun) + frontend (frontendDev) separately."
    // NOTE: Gradle에서 두 프로세스를 동시에 붙잡는 건 환경별로 까다로워서,
    // 이 task는 안내용/그룹핑용으로 두고 실제 실행은 아래처럼 2개 터미널로 권장:
    // 1) ./gradlew bootRun
    // 2) ./gradlew frontendDev
    dependsOn("bootRun")
}

