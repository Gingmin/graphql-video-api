package com.example.graphql.tag;

import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import com.example.graphql.PageInfoGql;
import com.example.tag.application.TagService;

import java.util.UUID;

@Controller
public class TagQueryController {
    private final TagService tagService;

    public TagQueryController(TagService tagService) {
        this.tagService = tagService;
    }

    @QueryMapping
    public TagPageGql tags(@Argument("page") Integer page, @Argument("size") Integer size) {
        var result = tagService.tags(page, size);

        int totalElements = result.totalElements() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) result.totalElements();

        return new TagPageGql(
            result.items().stream().map(TagMapper::toGql).toList(),
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
    public TagGql tag(@Argument("id") String id) {
        return TagMapper.toGql(tagService.tag(UUID.fromString(id)));
    }
}
