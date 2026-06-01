package com.example.content.application;

import java.util.Optional;
import java.util.UUID;

import com.example.content.domain.Content;

public interface ContentRepository {
    ContentPage findPage(Integer page, Integer size);

    Optional<Content> findById(UUID id);

    Content addContent(String contentType, String title, String description, String ageRating, UUID thumbnailFileId, UUID trailerFileId);

    Content modifyContent(UUID id, String contentType, String title, String description, String ageRating, UUID thumbnailFileId, UUID trailerFileId);

    boolean deleteContent(UUID id);

    boolean existsByTitle(String title);
}
