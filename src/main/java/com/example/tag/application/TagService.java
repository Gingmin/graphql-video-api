package com.example.tag.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import com.example.tag.domain.Tag;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public TagPage tags() {
        return tags(1, 20);
    }

    @Transactional(readOnly = true)
    public TagPage tags(Integer page, Integer size) {
        int p = page == null ? 1 : page;
        int s = size == null ? 20 : size;
        if (p < 1) {
            throw new IllegalArgumentException("page must be greater than or equal to 1");
        }
        if (s < 1 || s > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
        return tagRepository.findPage(p, s);
    }

    @Transactional(readOnly = true)
    public Tag tag(UUID id) {
        return tagRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("tag not found"));
    }

    @Transactional
    public Tag addTag(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        if (tagRepository.existsByName(code)) {
            throw new IllegalArgumentException("code already exists: " + code);
        }
        return tagRepository.addTag(code);
    }

    @Transactional
    public Tag modifyTag(UUID id, String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        return tagRepository.modifyTag(id, code);
    }

    @Transactional
    public boolean deleteTag(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return tagRepository.deleteTag(id);
    }
}
