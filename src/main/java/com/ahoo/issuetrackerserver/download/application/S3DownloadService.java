package com.ahoo.issuetrackerserver.download.application;

import com.ahoo.issuetrackerserver.common.exception.ApplicationException;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.upload.domain.S3Component;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3DownloadService {

    private final AmazonS3 amazonS3;
    private final S3Component component;

    public ResponseEntity<byte[]> download(String fileName) {
        try {
            S3Object o = amazonS3.getObject(new GetObjectRequest(component.getBucket(), fileName));
            S3ObjectInputStream objectInputStream = o.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            String downloadFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20")
                .replace(".json", "");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", downloadFileName);

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(ErrorType.FILE_DOWNLOAD_FAIL);
        }
    }
}
