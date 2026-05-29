package com.example.person.infra.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface PersonJpaRepository extends JpaRepository<PersonJpaEntity, UUID> {

    @Query("SELECT p FROM PersonJpaEntity p ORDER BY p.createdAt DESC")
    Page<PersonJpaEntity> findPage(Pageable pageable);

    Optional<PersonJpaEntity> findById(UUID id);

    boolean existsByCode(String code);
}
