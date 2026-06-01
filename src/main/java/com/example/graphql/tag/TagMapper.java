package com.example.graphql.tag;

import com.example.graphql.GqlDateTimeFormat;
import com.example.tag.domain.Tag;

public class TagMapper {
    
    private TagMapper() {}

    static TagGql toGql(Tag tag) {
        return new TagGql(
            String.valueOf(tag.id()),
            tag.code(),
            GqlDateTimeFormat.formatOrNull(tag.createdAt()),
            GqlDateTimeFormat.formatOrNull(tag.modifiedAt())
        );
    }
}
