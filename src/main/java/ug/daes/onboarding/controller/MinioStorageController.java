package ug.daes.onboarding.controller;

import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.FileUploadDTO;
import ug.daes.onboarding.dto.Selfie;
import ug.daes.onboarding.service.impl.MinioStorageServiceImpl;
import io.minio.GetObjectArgs;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@RestController
public class MinioStorageController {

    private static final Logger logger = LoggerFactory.getLogger(MinioStorageController.class);


    private final MinioStorageServiceImpl minioStorageService;
    private final MinioClient minioClient;
    private final String bucketName;


    public MinioStorageController(MinioStorageServiceImpl minioStorageService,
                                  MinioClient minioClient,
                                  @Value("${minio.bucket.name}") String bucketName) {
        this.minioStorageService = minioStorageService;
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }


    @PostMapping("/selfie/upload")
    public CompletableFuture<ApiResponse> uploadSelfie(@RequestBody Selfie selfie) {
        return minioStorageService.uploadSelfie(selfie);
    }


    @PostMapping("/video/upload")
    public CompletableFuture<ApiResponse> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute FileUploadDTO fileUploadDTO) {
        return minioStorageService.uploadVideo(file, fileUploadDTO);
    }

    @GetMapping("/{subscriberUid}/selfie/{fileName}/generate-uri")
    public CompletableFuture<ApiResponse> generateSelfieURI(
            @PathVariable String subscriberUid,
            @PathVariable String fileName) {
        return minioStorageService.generateSelfieURI(subscriberUid, fileName);
    }


    @GetMapping("/{subscriberUid}/video/{fileName}/generate-uri")
    public CompletableFuture<ApiResponse> generateVideoURI(
            @PathVariable String subscriberUid,
            @PathVariable String fileName) {
        return minioStorageService.generateVideoURI(subscriberUid, fileName);
    }


    @DeleteMapping("/{subscriberUid}/{folder}/{fileName}")
    public ApiResponse deleteFile(
            @PathVariable String subscriberUid,
            @PathVariable String folder,
            @PathVariable String fileName) {
        return minioStorageService.deleteFile(subscriberUid, folder, fileName);
    }


    @GetMapping("/api/documents/{subscriberUid}/selfie/{fileName}/download")
    public ResponseEntity<Object> downloadSelfie(
            @PathVariable String subscriberUid,
            @PathVariable String fileName) {
        try {
            String objectPath = subscriberUid + "/selfie/" + fileName;
            logger.info("Trying to download from downloadSelfie: {}", objectPath);

            InputStream fileStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectPath)
                            .build()
            );

            byte[] fileBytes = org.apache.commons.io.IOUtils.toByteArray(fileStream);
            fileStream.close();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(fileBytes);

        } catch (Exception e) {
            logger.error("Unexpected exception", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Selfie not found for SUID: " + subscriberUid);
        }
    }

    @GetMapping("/api/documents/{subscriberUid}/video/{fileName}/download")
    public ResponseEntity<Object> downloadVideo(
            @PathVariable String subscriberUid,
            @PathVariable String fileName) {
        try {

            String objectPath = subscriberUid + "/video/" + fileName;
            logger.info("Trying to download from: {}", objectPath);


            InputStream fileStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectPath)
                            .build()
            );


            byte[] fileBytes = org.apache.commons.io.IOUtils.toByteArray(fileStream);
            fileStream.close();


            MediaType contentType = fileName.toLowerCase().endsWith(".mp4")
                    ? MediaType.valueOf("video/mp4")
                    : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(contentType)
                    .body(fileBytes);

        } catch (Exception e) {
            logger.error("Unexpected exception", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Video not found for SUID: " + subscriberUid);
        }
    }
}
