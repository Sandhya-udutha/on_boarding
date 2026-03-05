package ug.daes.onboarding.service.impl;

import io.minio.*;
import io.minio.http.Method;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.FileUploadDTO;
import ug.daes.onboarding.dto.Selfie;
import ug.daes.onboarding.exceptions.BucketInitializationException;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.OnboardingLiveliness;
import ug.daes.onboarding.repository.OnboardingLivelinessRepository;
import ug.daes.onboarding.util.AppUtil;
import ug.daes.onboarding.util.Utility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
public class MinioStorageServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(MinioStorageServiceImpl.class);
    private static final String CLASS = "MinioStorageServiceImpl";
    private static final String PATH_SEPARATOR = "/";
    private static final String EXCEPTION = "Unexpected exception";
    private static final String VIDEO = "video";
    private final MinioClient minioClient;
    private final MessageSource messageSource;
    private final ExceptionHandlerUtil exceptionHandlerUtil;
    private final OnboardingLivelinessRepository onboardingLivelinessRepository;

    private final String bucketName;
    private final int expiryDays;
    private final String baseUrl;

    public MinioStorageServiceImpl(
            MinioClient minioClient,
            MessageSource messageSource,
            ExceptionHandlerUtil exceptionHandlerUtil,
            OnboardingLivelinessRepository onboardingLivelinessRepository,
            @Value("${minio.bucket.name}") String bucketName,
            @Value("${minio.expiry.days}") int expiryDays,
            @Value("${app.base.url}") String baseUrl) {

        this.minioClient = minioClient;
        this.messageSource = messageSource;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
        this.onboardingLivelinessRepository = onboardingLivelinessRepository;
        this.bucketName = bucketName;
        this.expiryDays = expiryDays;
        this.baseUrl = baseUrl;
    }


    private String generateFileName(String prefix, String extension) {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS")
                .format(new java.util.Date());
        String randomId = java.util.UUID.randomUUID().toString().substring(0, 4); // short unique ID
        return prefix + "_" + timestamp + "_" + randomId + extension;
    }


    private void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build());

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build());

                logger.info("{} Bucket created successfully: {}", CLASS, bucketName);
            }

        } catch (Exception e) {
            throw new BucketInitializationException(
                    "Failed to initialize MinIO bucket: " + bucketName, e);

        }
    }

    @Async
    public CompletableFuture<ApiResponse> saveFileToMinio(Object fileContent, String fileType, FileUploadDTO fileupload) {
        try {
            logger.info("{}{} - Request to save file to MinIO with fileType: {} and fileUpload: {}",
                    CLASS, Utility.getMethodName(), fileType, fileupload);


            if (fileContent == null
                    || (fileContent instanceof MultipartFile multipartFile && multipartFile.isEmpty())) {

                return CompletableFuture.completedFuture(
                        exceptionHandlerUtil.createErrorResponse("api.error.file.cant.be.null.or.empty"));
            }


            if (VIDEO.equalsIgnoreCase(fileType)
                    && fileContent instanceof MultipartFile multipartFile) {
                return uploadVideo(multipartFile, fileupload);
            } else if ("selfie".equalsIgnoreCase(fileType) && fileContent instanceof Selfie) {
                return uploadSelfie((Selfie) fileContent);
            } else {
                return CompletableFuture.completedFuture(
                        exceptionHandlerUtil.createErrorResponse("api.error.invalid.file.type"));
            }

        } catch (Exception e) {
            logger.error("{} saveFileToMinio Exception: {}", CLASS, e.getMessage(), e);
            return CompletableFuture.completedFuture(exceptionHandlerUtil.handleException(e));
        }
    }

    private ApiResponse uploadFile(InputStream inputStream, long size, String path, String contentType) {
        try {
            ensureBucketExists();


            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );


            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(path)
                            .expiry(expiryDays * 24 * 3600)
                            .build()
            );


            return exceptionHandlerUtil.createSuccessResponse("api.response.file.upload", presignedUrl);

        } catch (Exception e) {

            return exceptionHandlerUtil.createErrorResponse("api.error.something.went.wrong.please.try.after.sometime");

        }
    }


    @Async
    public CompletableFuture<ApiResponse> uploadSelfie(Selfie image) {
        try {
            byte[] img = Base64.getDecoder().decode(image.getSubscriberSelfie());
            if (img.length == 0) {
                return CompletableFuture.completedFuture(exceptionHandlerUtil.createErrorResponse("api.error.file.cant.be.null.or.empty"));
            }

            String fileName = generateFileName("selfie", ".jpeg");
            String path = image.getSubscriberUniqueId() + "/selfie/" + fileName;

            ApiResponse res = uploadFile(new ByteArrayInputStream(img), img.length, path, "image/jpeg");
            if (!res.isSuccess()) {
                return CompletableFuture.completedFuture(exceptionHandlerUtil.createErrorResponse("api.error.selfie.upload.failed"));

            }

            CompletableFuture<ApiResponse> selfieURI = generateSelfieURI(image.getSubscriberUniqueId(), fileName);
            return CompletableFuture.completedFuture(exceptionHandlerUtil.createSuccessResponse("api.response.selfie.uploaded.successfully", selfieURI.get().getResult()));
        } catch (Exception e) {
            logger.error(CLASS + " uploadSelfie Exception {}", e.getMessage());
            return CompletableFuture.completedFuture(exceptionHandlerUtil.handleException(e));
        }
    }


    @Async
    public CompletableFuture<ApiResponse> uploadVideo(
            MultipartFile file,
            FileUploadDTO fileupload) {

        try {

            if (file.isEmpty() || fileupload.getSubscriberUid() == null) {
                return CompletableFuture.completedFuture(
                        exceptionHandlerUtil.createErrorResponse(
                                "api.error.video.cant.be.null.or.empty"));
            }

            if (file.getContentType() == null ||
                    !file.getContentType().startsWith("video/")) {

                return CompletableFuture.completedFuture(
                        exceptionHandlerUtil.createErrorResponse(
                                "api.error.video.content.type.is.not.mp4"));
            }

            File tempFile = convertTempFile(file);
            String fileName = generateFileName(VIDEO, ".mp4");

            String path = Paths.get(
                    fileupload.getSubscriberUid(),
                    VIDEO,
                    fileName).toString();

            ApiResponse res;

            try (FileInputStream inputStream = new FileInputStream(tempFile)) {
                res = uploadFile(
                        inputStream,
                        tempFile.length(),
                        path,
                        "video/mp4");
            }

            Files.deleteIfExists(tempFile.toPath());

            if (!res.isSuccess()) {
                return CompletableFuture.completedFuture(
                        exceptionHandlerUtil.createErrorResponse(
                                "api.error.video.upload.failed"));
            }

            return generateVideoURI(
                    fileupload.getSubscriberUid(),
                    fileName
            ).thenApply(videoResponse -> {

                saveOnboardingLiveliness(
                        fileupload,
                        videoResponse.getResult().toString());

                return exceptionHandlerUtil.successResponse(
                        "api.response.video.uploaded.successfully");
            });

        } catch (Exception e) {
            logger.error("{} uploadVideo exception", CLASS, e);

            return CompletableFuture.completedFuture(
                    exceptionHandlerUtil.handleException(e));
        }

    }


    public ApiResponse deleteFile(String subscriberUid, String folder, String fileName) {
        try {

            String path = subscriberUid + PATH_SEPARATOR
                    + folder + PATH_SEPARATOR
                    + fileName;
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );

            return exceptionHandlerUtil.createSuccessResponse("api.response.file.delete", null);
        } catch (Exception e) {
            logger.error(CLASS + " deleteFile Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }


    public ApiResponse generateDownloadUrl(String subscriberUid, String folder, String fileName) {
        try {
            String path = subscriberUid + PATH_SEPARATOR
                    + folder + PATH_SEPARATOR
                    + fileName;

            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(path)
                            .expiry(expiryDays * 24 * 3600)
                            .build()
            );

            return exceptionHandlerUtil.createSuccessResponse("api.response.file.download.url", url);
        } catch (Exception e) {
            logger.error(CLASS + " generateDownloadUrl Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }


    @Async
    public CompletableFuture<ApiResponse> generateSelfieURI(String subscriberUid, String fileName) {
        try {
            if (subscriberUid == null || fileName == null) {
                return CompletableFuture.completedFuture(
                        exceptionHandlerUtil.createErrorResponse("api.error.invalid.thumbnail.request")
                );
            }


            String downloadUrl = baseUrl + "/api/documents/" + subscriberUid + "/selfie/" + fileName + "/download";


            logger.info("==== Download URL Generated ====");
            logger.info("Selfie URL: {}", downloadUrl);
            logger.info("==========================================");
            // Return the URL (do NOT create presigned MinIO link)
            return CompletableFuture.completedFuture(
                    exceptionHandlerUtil.createSuccessResponse(
                            "api.response.selfie.thumbnail.generated.successfully",
                            downloadUrl
                    )
            );

        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            return CompletableFuture.completedFuture(
                    exceptionHandlerUtil.handleException(e)
            );
        }
    }

    @Async
    public CompletableFuture<ApiResponse> generateVideoURI(String subscriberUid, String fileName) {
        try {
            if (subscriberUid == null || fileName == null) {
                return CompletableFuture.completedFuture(
                        exceptionHandlerUtil.createErrorResponse("api.error.invalid.video.uri.request")
                );
            }


            String downloadUrl = baseUrl + "/api/documents/" + subscriberUid + "/video/" + fileName + "/download";

            logger.info("==== Video Download URL Generated ====");
            logger.info("Video URL: {}", downloadUrl);
            logger.info("======================================");

            return CompletableFuture.completedFuture(
                    exceptionHandlerUtil.createSuccessResponse(
                            "api.response.video.uri.generated.successfully",
                            downloadUrl
                    )
            );

        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            return CompletableFuture.completedFuture(
                    exceptionHandlerUtil.handleException(e)
            );
        }
    }


    private File convertTempFile(MultipartFile file) throws IOException {
        File temp = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(temp)) {
            fos.write(file.getBytes());
        }
        return temp;
    }

    @Async
    public CompletableFuture<ApiResponse> createThumbnailOfSelfie(Selfie image) {
        ApiResponse response;
        try {
            if (image == null) {
                response = AppUtil.createApiResponse(false,
                        messageSource.getMessage("api.error.selfie.cant.be.null.or.empty", null, Locale.ENGLISH),
                        null);
            } else {
                byte[] imgBytes = Base64.getDecoder().decode(image.getSubscriberSelfie());
                InputStream imgInputStream = new ByteArrayInputStream(imgBytes);

                BufferedImage originalImage = ImageIO.read(imgInputStream);
                if (originalImage == null) {
                    response = AppUtil.createApiResponse(false,
                            messageSource.getMessage("api.error.invalid.image.format", null, Locale.ENGLISH),
                            null);
                } else {
                    BufferedImage thumbnail = Scalr.resize(originalImage, Scalr.Method.AUTOMATIC,
                            Scalr.Mode.AUTOMATIC, 100, Scalr.OP_ANTIALIAS);

                    ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
                    ImageIO.write(thumbnail, "jpeg", thumbOutput);

                    byte[] thumbnailBytes = thumbOutput.toByteArray();
                    String base64EncodedThumbnail = Base64.getEncoder().encodeToString(thumbnailBytes);

                    logger.info(CLASS + " createThumbnailOfSelfie: Selfie Thumbnail Generated Successfully");

                    response = AppUtil.createApiResponse(true,
                            messageSource.getMessage("api.response.selfie.thumbnail.generated.successfully", null, Locale.ENGLISH),
                            base64EncodedThumbnail);
                }
            }
        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            logger.error(CLASS + " createThumbnailOfSelfie Exception {}", e.getMessage());
            response = AppUtil.createApiResponse(false,
                    messageSource.getMessage("api.error.something.went.wrong.please.try.after.sometime", null, Locale.ENGLISH),
                    null);
        }

        return CompletableFuture.completedFuture(response);
    }

    private void saveOnboardingLiveliness(FileUploadDTO dto, String url) {
        OnboardingLiveliness entity = new OnboardingLiveliness();
        entity.setSubscriberUid(dto.getSubscriberUid());
        entity.setRecordedTime(dto.getRecordedTime());
        entity.setRecordedGeoLocation(dto.getRecordedGeoLocation());
        entity.setVerificationFirst(dto.getVerificationFirst().name());
        entity.setVerificationSecond(dto.getVerificationSecond().name());
        entity.setVerificationThird(dto.getVerificationThird().name());
        entity.setTypeOfService(dto.getTypeOfService().name());
        entity.setUrl(url);
        onboardingLivelinessRepository.save(entity);
    }
}
