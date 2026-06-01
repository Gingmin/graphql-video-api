package com.example.graphql.file;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.example.file.application.FileService;

import java.util.UUID;

@Controller
public class FileMutationController {
    private final FileService fileService;

    public FileMutationController(FileService fileService) {
        this.fileService = fileService;
    }

    @MutationMapping
    public boolean deleteFile(@Argument("id") String id) {
        return fileService.deleteFile(UUID.fromString(id));
    }
}
