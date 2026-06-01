package com.example.tag.infra;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;

import com.example.tag.application.TagPage;
import com.example.tag.application.TagRepository;
import com.example.tag.domain.Tag;
import com.example.tag.infra.jpa.TagJpaEntity;
import com.example.tag.infra.jpa.TagJpaRepository;

@Repository
public class TagRepositoryAdapter implements TagRepository {
    private final TagJpaRepository jpaRepository;

    public TagRepositoryAdapter(TagJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private static Tag toDomain(TagJpaEntity e) {
        return new Tag(e.getId(), e.getCode(), e.getCreatedAt(), e.getModifiedAt());
    }

    @Override
    public TagPage findPage(Integer page, Integer size) {
        var pageable = PageRequest.of(page - 1, size);
        var pageResult = jpaRepository.findPage(pageable);
        var tags = pageResult.getContent();

        if (tags.isEmpty()) {
            return new TagPage(
                List.of(),
                pageResult.getNumber() + 1,
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.hasNext(),
                pageResult.hasPrevious()
            );
        }

        var items = tags.stream().map(TagRepositoryAdapter::toDomain).toList();

        return new TagPage(
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
    public Optional<Tag> findById(UUID id) {
        return jpaRepository.findActiveById(id).map(TagRepositoryAdapter::toDomain);
    }

    @Override
    public Tag addTag(String name) {
        var saved = jpaRepository.save(new TagJpaEntity(name));
        return toDomain(saved);
    }

    @Override
    public Tag modifyTag(UUID id, String name) {
        var entity = jpaRepository.findActiveById(id)
            .orElseThrow(() -> new IllegalArgumentException("tag not found"));
        entity.setCode(name);
        var saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean deleteTag(UUID id) {
        var entity = jpaRepository.findActiveById(id)
            .orElseThrow(() -> new IllegalArgumentException("tag not found"));
        entity.setDeleted(true);
        jpaRepository.save(entity);
        return true;
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByCode(name);
    }
}
