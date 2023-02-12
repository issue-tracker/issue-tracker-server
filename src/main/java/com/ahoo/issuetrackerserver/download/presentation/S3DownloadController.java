package com.ahoo.issuetrackerserver.download.presentation;

import com.ahoo.issuetrackerserver.download.application.S3DownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3DownloadController {

    private final S3DownloadService s3DownloadService;

    @GetMapping("/.well-known/apple-app-site-association")
    public ResponseEntity<byte[]> downloadAASAFile() {
        return s3DownloadService.download("apple-app-site-association.json");
    }
}
