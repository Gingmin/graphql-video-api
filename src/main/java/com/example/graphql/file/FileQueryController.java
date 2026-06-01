package com.example.graphql.file;

import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import com.example.file.application.FileService;
import com.example.graphql.PageInfoGql;

import java.util.UUID;

@Controller
public class FileQueryController {
    private final FileService fileService;

    public FileQueryController(FileService fileService) {
        this.fileService = fileService;
    }

    @QueryMapping
    public FilePageGql files(@Argument("page") Integer page, @Argument("size") Integer size) {
        var result = fileService.files(page, size);

        int totalElements = result.totalElements() > Integer.MAX_VALUE
            ? Integer.MAX_VALUE : (int) result.totalElements();

        return new FilePageGql(
            result.items().stream().map(FileMapper::toGql).toList(),
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
    public FileGql file(@Argument("id") String id) {
        return FileMapper.toGql(fileService.file(UUID.fromString(id)));
    }
}
