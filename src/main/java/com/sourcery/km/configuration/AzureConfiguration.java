package com.sourcery.km.configuration;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AzureConfigurationProperties.class)
public class AzureConfiguration {
    private final AzureConfigurationProperties azureConfigurationProperties;

    @Bean
    public BlobContainerClient blobContainerClient() {
        var connectionString = azureConfigurationProperties.getConnectionString();
        var containerName = azureConfigurationProperties.getContainerName();

        var blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        containerClient.createIfNotExists();
        return containerClient;
    }
}
