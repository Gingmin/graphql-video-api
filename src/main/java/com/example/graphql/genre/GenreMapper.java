package com.example.graphql.genre;

import com.example.graphql.GqlDateTimeFormat;
import com.example.genre.domain.Genre;

public class GenreMapper {
    
    private GenreMapper() {}

    static GenreGql toGql(Genre genre) {
        return new GenreGql(
            String.valueOf(genre.id()),
            genre.code(),
            GqlDateTimeFormat.formatOrNull(genre.createdAt()),
            GqlDateTimeFormat.formatOrNull(genre.modifiedAt())
        );
    }
}
