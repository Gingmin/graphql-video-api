package com.example.person.infra.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface PersonJpaRepository extends JpaRepository<PersonJpaEntity, Long> {

    @Query("SELECT p FROM PersonJpaEntity p ORDER BY p.createdAt DESC")
    Page<PersonJpaEntity> findPage(Pageable pageable);

    Optional<PersonJpaEntity> findById(Long id);

    boolean existsByCode(String code);
}
