package com.example.graphql.genre;

import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import org.springframework.graphql.data.method.annotation.Argument;

import com.example.genre.application.GenreService;

import java.util.UUID;

@Controller
public class GenreMutationController {
    private final GenreService genreService;

    public GenreMutationController(GenreService genreService) {
        this.genreService = genreService;
    }

    @MutationMapping
    public GenreGql addGenre(@Argument("code") String code) {
        var genre = genreService.addGenre(code);
        return GenreMapper.toGql(genre);
    }

    @MutationMapping
    public GenreGql modifyGenre(
        @Argument("id") String id,
        @Argument("code") String code) {
        var genre = genreService.modifyGenre(UUID.fromString(id), code);
        return GenreMapper.toGql(genre);
    }
    
    @MutationMapping
    public boolean deleteGenre(@Argument("id") String id) {
        return genreService.deleteGenre(UUID.fromString(id));
    }
}
