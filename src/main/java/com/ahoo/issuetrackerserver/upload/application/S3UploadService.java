package com.ahoo.issuetrackerserver.upload.application;

import com.ahoo.issuetrackerserver.common.exception.ApplicationException;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.upload.domain.S3Component;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    public static final String S3_OBJECT_PATH = "issue-tracker/";

    private final AmazonS3 amazonS3;
    private final S3Component component;

    public String upload(MultipartFile file) {
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
            throw new ApplicationException(ErrorType.INVALID_CONTENT_TYPE, new IllegalArgumentException());
        }

        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(
                new PutObjectRequest(component.getBucket(), fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ApplicationException(ErrorType.FILE_CONVERT_FAIL, new IllegalArgumentException());
        }
        return getFileUrl(fileName);
    }

    private String createFileName(String originalFileName) {
        return S3_OBJECT_PATH.concat(UUID.randomUUID().toString()).concat(getExtension(originalFileName));
    }

    private String getExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ApplicationException(ErrorType.INVALID_FILE_NAME, new IllegalArgumentException());
        }
    }

    private String getFileUrl(String fileName) {
        return amazonS3.getUrl(component.getBucket(), fileName).toString();
    }
}
