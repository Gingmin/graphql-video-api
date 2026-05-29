package com.example.tag.infra.jpa;

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
@Table(name = "tags")
public class TagJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    protected TagJpaEntity() {}

    public TagJpaEntity(String code) {
        this.code = code;
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

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }
}
