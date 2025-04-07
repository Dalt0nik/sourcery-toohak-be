package com.sourcery.km.builder.file;

import com.sourcery.km.dto.file.FileDTO;
import com.sourcery.km.entity.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileBuilder {
    public static FileDTO toFileDTO(File file) {
        return FileDTO.builder()
                .imageId(file.getId())
                .fileType(file.getFileType())
                .build();
    }

    public static File fromFileIdSetTemporary(UUID fileId, boolean isTemporary) {
        return File.builder()
                .id(fileId)
                .isTemporary(isTemporary)
                .build();
    }
}
