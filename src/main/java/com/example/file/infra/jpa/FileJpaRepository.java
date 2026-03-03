package com.example.file.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<FileJpaEntity, Long> {
    
}
