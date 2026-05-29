package com.example.graphql.tag;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.example.tag.domain.Tag;

public class TagMapper {
    
    private TagMapper() {}

    private static final DateTimeFormatter ISO_INSTANT = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

    static TagGql toGql(Tag tag) {
        return new TagGql(
            String.valueOf(tag.id()),
            tag.code(),
            tag.createdAt() == null ? null : ISO_INSTANT.format(tag.createdAt()),
            tag.modifiedAt() == null ? null : ISO_INSTANT.format(tag.modifiedAt())
        );
    }
}
