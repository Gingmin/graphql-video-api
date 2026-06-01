package com.example.file.application;

import java.util.Optional;
import java.util.UUID;

import com.example.file.domain.FileInfo;

public interface FileRepository {
    FilePage findPage(Integer page, Integer size);

    Optional<FileInfo> findById(UUID id);

    boolean deleteFile(UUID id);
}
