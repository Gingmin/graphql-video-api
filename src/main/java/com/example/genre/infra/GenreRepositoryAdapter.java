package com.example.genre.infra;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;

import com.example.genre.application.GenrePage;
import com.example.genre.application.GenreRepository;
import com.example.genre.domain.Genre;
import com.example.genre.infra.jpa.GenreJpaEntity;
import com.example.genre.infra.jpa.GenreJpaRepository;

@Repository
public class GenreRepositoryAdapter implements GenreRepository {
    private final GenreJpaRepository jpaRepository;

    public GenreRepositoryAdapter(GenreJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private static Genre toDomain(GenreJpaEntity e) {
        return new Genre(e.getId(), e.getCode(), e.getCreatedAt(), e.getModifiedAt());
    }

    @Override
    public GenrePage findPage(Integer page, Integer size) {
        var pageable = PageRequest.of(page - 1, size);
        var pageResult = jpaRepository.findPage(pageable);
        var genres = pageResult.getContent();
        
        if (genres.isEmpty()) {
            return new GenrePage(
                List.of(),
                pageResult.getNumber() + 1,
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.hasNext(),
                pageResult.hasPrevious()
            );
        }

        var items = genres.stream().map(GenreRepositoryAdapter::toDomain).toList();

        return new GenrePage(
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
    public Optional<Genre> findById(UUID id) {
        return jpaRepository.findActiveById(id).map(GenreRepositoryAdapter::toDomain);
    }

    @Override
    public Genre addGenre(String code) {
        var saved = jpaRepository.save(new GenreJpaEntity(code));
        return toDomain(saved);
    }

    @Override
    public Genre modifyGenre(UUID id, String code) {
        var entity = jpaRepository.findActiveById(id)
            .orElseThrow(() -> new IllegalArgumentException("genre not found"));
        entity.setCode(code);
        var saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean deleteGenre(UUID id) {
        var entity = jpaRepository.findActiveById(id)
            .orElseThrow(() -> new IllegalArgumentException("genre not found"));
        entity.setDeleted(true);
        jpaRepository.save(entity);
        return true;
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}