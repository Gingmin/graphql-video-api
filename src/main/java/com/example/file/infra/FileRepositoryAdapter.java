package com.example.file.infra;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;

import com.example.file.application.FilePage;
import com.example.file.application.FileRepository;
import com.example.file.domain.FileInfo;
import com.example.file.infra.jpa.FileJpaEntity;
import com.example.file.infra.jpa.FileJpaRepository;

@Repository
public class FileRepositoryAdapter implements FileRepository {
    private final FileJpaRepository jpaRepository;

    public FileRepositoryAdapter(FileJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private static FileInfo toDomain(FileJpaEntity e) {
        return new FileInfo(
            e.getId(),
            e.getName(),
            e.getOriginalName(),
            e.getPath(),
            e.getExtension(),
            e.getMimeType(),
            e.getFileSize(),
            e.getCreatedAt(),
            e.getModifiedAt()
        );
    }

    @Override
    public FilePage findPage(Integer page, Integer size) {
        var pageable = PageRequest.of(page - 1, size);
        var pageResult = jpaRepository.findPage(pageable);
        var files = pageResult.getContent();

        if (files.isEmpty()) {
            return new FilePage(
                List.of(),
                pageResult.getNumber() + 1,
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.hasNext(),
                pageResult.hasPrevious()
            );
        }

        var items = files.stream().map(FileRepositoryAdapter::toDomain).toList();

        return new FilePage(
            items,
            pageResult.getNumber() + 1,
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.getTotalPages(),
            pageResult.hasNext(),
            pageResult.hasPrevious()
        );
    }

    @Override
    public Optional<FileInfo> findById(UUID id) {
        return jpaRepository.findActiveById(id).map(FileRepositoryAdapter::toDomain);
    }

    @Override
    public boolean deleteFile(UUID id) {
        var entity = jpaRepository.findActiveById(id)
            .orElseThrow(() -> new IllegalArgumentException("file not found"));
        entity.setDeleted(true);
        jpaRepository.save(entity);
        return true;
    }
}
