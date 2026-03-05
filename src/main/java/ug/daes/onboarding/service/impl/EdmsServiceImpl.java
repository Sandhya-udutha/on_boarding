package ug.daes.onboarding.service.impl;

import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import ug.daes.onboarding.config.SentryClientExceptions;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.DocumentResponse;
import ug.daes.onboarding.dto.FileUploadDTO;
import ug.daes.onboarding.dto.Selfie;
import ug.daes.onboarding.exceptions.EdmsUrlValidationException;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.OnboardingLiveliness;
import ug.daes.onboarding.repository.OnboardingLivelinessRepository;

import ug.daes.onboarding.util.AppUtil;


@Service
public class EdmsServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(EdmsServiceImpl.class);


    private static final String CLASS = "EdmsServiceImpl";
    private static final String FILES_DOCUMENTS = "/files/downloads";
    private static final String DOCUMENTS = "/documents";
    private static final String MODEL = "model";
    private static final String FILE_NEW = "file_new";
    private static final String UNEXPECTED_EXCEPTION = "Unexpected exception";
    private static final String FILES = "/files";
    private static final String SOMETHING_WENT_WRONG = "api.error.something.went.wrong.please.try.after.sometime";
    private static final String DOC = "/documents/";

    private final String baselocalUrl;
    private final String edmsDwonlodUrl;
    private final RestTemplate restTemplate;
    private final OnboardingLivelinessRepository onboardingLivelinessRepository;
    private final SentryClientExceptions sentryClientExceptions;
    private final ExceptionHandlerUtil exceptionHandlerUtil;


    public EdmsServiceImpl(
            @Value("${edms.localurl}") String baselocalUrl,
            @Value("${edms.downloadurl}") String edmsDwonlodUrl,
            RestTemplate restTemplate,
            OnboardingLivelinessRepository onboardingLivelinessRepository,
            SentryClientExceptions sentryClientExceptions,
            ExceptionHandlerUtil exceptionHandlerUtil) {
        this.baselocalUrl = baselocalUrl;
        this.edmsDwonlodUrl = edmsDwonlodUrl;
        this.restTemplate = restTemplate;
        this.onboardingLivelinessRepository = onboardingLivelinessRepository;
        this.sentryClientExceptions = sentryClientExceptions;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }


    private void validateUrl(String url) throws EdmsUrlValidationException {
        try {
            if (url == null || url.trim().isEmpty()) {
                throw new EdmsUrlValidationException("URL cannot be null or empty");
            }

            URI uri = new URI(url);


            if (!"https".equalsIgnoreCase(uri.getScheme())) {
                throw new EdmsUrlValidationException("Only HTTPS protocol is allowed");
            }


            String allowedHost = "internal-edms.company.com";
            if (!allowedHost.equalsIgnoreCase(uri.getHost())) {
                throw new EdmsUrlValidationException("Unauthorized host detected: " + uri.getHost());
            }


            InetAddress address = InetAddress.getByName(uri.getHost());
            if (address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isSiteLocalAddress()) {
                throw new EdmsUrlValidationException("Access to internal/private IPs is not allowed");
            }
        } catch (URISyntaxException | UnknownHostException e) {
            throw new EdmsUrlValidationException("Invalid URL or host cannot be resolved: " + url, e);
        }
    }

    public ApiResponse saveSelfieToEdms(Selfie image) {
        try {
            logger.info("{} saveSelfieToEdms req for saveSelfieToEdms {}", CLASS, image.getSubscriberUniqueId());


            byte[] img = Base64.getDecoder().decode(image.getSubscriberSelfie());
            Resource fileRes = getTestFile(img, "selfie", ".jpeg");


            String docIdUrl = baselocalUrl + DOCUMENTS;
            validateUrl(docIdUrl);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            logger.info("{} saveSelfieToEdms req for get DocId docIdUrl {} and requestEntity {} ", CLASS,
                    docIdUrl, requestEntity);
            ResponseEntity<DocumentResponse> documentId = restTemplate.exchange(docIdUrl, HttpMethod.POST,
                    requestEntity, DocumentResponse.class);

            logger.info("{} saveSelfieToEdms res for get DocId {}", CLASS, documentId);


            String docIdAndFileUrl = baselocalUrl + DOC + documentId.getBody().getId() + FILES;
            validateUrl(docIdAndFileUrl);

            MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
            bodyMap.add(FILE_NEW, fileRes);
            bodyMap.add(MODEL, image.getSubscriberUniqueId() + " _Selfie " + AppUtil.getDate());
            bodyMap.add("action", 1);

            HttpHeaders headers4 = new HttpHeaders();
            headers4.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity4 = new HttpEntity<>(bodyMap, headers4);

            logger.info("{} saveSelfieToEdms req for saveFileWithDocId docIdAndFileUrl {} and requestEntity4 {}",
                    CLASS, docIdAndFileUrl, requestEntity4);

            ResponseEntity<ApiResponse> result = restTemplate.exchange(docIdAndFileUrl, HttpMethod.POST, requestEntity4,
                    ApiResponse.class);

            logger.info("{} saveSelfieToEdms res for saveFileWithDocId {}", CLASS, result);

            return handleEdmsResponse(result, documentId, fileRes);

        } catch (Exception e) {
            logger.error("{} saveSelfieToEdms Exception {}", CLASS, e.getMessage());
            return exceptionHandlerUtil.handleHttpException(e);
        }
    }

    private ApiResponse handleEdmsResponse(ResponseEntity<ApiResponse> result,
                                           ResponseEntity<DocumentResponse> documentId,
                                           Resource fileRes) {
        int status = result.getStatusCodeValue();
        return switch (status) {
            case 202 -> deleteTempFileAndReturnSuccess(fileRes, documentId);
            case 500 -> exceptionHandlerUtil.createErrorResponseWithResult("api.error.internal.server.error", status);
            case 400 -> exceptionHandlerUtil.createErrorResponseWithResult("api.error.bad.request", status);
            case 401 -> exceptionHandlerUtil.createErrorResponseWithResult("api.error.unauthorized", status);
            case 403 -> exceptionHandlerUtil.createErrorResponseWithResult("api.error.forbidden", status);
            case 408 -> exceptionHandlerUtil.createErrorResponseWithResult("api.error.request.timeout", status);
            default -> exceptionHandlerUtil.createErrorResponseWithResult(SOMETHING_WENT_WRONG, status);
        };
    }


    private ApiResponse deleteTempFileAndReturnSuccess(Resource fileRes, ResponseEntity<DocumentResponse> documentId) {
        try {
            Path filePath = fileRes.getFile().toPath();
            java.nio.file.Files.deleteIfExists(filePath);
            String downloadUrl = edmsDwonlodUrl + documentId.getBody().getId() + FILES_DOCUMENTS;
            logger.info("{} saveSelfieToEdms downloadurlselfie {}", CLASS, downloadUrl);
            return exceptionHandlerUtil.createSuccessResponse("api.response.selfie.uploaded.successfully", downloadUrl);
        } catch (IOException e) {
            logger.error("{} Failed to delete temporary file", CLASS, e);
            return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);
        }
    }


    public ApiResponse saveVideoToEdms(MultipartFile file, FileUploadDTO fileupload) throws UnknownHostException, EdmsUrlValidationException {
        try {
            logger.info(CLASS + " saveVideoToEdms req fileupload {} and File {} ", fileupload,
                    file.getOriginalFilename());

            if (file.isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.video.cant.be.null.or.empty");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("video/")) {
                return exceptionHandlerUtil.createErrorResponse("api.error.vedio.content.type.isnot.mp4");
            }

            String docIdUrl = baselocalUrl + DOCUMENTS;


            validateEdmsUrl(docIdUrl);


            MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
            logger.info(CLASS + " saveVideoToEdms req for get DocId docIdUrl {} and requestEntity {}", docIdUrl,
                    requestEntity);

            ResponseEntity<DocumentResponse> documentId = restTemplate.exchange(docIdUrl, HttpMethod.POST,
                    requestEntity, DocumentResponse.class);
            logger.info(CLASS + " saveVideoToEdms res for get DocId {}", documentId);
            String docIdAndFileUrl = baselocalUrl + DOC + documentId.getBody().getId() + FILES;
            validateUrl(docIdAndFileUrl);
            MultiValueMap<String, Object> bodyMap1 = new LinkedMultiValueMap<>();
            bodyMap1.add(FILE_NEW, new FileSystemResource(convert(file)));

            bodyMap1.add(MODEL, fileupload.getSubscriberUid() + " _Video " + AppUtil.getDate());
            bodyMap1.add("action", 1);
            HttpHeaders headers1 = new HttpHeaders();
            headers1.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity1 = new HttpEntity<>(bodyMap1, headers1);
            logger.info(CLASS + " saveVideoToEdms req for saveFileWithDocId docIdAndFileUrl {} and requestEntity1 {}",
                    docIdAndFileUrl, requestEntity1);
            ResponseEntity<ApiResponse> result = restTemplate.exchange(docIdAndFileUrl, HttpMethod.POST, requestEntity1,
                    ApiResponse.class);
            logger.info(CLASS + " saveVideoToEdms res for saveFileWithDocId {}", result);
            if (result.getStatusCodeValue() == 202) {
                String download = edmsDwonlodUrl + documentId.getBody().getId() + FILES_DOCUMENTS;
                logger.info(CLASS + " saveVideoToEdms downloadVideoUrl {}", download);
                if (download != null) {
                    OnboardingLiveliness onboardingLiveliness = new OnboardingLiveliness();
                    onboardingLiveliness.setSubscriberUid(fileupload.getSubscriberUid());
                    onboardingLiveliness.setRecordedTime(fileupload.getRecordedTime());
                    onboardingLiveliness.setRecordedGeoLocation(fileupload.getRecordedGeoLocation());
                    onboardingLiveliness.setVerificationFirst(fileupload.getVerificationFirst().name());
                    onboardingLiveliness.setVerificationSecond(fileupload.getVerificationSecond().name());
                    onboardingLiveliness.setVerificationThird(fileupload.getVerificationThird().name());
                    onboardingLiveliness.setTypeOfService(fileupload.getTypeOfService().name());
                    onboardingLiveliness.setUrl(download);
                    onboardingLivelinessRepository.save(onboardingLiveliness);
                    logger.info(CLASS + " saveVideoToEdms true Video uploaded successfully ");
                    return exceptionHandlerUtil.successResponse("api.response.video.uploaded.successfully");
                }
            } else if (result.getStatusCodeValue() == 500) {
                logger.error(CLASS + "saveVideoToEdms false Internal Server Error = 500 ");
                return exceptionHandlerUtil.createErrorResponseWithResult("api.error.internal.server.error",
                        result.getStatusCodeValue());

            } else if (result.getStatusCodeValue() == 400) {
                logger.error(CLASS + " saveVideoToEdms false Bad Request = 400 ");
                return exceptionHandlerUtil.createErrorResponseWithResult("api.error.bad.request",
                        result.getStatusCodeValue());

            } else if (result.getStatusCodeValue() == 401) {
                logger.error(CLASS + " saveVideoToEdms false Unauthorized = 401 ");
                return exceptionHandlerUtil.createErrorResponseWithResult("api.error.unauthorized",
                        result.getStatusCodeValue());

            } else if (result.getStatusCodeValue() == 403) {
                logger.error(CLASS + " saveVideoToEdms false Forbidden = 403");
                return exceptionHandlerUtil.createErrorResponseWithResult("api.error.forbidden",
                        result.getStatusCodeValue());

            } else if (result.getStatusCodeValue() == 408) {
                logger.error(CLASS + " saveVideoToEdms false Request Timeout = 408");
                return exceptionHandlerUtil.createErrorResponseWithResult("api.error.request.timeout",
                        result.getStatusCodeValue());

            } else {
                logger.error(CLASS + "saveVideoToEdms false Something went wrong. Try after sometime 1");
                return exceptionHandlerUtil.createErrorResponseWithResult(
                        SOMETHING_WENT_WRONG, result.getStatusCodeValue());

            }
            logger.error(CLASS + " saveVideoToEdms false Something went wrong. Try after sometime 2");

            return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);

        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            logger.error(CLASS + "saveVideoToEdms Exception {}", e.getMessage());
            sentryClientExceptions.captureTags(fileupload.getSubscriberUid(), null, "saveVideoToEdms",
                    "VideoUploadUrl");
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleHttpException(e);
        }
    }

    private void validateEdmsUrl(String url) throws EdmsUrlValidationException {
        try {
            validateUrl(url);
        } catch (Exception e) {
            throw new EdmsUrlValidationException("Invalid EDMS URL detected", e);
        }
    }

    public static File convert(MultipartFile file) {


        File folder = new File(System.getProperty("catalina.home"), "ObTempFiles");


        File convFile = new File(folder.getAbsolutePath() + File.separator + file.getOriginalFilename());

        if (folder.exists()) {
            logger.error("Folder already exists. PATH :: {}", folder.getAbsolutePath());
            try {
                boolean fileCreated = convFile.createNewFile();
                if (fileCreated) {
                    logger.info("File created successfully at PATH :: {}", convFile.getAbsolutePath());
                } else {
                    logger.warn("File already exists at PATH :: {}", convFile.getAbsolutePath());
                }


                try (FileOutputStream fos = new FileOutputStream(convFile)) {
                    fos.write(file.getBytes());
                }
            } catch (IOException e) {
                logger.error(UNEXPECTED_EXCEPTION, e);
            }
            return convFile;
        } else {

            boolean created = folder.mkdir();
            if (created) {
                logger.info("Folder created successfully. PATH :: {}", folder.getAbsolutePath());
            } else {
                logger.warn("Failed to create the folder or it already exists.");
            }

            try {

                boolean fileCreated = convFile.createNewFile();
                if (fileCreated) {
                    logger.info("File created successfully at PATH :: {}", convFile.getAbsolutePath());
                } else {
                    logger.warn("File already exists at PATH :: {}", convFile.getAbsolutePath());
                }

                try (FileOutputStream fos = new FileOutputStream(convFile)) {
                    fos.write(file.getBytes());
                }
            } catch (IOException e) {
                logger.error(UNEXPECTED_EXCEPTION, e);
            }

            return convFile;
        }
    }

    public static Resource getTestFile(byte[] bytes, String prefix, String suffix) throws IOException {

        Path testFile = Files.createTempFile(prefix, suffix);
        Files.write(testFile, bytes);

        return new FileSystemResource(testFile.toFile());
    }

}