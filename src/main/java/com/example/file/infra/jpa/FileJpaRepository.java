package com.example.file.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FileJpaRepository extends JpaRepository<FileJpaEntity, UUID> {
    
}
