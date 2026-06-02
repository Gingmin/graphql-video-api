package com.example.content.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import com.example.content.domain.Content;

@Service
public class ContentService {
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Transactional(readOnly = true)
    public ContentPage contents() {
        return contents(1, 20);
    }

    @Transactional(readOnly = true)
    public ContentPage contents(Integer page, Integer size) {
        int p = page == null ? 1 : page;
        int s = size == null ? 20 : size;
        if (p < 1) {
            throw new IllegalArgumentException("page must be greater than or equal to 1");
        }
        if (s < 1 || s > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
        return contentRepository.findPage(p, s);
    }

    @Transactional(readOnly = true)
    public Content content(UUID id) {
        return contentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("content not found"));
    }

    @Transactional
    public Content addContent(String contentType, String title, String description, String ageRating, UUID thumbnailFileId, UUID trailerFileId) {
        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("contentType must not be blank");
        }
        if (ageRating == null || ageRating.isBlank()) {
            throw new IllegalArgumentException("ageRating must not be blank");
        }
        return contentRepository.addContent(contentType, title, description, ageRating, thumbnailFileId, trailerFileId);
    }

    @Transactional
    public Content modifyContent(UUID id, String contentType, String title, String description, String ageRating, UUID thumbnailFileId, UUID trailerFileId) {
        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("contentType must not be blank");
        }
        if (ageRating == null || ageRating.isBlank()) {
            throw new IllegalArgumentException("ageRating must not be blank");
        }
        return contentRepository.modifyContent(id, contentType, title, description, ageRating, thumbnailFileId, trailerFileId);
    }

    @Transactional
    public boolean deleteContent(UUID id) {
        return contentRepository.deleteContent(id);
    }
}
