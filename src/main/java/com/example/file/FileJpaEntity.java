package com.example.file;

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
@Table(name = "files")
public class FileJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false, length = 10)
    private String extension;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "file_size", nullable = false)
    private long fileSize;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    protected FileJpaEntity() {}

    public FileJpaEntity(String name, String originalName, String path,
                         String extension, String mimeType, long fileSize) {
        this.name = name;
        this.originalName = originalName;
        this.path = path;
        this.extension = extension;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
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

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getOriginalName() { return originalName; }
    public String getPath() { return path; }
    public String getExtension() { return extension; }
    public String getMimeType() { return mimeType; }
    public long getFileSize() { return fileSize; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getModifiedAt() { return modifiedAt; }
    public boolean isDeleted() { return isDeleted; }
}
