package com.example.content.infra.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "contents")
public class ContentJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "age_rating", nullable = false)
    private String ageRating;

    @Column(name = "thumbnail_file_id", nullable = false)
    private UUID thumbnailFileId;

    @Column(name = "trailer_file_id", nullable = false)
    private UUID trailerFileId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    protected ContentJpaEntity() {}

    public ContentJpaEntity(String contentType, String title, String description, String ageRating, UUID thumbnailFileId, UUID trailerFileId) {
        this.contentType = contentType;
        this.title = title;
        this.description = description;
        this.ageRating = ageRating;
        this.thumbnailFileId = thumbnailFileId;
        this.trailerFileId = trailerFileId;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (modifiedAt == null) {
            modifiedAt = createdAt;
        }
    }

    @PreUpdate
    void preUpdate() {
        modifiedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getContentType() {
        return contentType;
    }
    
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAgeRating() {
        return ageRating;
    }
    
    public UUID getThumbnailFileId() {
        return thumbnailFileId;
    }

    public UUID getTrailerFileId() {
        return trailerFileId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
}
