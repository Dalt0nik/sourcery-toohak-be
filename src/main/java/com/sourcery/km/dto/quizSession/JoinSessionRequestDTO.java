package com.sourcery.km.dto.quizSession;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinSessionRequestDTO {
    @NotBlank(message = "quizSessionId cannot be blank")
    UUID quizSessionId;

    @NotBlank(message = "nickname cannot be blank")
    @Size(max = 20, message = "nickname must not exceed 20 characters")
    String nickname;
}
