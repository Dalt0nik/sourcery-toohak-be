package com.sourcery.km.builder.file;

import com.sourcery.km.dto.file.FileDTO;
import com.sourcery.km.entity.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileBuilder {
    public static FileDTO toFileDTO(File file) {
        return FileDTO.builder()
                .imageId(file.getId())
                .fileType(file.getFileType())
                .build();
    }
}
