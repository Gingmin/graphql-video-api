package com.example.file.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.file.domain.FileInfo;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional(readOnly = true)
    public FilePage files(Integer page, Integer size) {
        int p = page == null ? 1 : page;
        int s = size == null ? 20 : size;
        if (p < 1) {
            throw new IllegalArgumentException("page must be greater than or equal to 1");
        }
        if (s < 1 || s > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
        return fileRepository.findPage(p, s);
    }

    @Transactional(readOnly = true)
    public FileInfo file(UUID id) {
        return fileRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("file not found"));
    }

    @Transactional
    public boolean deleteFile(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return fileRepository.deleteFile(id);
    }
}
