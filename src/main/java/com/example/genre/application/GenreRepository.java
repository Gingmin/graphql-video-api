package com.example.genre.application;

import java.util.Optional;
import java.util.UUID;

import com.example.genre.domain.Genre;

public interface GenreRepository {
    GenrePage findPage(Integer page, Integer size);

    Optional<Genre> findById(UUID id);

    Genre addGenre(String code);

    Genre modifyGenre(UUID id, String code);

    boolean deleteGenre(UUID id);

    boolean existsByCode(String code);
}
