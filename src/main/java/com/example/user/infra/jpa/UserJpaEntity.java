package com.example.user.infra.jpa;

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
@Table(name = "users")
public class UserJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "latest_login_ip")
    private String latestLoginIp;

    @Column(name = "last_login_date")
    private Instant lastLoginDate;

    @Column(name = "jti")
    private UUID jti;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    protected UserJpaEntity() {}

    public UserJpaEntity(
      String name
    , String email
    , String password
    , String latestLoginIp
    , Instant lastLoginDate
    , UUID jti
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.latestLoginIp = latestLoginIp;
        this.lastLoginDate = lastLoginDate;
        this.jti = jti;
    }

    public UserJpaEntity(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setLatestLoginIp(String latestLoginIp) {
        this.latestLoginIp = latestLoginIp;
    }

    public String getLatestLoginIp() {
        return latestLoginIp;
    }

    public void setLastLoginDate(Instant lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Instant getLastLoginDate() {
        return lastLoginDate;
    }

    public void setJti(UUID jti) {
        this.jti = jti;
    }

    public UUID getJti() {
        return jti;
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
}