package ug.daes.onboarding.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Base64;

import javax.imageio.ImageIO;

import jakarta.servlet.http.HttpServletRequest;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.*;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.Subscriber;

import ug.daes.onboarding.model.SubscriberStatusModel;
import ug.daes.onboarding.repository.SubscriberRepoIface;
import ug.daes.onboarding.repository.SubscriberStatusRepoIface;
import ug.daes.onboarding.service.iface.SubscriberServiceIface;
import ug.daes.onboarding.util.Utility;

@RestController
public class SubscriberController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriberController.class);
    private static final String CLASS = "SubscriberController";

    private final SubscriberServiceIface subscriberServiceIface;
    private final SubscriberRepoIface subscriberRepo;
    private final SubscriberStatusRepoIface statusRepo;

    private final ExceptionHandlerUtil exceptionHandlerUtil;

    public SubscriberController(SubscriberServiceIface subscriberServiceIface,
                                SubscriberRepoIface subscriberRepo,
                                SubscriberStatusRepoIface statusRepo,

                                ExceptionHandlerUtil exceptionHandlerUtil) {
        this.subscriberServiceIface = subscriberServiceIface;
        this.subscriberRepo = subscriberRepo;
        this.statusRepo = statusRepo;

        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }


    @PostMapping("/api/post/trusted/user")
    public ApiResponse addTrustedUsers(@RequestBody TrustedUserDto emails) {
        logger.info("{}{} - addTrustedUsers req: {}", CLASS, Utility.getMethodName(), emails);
        return subscriberServiceIface.addTrustedUsers(emails);
    }

    @PostMapping("/api/post/save-subscriber-details")
    public ApiResponse saveSubscriberData(HttpServletRequest request, @RequestBody MobileOTPDto subscriberDTO)
            throws ParseException, UnknownHostException {
        logger.info("{}{} - saveSubscriberData req for {}", CLASS, Utility.getMethodName(), subscriberDTO);
        subscriberDTO.setSubscriberEmail(subscriberDTO.getSubscriberEmail().toLowerCase());
        return subscriberServiceIface.saveSubscribersData(subscriberDTO);
    }


    @PostMapping("/api/post/add/subscriber-ob-data")
    public ApiResponse saveSubscriberObData(HttpServletRequest request,
                                            @RequestBody SubscriberObRequestDTO obRequestDTO) {
        return subscriberServiceIface.addSubscriberObData(obRequestDTO);
    }

    @PostMapping("/api/post/reonboard/subscriber-ob-data")
    public ApiResponse reOnboardSaveSubacriberObData(HttpServletRequest request,
                                                     @RequestBody SubscriberObRequestDTO obRequestDTO) {
        return subscriberServiceIface.reOnboardAddSubscriberObData(obRequestDTO);
    }

    @PostMapping("/api/post/fetch/subscriber-ob-data")
    public ApiResponse getSubscriberObData(HttpServletRequest request,
                                           @RequestBody GetSubscriberObDataDTO subscriberUID) {

        return subscriberServiceIface.getSubscriberObData(request, subscriberUID);
    }

    @GetMapping(value = "/api/get/verification-channel-response/{suid}", produces = "application/json")
    public ApiResponse getVerificationChannelResponse(HttpServletRequest request, @PathVariable String suid) {
        return subscriberServiceIface.getVerificationChannelResponse(request, suid);
    }

    @PostMapping("/api/post/fetch/reset-pin")
    public ApiResponse resetPin(HttpServletRequest request, @RequestBody GetSubscriberObDataDTO obDataDTO) {
        return subscriberServiceIface.resetPin(obDataDTO);
    }

    @GetMapping("/api/get/live/video")
    public ResponseEntity<Object> getVideoLiveStreaming(@RequestParam String subscriberUid) {
        return subscriberServiceIface.getVideoLiveStreaming(subscriberUid);
    }

    @GetMapping("/api/get/live/video/local")
    public ResponseEntity<Object> getVideoLiveStreamingLocalEdms(@RequestParam String subscriberUid) {

        return subscriberServiceIface.getVideoLiveStreamingLocalEdms(subscriberUid);
    }

    @GetMapping("/api/get/subscriber/details/report")
    public ApiResponse getSubscriberDetailsReport(@RequestParam String startDate, @RequestParam String endDate) {
        return subscriberServiceIface.getSubscriberDetailsReports(startDate, endDate);
    }

    @GetMapping("/api/get/devicestatus")
    public ApiResponse getDeviceStatus(HttpServletRequest request) {
        return subscriberServiceIface.getDeviceStatus(request);
    }


    @PostMapping("/api/get/thum")
    public byte[] createThumbnail(@RequestBody Selfie image) throws IOException {
        try {
            byte[] img = Base64.getDecoder().decode(image.getSubscriberSelfie());
            Resource fileRes = getTestFile(img, "selfie", ".jpeg");

            ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
            BufferedImage img2 = ImageIO.read(fileRes.getInputStream());
            BufferedImage thumbImg = Scalr.resize(
                    img2,
                    Scalr.Method.AUTOMATIC,
                    Scalr.Mode.AUTOMATIC,
                    100,
                    Scalr.OP_ANTIALIAS
            );
            ImageIO.write(thumbImg, "jpeg", thumbOutput);

            logger.info("thumbOutput :: {}", thumbOutput);
            return thumbOutput.toByteArray();

        } catch (Exception e) {
            logger.error("Unexpected exception", e);
            return new byte[0];
        }
    }

    @GetMapping("/api/get/subscriber/details")
    public ApiResponse deleteSubscriber(@RequestParam String id) {
        SubscriberDetailsDto details = new SubscriberDetailsDto();
        try {
            if (id == null || id.isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.subscriberuid.cant.be.null");
            }
            Subscriber subscriber = subscriberRepo.findBysubscriberUid(id);
            SubscriberStatusModel status = statusRepo.findBysubscriberUid(id);
            if (subscriber != null) {
                details.setEmail(subscriber.getEmailId());
                details.setPhoneNo(subscriber.getMobileNumber());
                details.setSubscriberStatus(status.getSubscriberStatus());
                details.setFullName(subscriber.getFullName());
                return exceptionHandlerUtil.createSuccessResponse("api.response.subscriber.details", details);
            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.not.found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception", e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    public Resource getTestFile(byte[] bytes, String prefix, String suffix) throws IOException {

        Path testFile = Files.createTempFile(prefix, suffix);
        Files.write(testFile, bytes);
        return new FileSystemResource(testFile.toFile());
    }


    @PostMapping("/api/delete/subscriber-record")
    public ApiResponse deleteSubscriber(@RequestParam String mobileNo, @RequestParam String email) {
        logger.info("response");
        return subscriberServiceIface.deleteRecord(mobileNo, email);
    }

    @GetMapping(value = "/api/get/subscriber/details/{searchType}/{searchValue}", produces = "application/json")
    public ApiResponse getSubscriberDetails(@PathVariable String searchType, @PathVariable String searchValue) {
        return subscriberServiceIface.getSubscriberDetailsBySerachType(searchType, searchValue);
    }


    @PostMapping(value = "/api/update/subscriber/device-status/{subscriberUniqueId}")
    public ApiResponse updateSubscriberDeviceStatus(@PathVariable String subscriberUniqueId) {
        return subscriberServiceIface.updateSusbcriberDeviceStatus(subscriberUniqueId);

    }


    @GetMapping(value = "/api/get/subscriber/list/{searchType}/{searchValue}", produces = "application/json")
    public ApiResponse getSubscriberList(@PathVariable String searchType, @PathVariable String searchValue) {
        return subscriberServiceIface.getSubscriberListBySerachType(searchType, searchValue);
    }

    @PostMapping("/api/post/update/fcmtoken")
    public ApiResponse updateFCMToken(@RequestParam String suid, @RequestParam String fcmToken) {
        logger.info("{}{} - Received request to update FCM token for suid: {} with fcmToken: {}",
                CLASS, Utility.getMethodName(), suid, fcmToken);
        return subscriberServiceIface.updateFcmTokenDetails(suid, fcmToken);
    }

    @GetMapping("/api/get/fcmtoken/{suid}")
    public ApiResponse updateFCMToken(@PathVariable String suid) {

        return subscriberServiceIface.getFCMToken(suid);
    }

    @GetMapping(value = "/api/get/subscriber/profile/{searchType}/{searchValue}", produces = "application/json")
    public ApiResponse getSubscriberDetailsBySerachType(HttpServletRequest request, @PathVariable String searchType,
                                                        @PathVariable String searchValue) {
        return subscriberServiceIface.getSubDetailsBySerachType(request, searchType, searchValue);
    }


    @GetMapping(value = "/api/get/subscriber-device-details/{suid}", produces = "application/json")
    public ApiResponse getSusbcriberDeviceHistory(@PathVariable String suid) {
        return subscriberServiceIface.getSusbcriberDeviceHistory(suid);
    }


    @PostMapping("/api/post/totp")
    public ApiResponse postGetUserAuthData(@RequestBody TotpDto totpDto) {
        return subscriberServiceIface.getTotp(totpDto);

    }

    @DeleteMapping("/api/get/delete-record")
    public ApiResponse deleteSubscriberBySuid(@RequestParam("suid") String subscriberUid) {
        return subscriberServiceIface.deleteRecordBySuid(subscriberUid);
    }

    @PostMapping("/api/save/subscriber-document")
    public ApiResponse saveSubscriberDocument(HttpServletRequest request, @RequestBody SubscriberDocumentDto subscriberDocumentDto) {
        return subscriberServiceIface.saveSubscriberDocument(subscriberDocumentDto);
    }

    @GetMapping("/api/get/all-subscriber-data")
    public ApiResponse getAllSubscribersDataFromView(HttpServletRequest request) {
        logger.info("Came getAllSubscribersDataFromView and called getAllSubscriberdataFromView");
        return subscriberServiceIface.getAllSubscribersDataFromView();
    }
}
