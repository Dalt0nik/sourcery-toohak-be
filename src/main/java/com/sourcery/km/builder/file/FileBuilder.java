package com.sourcery.km.builder.file;

import com.sourcery.km.dto.file.FileDTO;
import com.sourcery.km.entity.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileBuilder {
    public static File toFileEntity(UUID id, String fileName, String fileType) {
        return File.builder()
                .id(id)
                .fileUrl(fileName)
                .fileType(fileType)
                .createdAt(Instant.now())
                .build();
    }

    public static FileDTO toFileDTO(File file) {
        return FileDTO.builder()
                .fileName(file.getFileUrl())
                .fileType(file.getFileType())
                .build();
    }
}
