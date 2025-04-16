package com.sourcery.km.configuration.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "sourcery.jwt")
public class JwtProperties {
    public final String tokenType = "Bearer";

    @NotNull(message = "expires-in-seconds must be configured")
    @Min(value = 1, message = "expires-in-seconds must be at least 1")
    public int expiresInSeconds;

    @NotBlank(message = "JWT secret must be configured")
    private String secret;
}
