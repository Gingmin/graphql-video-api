package com.example.graphql.translation;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.example.translation.application.TranslationService;

import java.util.UUID;

@Controller
public class TranslationMutationController {
    private final TranslationService translationService;

    public TranslationMutationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @MutationMapping
    public TranslationGql addTranslation(
        @Argument("targetId") String targetId,
        @Argument("language") String language,
        @Argument("name") String name
    ) {
        var t = translationService.add(UUID.fromString(targetId), language, name);
        return TranslationMapper.toGql(t);
    }

    @MutationMapping
    public TranslationGql modifyTranslation(
        @Argument("id") String id,
        @Argument("language") String language,
        @Argument("name") String name
    ) {
        var t = translationService.modify(UUID.fromString(id), language, name);
        return TranslationMapper.toGql(t);
    }

    @MutationMapping
    public boolean deleteTranslation(@Argument("id") String id) {
        return translationService.delete(UUID.fromString(id));
    }
}
