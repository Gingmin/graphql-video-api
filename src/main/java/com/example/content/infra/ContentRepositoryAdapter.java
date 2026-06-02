package com.example.content.infra;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;

import com.example.content.application.ContentPage;
import com.example.content.application.ContentRepository;
import com.example.content.domain.Content;
import com.example.content.infra.jpa.ContentJpaEntity;
import com.example.content.infra.jpa.ContentJpaRepository;

@Repository
public class ContentRepositoryAdapter implements ContentRepository {
    private final ContentJpaRepository jpaRepository;

    public ContentRepositoryAdapter(ContentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    
}
