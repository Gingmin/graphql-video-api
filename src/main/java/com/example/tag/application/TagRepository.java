package com.example.tag.application;

import java.util.Optional;
import java.util.UUID;

import com.example.tag.domain.Tag;

public interface TagRepository {
    TagPage findPage(Integer page, Integer size);

    Optional<Tag> findById(UUID id);

    Tag addTag(String name);

    Tag modifyTag(UUID id, String name);

    boolean deleteTag(UUID id);

    boolean existsByName(String name);
}
