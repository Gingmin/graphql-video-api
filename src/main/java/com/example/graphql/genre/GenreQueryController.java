package com.example.graphql.genre;

import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import com.example.graphql.PageInfoGql;

import com.example.genre.application.GenreService;

import java.util.UUID;

@Controller
public class GenreQueryController {
    private final GenreService genreService;

    public GenreQueryController(GenreService genreService) {
        this.genreService = genreService;
    }

    @QueryMapping
    public GenrePageGql genres(@Argument("page") Integer page, @Argument("size") Integer size) {
        var result = genreService.genres(page, size);

        int totalElements = result.totalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.totalElements();

        return new GenrePageGql(
            result.items().stream().map(GenreMapper::toGql).toList(),
            new PageInfoGql(
                result.page(),
                result.size(),
                totalElements,
                result.totalPages(),
                result.hasNext(),
                result.hasPrev()
            )
        );
    }

    @QueryMapping
    public GenreGql genre(@Argument("id") String id) {
        return GenreMapper.toGql(genreService.genre(UUID.fromString(id)));
    }
}
