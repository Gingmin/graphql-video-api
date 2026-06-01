package com.example.translation.infra.jpa;

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
@Table(name = "translations")
public class TranslationJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "target_id", nullable = false)
    private UUID targetId;

    @Column(nullable = false, length = 10)
    private String language;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    protected TranslationJpaEntity() {}

    public TranslationJpaEntity(UUID targetId, String language, String name) {
        this.targetId = targetId;
        this.language = language;
        this.name = name;
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

    public void setTargetId(UUID targetId) {
        this.targetId = targetId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() { return id; }

    public UUID getTargetId() { return targetId; }

    public String getLanguage() { return language; }

    public String getName() { return name; }

    public Instant getCreatedAt() { return createdAt; }

    public Instant getModifiedAt() { return modifiedAt; }

    public boolean isDeleted() { return isDeleted; }

    public void setDeleted(boolean deleted) { this.isDeleted = deleted; }
}
