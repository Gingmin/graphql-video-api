package com.example.person.infra.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "person_translation")
public class PersonTranslationJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "person_id", nullable = false)
    private Long personId;

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

    protected PersonTranslationJpaEntity() {}

    public PersonTranslationJpaEntity(Long personId, String language, String name) {
        this.personId = personId;
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

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() { return id; }

    public Long getPersonId() { return personId; }

    public String getLanguage() { return language; }

    public String getName() { return name; }

    public Instant getCreatedAt() { return createdAt; }

    public Instant getModifiedAt() { return modifiedAt; }

    public boolean isDeleted() { return isDeleted; }
}
