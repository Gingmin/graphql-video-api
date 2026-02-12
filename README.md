# gingflix

`blog` 스타일(Gradle Kotlin DSL + Spring Boot + GraphQL)로 만든 백엔드에,
같은 레포 안에서 `frontend/`(React/Vite)까지 같이 관리하도록 구성했습니다.

## 요구 사항

- Java 17+
- Node.js 18+ (권장)

## 실행 (개발)

### 1) 백엔드

```bash
.\gradlew.bat bootRun
```

- GraphiQL: `http://localhost:8080/graphiql`
- GraphQL endpoint: `POST http://localhost:8080/graphql`

### 2) 프론트엔드 (Vite dev server)

```bash
.\gradlew.bat frontendDev
```

기본 포트는 `http://localhost:5173` 이고, `/graphql`은 Vite proxy로 백엔드(8080)로 전달됩니다.

## 빌드 (운영)

### 1) 프론트 빌드 + static 복사 + jar 생성

```bash
.\gradlew.bat bootJar
```

프론트 빌드 산출물(`frontend/dist`)은 자동으로 `src/main/resources/static/`에 복사되고,
`build/libs/app.jar`에 포함됩니다.

### 2) (옵션) 프론트 없이 백엔드만 빌드

```bash
.\gradlew.bat bootJar -PskipFrontend
```

