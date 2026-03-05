package ug.daes.onboarding.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.FileUploadDTO;
import ug.daes.onboarding.dto.Selfie;
import ug.daes.onboarding.exceptions.EdmsUrlValidationException;

import ug.daes.onboarding.service.impl.EdmsServiceImpl;

import java.net.UnknownHostException;

@RestController
public class VideoUploadUrl {

    private static final Logger logger = LoggerFactory.getLogger(VideoUploadUrl.class);

    private static final String CLASS = "VideoUploadUrl";

    private final EdmsServiceImpl edmsService;


    public VideoUploadUrl(EdmsServiceImpl edmsService) {
        this.edmsService = edmsService;
    }


    @PostMapping("/api/post/upload-file")
    public ApiResponse uploadFileLocal(
            @RequestParam("model") String model,
            @RequestParam("file") MultipartFile file)
            throws JsonProcessingException, UnknownHostException, EdmsUrlValidationException {

        logger.info(CLASS + " uploadFileLocal req model {} and File {}", model, file.getOriginalFilename());

        ObjectMapper mapper = new ObjectMapper();
        FileUploadDTO modelDTO = mapper.readValue(model, FileUploadDTO.class);

        return edmsService.saveVideoToEdms(file, modelDTO);
    }

    @PostMapping("/api/post/selfi-upload")
    public ApiResponse selfiUpload(@RequestBody Selfie selfie) {
        logger.info(CLASS + " SelfiUpload req  {}", selfie);
        return edmsService.saveSelfieToEdms(selfie);

    }

}
