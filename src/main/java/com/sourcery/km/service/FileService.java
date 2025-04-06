package com.sourcery.km.service;

import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobCopyInfo;
import com.sourcery.km.dto.file.FileDTO;
import com.sourcery.km.exception.BadRequestException;
import com.sourcery.km.exception.ResourceNotFoundException;
import com.sourcery.km.exception.UnsupportedFileTypeException;
import com.sourcery.km.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    private final BlobContainerClient blobContainerClient;

    /**
     * Saves the file by checking mimetype to be jpeg or png, compresses the image and stores in blobStorage
     */
    public FileDTO save(MultipartFile file) throws IOException {
        String id = UUID.randomUUID().toString();

        Tika tika = new Tika();
        InputStream originalInputStream = new BufferedInputStream(file.getInputStream());
        final var BytesToRead = 1024 * 1024; // 1 Mb read to determine mimetype
        originalInputStream.mark(BytesToRead);
        String detectedMimeType = tika.detect(originalInputStream);
        originalInputStream.reset(); // reset the 1Mb read pointer and start from beginning

        if (!("image/jpeg".equalsIgnoreCase(detectedMimeType) || "image/png".equalsIgnoreCase(detectedMimeType))) {
            throw new UnsupportedFileTypeException("Unsupported image type: " + detectedMimeType);
        }

        String extension;
        if ("image/jpeg".equalsIgnoreCase(detectedMimeType)) {
            extension = ".jpg";
        } else {
            extension = ".png";
        }

        String fileName = id + extension;
        String fileUrl = "tmp/" + fileName;

        // Image compression to 80%
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(originalInputStream)
                .scale(1.0)
                .outputQuality(0.8)
                .outputFormat(extension.substring(1))
                .toOutputStream(baos);

        BinaryData data = BinaryData.fromBytes(baos.toByteArray());

        BlobClient blobClient = blobContainerClient.getBlobClient(fileUrl);
        blobClient.upload(data, true);

        return FileDTO.builder()
                .fileName(fileName)
                .fileType(detectedMimeType)
                .build();
    }

    /**
     * Returns the whole image byte stream
     */
    public InputStreamResource retrieve(String fileName) {
        BlobClient blobClient = blobContainerClient.getBlobClient("db/" + fileName);
        if (!blobClient.exists()) {
            blobClient = blobContainerClient.getBlobClient("tmp/" + fileName);
        }
        if (!blobClient.exists()) {
            throw new ResourceNotFoundException("Blob with name '" + fileName + "' not found.");
        }
        var inputStream = blobClient.downloadContent().toStream();
        return new InputStreamResource(inputStream);
    }

    /**
     * Copies the file into a new name which is persistent. From tmp/ to db/
     * It should only be used when storing into database
     */
    public String persist(String fileUrl) {
        String finalFileUrl;
        String tmpPrefix = "tmp/";
        if (fileUrl.startsWith(tmpPrefix)) {
            finalFileUrl = "db/" + fileUrl.substring(tmpPrefix.length());
        } else {
            throw new BadRequestException("File " + fileUrl + " is not temporary");
        }

        var sourceBlob = getBlobClient(fileUrl);

        BlobClient destinationBlob = blobContainerClient.getBlobClient(finalFileUrl);
        SyncPoller<BlobCopyInfo, Void> poller = destinationBlob.beginCopy(sourceBlob.getBlobUrl(), Duration.ofSeconds(1));
        poller.waitForCompletion();

        sourceBlob.delete();

        return finalFileUrl;
    }

    public void delete(String blobName) {
        var blobClient = getBlobClient(blobName);
        blobClient.delete();
    }

    @Profile("dev")
    public List<String> listFiles() {
        List<String> fileNames = new ArrayList<>();
        blobContainerClient.listBlobs()
                .forEach(blobItem -> fileNames.add(blobItem.getName()));
        return fileNames;
    }

    private BlobClient getBlobClient(String blobName) {
        var blobClient = blobContainerClient.getBlobClient(blobName);
        if (Boolean.FALSE.equals(blobClient.exists())) {
            throw new ResourceNotFoundException("Blob with name '" + blobName + "' not found.");
        }
        return blobClient;
    }
}
