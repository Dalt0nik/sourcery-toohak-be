package com.sourcery.km.dto.file;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class FileDTO {
    UUID imageId;
    String fileType;
}