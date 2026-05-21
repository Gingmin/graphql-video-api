package com.example.person.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonTranslationJpaRepository extends JpaRepository<PersonTranslationJpaEntity, Long> {

    List<PersonTranslationJpaEntity> findByPersonId(Long personId);
}
