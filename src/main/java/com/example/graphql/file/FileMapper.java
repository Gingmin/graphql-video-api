package com.example.graphql.file;

import com.example.file.domain.FileInfo;
import com.example.graphql.GqlDateTimeFormat;

public class FileMapper {

    private FileMapper() {}

    static FileGql toGql(FileInfo file) {
        return new FileGql(
            String.valueOf(file.id()),
            file.name(),
            file.originalName(),
            file.path(),
            file.extension(),
            file.mimeType(),
            String.valueOf(file.fileSize()),
            GqlDateTimeFormat.formatOrNull(file.createdAt()),
            GqlDateTimeFormat.formatOrNull(file.modifiedAt())
        );
    }
}
