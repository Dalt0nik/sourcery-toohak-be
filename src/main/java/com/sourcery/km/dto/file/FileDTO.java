package com.sourcery.km.dto.file;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileDTO {
    String fileName;
    String fileType;
}