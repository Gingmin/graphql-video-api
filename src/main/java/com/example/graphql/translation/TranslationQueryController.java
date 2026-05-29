package com.example.graphql.translation;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.example.graphql.person.PersonGql;
import com.example.graphql.tag.TagGql;
import com.example.translation.application.TranslationService;

import java.util.List;
import java.util.UUID;

@Controller
public class TranslationQueryController {
    private final TranslationService translationService;

    public TranslationQueryController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @QueryMapping
    public List<TranslationGql> translations(@Argument("targetId") String targetId) {
        return translationService.findByTargetId(UUID.fromString(targetId)).stream()
            .map(TranslationMapper::toGql)
            .toList();
    }

    @QueryMapping
    public TranslationGql translation(@Argument("id") String id) {
        return TranslationMapper.toGql(
            translationService.findById(UUID.fromString(id))
        );
    }

    @SchemaMapping(typeName = "Person", field = "translations")
    public List<TranslationGql> personTranslations(PersonGql person) {
        return translationService.findByTargetId(UUID.fromString(person.id())).stream()
            .map(TranslationMapper::toGql)
            .toList();
    }

    @SchemaMapping(typeName = "Tag", field = "translations")
    public List<TranslationGql> tagTranslations(TagGql tag) {
        return translationService.findByTargetId(UUID.fromString(tag.id())).stream()
            .map(TranslationMapper::toGql)
            .toList();
    }
}
