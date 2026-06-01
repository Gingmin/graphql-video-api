package com.example.person.infra.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface PersonJpaRepository extends JpaRepository<PersonJpaEntity, UUID> {

    @Query("SELECT p FROM PersonJpaEntity p WHERE p.isDeleted = false ORDER BY p.createdAt DESC")
    Page<PersonJpaEntity> findPage(Pageable pageable);

    @Query("SELECT p FROM PersonJpaEntity p WHERE p.id = :id AND p.isDeleted = false")
    Optional<PersonJpaEntity> findActiveById(UUID id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PersonJpaEntity p WHERE p.code = :code AND p.isDeleted = false")
    boolean existsByCode(String code);
}
