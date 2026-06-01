package com.example.genre.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import com.example.genre.domain.Genre;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional(readOnly = true)
    public GenrePage genres() {
        return genres(1, 20);
    }

    @Transactional(readOnly = true)
    public GenrePage genres(Integer page, Integer size) {
        int p = page == null ? 1 : page;
        int s = size == null ? 20 : size;
        if (p < 1) {
            throw new IllegalArgumentException("page must be greater than or equal to 1");
        }
        if (s < 1 || s > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
        return genreRepository.findPage(p, s);
    }

    @Transactional(readOnly = true)
    public Genre genre(UUID id) {
        return genreRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("genre not found"));
    }

    @Transactional
    public Genre addGenre(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        if (genreRepository.existsByCode(code)) {
            throw new IllegalArgumentException("code already exists: " + code);
        }
        return genreRepository.addGenre(code);
    }

    @Transactional
    public Genre modifyGenre(UUID id, String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        return genreRepository.modifyGenre(id, code);
    }

    @Transactional
    public boolean deleteGenre(UUID id) {
        return genreRepository.deleteGenre(id);
    }
}
