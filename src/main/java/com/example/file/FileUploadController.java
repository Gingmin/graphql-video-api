package com.example.file;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/api/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "image") String type
    ) throws IOException {
        FileJpaEntity saved = fileUploadService.store(file, type);

        return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "originalName", saved.getOriginalName(),
                "storedName", saved.getName(),
                "path", saved.getPath(),
                "size", saved.getFileSize(),
                "contentType", saved.getMimeType()
        ));
    }
}
