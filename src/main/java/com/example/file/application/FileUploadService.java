package com.example.file.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.file.infra.jpa.FileJpaEntity;
import com.example.file.infra.jpa.FileJpaRepository;

@Service
public class FileUploadService {
    private final String baseDir;
    private final FileJpaRepository fileJpaRepository;

    public FileUploadService(
        @Value("${app.upload.base-dir:uploads}") String baseDir,
        FileJpaRepository fileJpaRepository
    ) {
        this.baseDir = baseDir;
        this.fileJpaRepository = fileJpaRepository;
    }

    @Transactional
    public FileJpaEntity store(MultipartFile file, String type) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            originalName = "unknown";
        }

        String ext = "";
        int dotIdx = originalName.lastIndexOf(".");
        if (dotIdx > 0) {
            ext = originalName.substring(dotIdx);
        }

        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String storedName = UUID.randomUUID() + ext;

        String relativeDirPath = baseDir + "/" + type + "/" + datePath;
        String relativeFilePath = relativeDirPath + "/" + storedName;

        Path absoluteDir = Paths.get(relativeDirPath).toAbsolutePath().normalize();
        Files.createDirectories(absoluteDir);

        String mimeType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        FileJpaEntity entity = new FileJpaEntity(
            storedName,
            originalName,
            relativeFilePath,
            ext.isEmpty() ? "" : ext.substring(1),
            mimeType,
            file.getSize()
        );

        return fileJpaRepository.save(entity);
    }
}
