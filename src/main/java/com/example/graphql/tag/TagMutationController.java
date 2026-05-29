package com.example.graphql.tag;

import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import org.springframework.graphql.data.method.annotation.Argument;

import com.example.tag.application.TagService;

import java.util.UUID;

@Controller
public class TagMutationController {
    private final TagService tagService;

    public TagMutationController(TagService tagService) {
        this.tagService = tagService;
    }

    @MutationMapping
    public TagGql addTag(@Argument("code") String code) {
        var tag = tagService.addTag(code);
        return TagMapper.toGql(tag);
    }

    @MutationMapping
    public TagGql modifyTag(
        @Argument("id") String id,
        @Argument("code") String code) {
        var tag = tagService.modifyTag(UUID.fromString(id), code);
        return TagMapper.toGql(tag);
    }

    @MutationMapping
    public boolean deleteTag(@Argument("id") String id) {
        return tagService.deleteTag(UUID.fromString(id));
    }
}
