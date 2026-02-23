package com.example.user.infra.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Table(name = "users")
public class UserJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}