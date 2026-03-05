
package ug.daes.onboarding.service.impl;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import org.hibernate.PessimisticLockException;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.web.util.InvalidUrlException;
import ug.daes.DAESService;

import ug.daes.Result;
import ug.daes.onboarding.config.SentryClientExceptions;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.constant.Constant;
import ug.daes.onboarding.dto.*;

import ug.daes.onboarding.exceptions.*;
import ug.daes.onboarding.model.*;
import ug.daes.onboarding.repository.*;
import ug.daes.onboarding.service.iface.DeviceUpdateIface;
import ug.daes.onboarding.service.iface.SubscriberServiceIface;
import ug.daes.onboarding.service.iface.TemplateServiceIface;
import ug.daes.onboarding.util.*;


@Primary
@Service
public class SubscriberServiceImpl implements SubscriberServiceIface {

    private static final Logger logger = LoggerFactory.getLogger(SubscriberServiceImpl.class);
    private static final String SUCCESS = "api.response.success";
    private static final String BAD_REQUEST = "api.error.bad.request";
    private static final String SUBSCRIBER_CONTROLLER = "SubscriberController";
    private static final String RESULT = "Result";
    private static final String SUBSCRIBER_NOT_FOUND = "api.error.subscriber.not.found";
    private static final String SOMETHING_WENT_WRONG = "api.error.something.went.wrong.please.try.after.sometime";
    private static final String ECXEPTION = "Unexpected exception";
    private static final String CUSTOMER_DETAILS = "customerDetails";
    private static final String EMAIL = "emailId";
    private static final String RESPONSE_OK = "api.response.ok";

    private static final String NO_DATA_FOUND = "api.error.no.data.found";
    private static final String SUBSCRIBER_DETAILS = "api.response.subscriber.details";
    private static final String SUBSCRIBER_DETAILS_DELETED = "api.response.subscriber.record.deleted.successfully";
    private static final String RESIDENCE_INFO = "ResidenceInfo";
    private static final String INVALID_PHONE_NUMBER = "api.error.this.device.is.already.register.with.differet.email.or.mobile.no";
    private static final String MOBILE_NUMBER = "mobileNumber";
    private static final String ACTIVE = "ACTIVE";
    private static final String DEVICE_ID = "deviceId";
    private static final String LOW_LEVEL_ASSURENCE = "api.error.you.are.using.low.level.of.assurance";
    private static final String TERMS_AND_CONDITIONS = "I agreed to above Terms and conditions and Data privacy terms";
    private static final String SUBSCRIBER_DETAILS_NOT_FOUND = "api.error.subscriber.details.not.found";
    private static final String GET_SUBSCRIBER_DETAILS_BY_SEARCH = "getSubscriberDetailsBySerachType request searchType and searchValue {},{}";
    private static final String SUBSCRIBER_UID_CANNOT_NULL = "api.error.subscriberuid.cant.be.null";
    private static final String VALIDATION_ERROR = "Validation errors: {}";

    private static final String CLASS = "SubscriberServiceImpl";


    private final SubscriberRepoIface subscriberRepoIface;
    private final SubscriberDeviceRepoIface deviceRepoIface;
    private final SubscriberDeviceHistoryRepoIface subscriberDeviceHistoryRepoIface;
    private final SubscriberFcmTokenRepoIface fcmTokenRepoIface;
    private final SubscriberOnboardingDataRepoIface onboardingDataRepoIface;
    private final SubscriberStatusRepoIface statusRepoIface;
    private final SubscriberCertificatesRepoIface subscriberCertificatesRepoIface;
    private final SubscriberCertPinHistoryRepoIface subscriberCertPinHistoryRepoIface;
    private final OnboardingLivelinessRepository livelinessRepository;
    private final TemplateServiceIface templateServiceIface;
    private final TrustedUserRepoIface trustedUserRepoIface;
    private final SubscriberDeletionRepository subscriberDeletionRepository;
    private final SubscriberCertificateDetailsRepoIface subscriberCertificateDetailsRepoIface;
    private final SubscriberCompleteDetailRepoIface subscriberCompleteDetailRepoIface;

    private final RestTemplate restTemplate;
    private final SubscriberViewRepoIface subscriberViewRepoIface;
    private final SubscriberConsentsRepo subscriberConsentsRepo;
    private final ConsentHistoryRepo consentHistoryRepo;
    private final SentryClientExceptions sentryClientExceptions;
    private final MinioStorageServiceImpl minioStorageService;
    private final LogModelServiceImpl logModelServiceImpl;
    private final SubscriberHistoryRepo subscriberHistoryRepo;

    private final OrgContactsEmailRepository orgContactsEmailRepository;
    private final DeviceUpdateIface deviceUpdateIface;
    private final ExceptionHandlerUtil exceptionHandlerUtil;
    private final SubscriberPersonalDocumentRepo subscriberPersonalDocumentRepo;


    private final ObjectMapper objectMapper = new ObjectMapper();


    private final boolean isOnboardingFee;
    private final String dtportal;
    private final String priAuthScheme;
    private final boolean priAuthSchemeBoolean;
    private final String emailBaseUrl;
    private final String indApiSMS;
    private final String niraApiSMS;
    private final String niraUserName;
    private final String niraPassword;
    private final String niraApiToken;
    private final String uaeApiSMS;
    private final String registerFaceURL;
    private final boolean registerFaceBoolean;
    private final String testAndroidEmail;
    private final String testIosEmail;
    private final String testAndroidOtp;
    private final String testIosOtp;

    private final String removeBackGroundFromImageURL;
    private final boolean removeBackGroundFromImageBoolean;
    private final int timeToLive;
    private final String trustedUserStatus;
    private final boolean checkDateOfBirth;
    private final boolean checkGender;
    private final boolean checkDocumentNumber;
    private final int expiryDays;
    private final boolean signRequired;

    public SubscriberServiceImpl(
            SubscriberRepoIface subscriberRepoIface,
            SubscriberDeviceRepoIface deviceRepoIface,
            SubscriberDeviceHistoryRepoIface subscriberDeviceHistoryRepoIface,
            SubscriberFcmTokenRepoIface fcmTokenRepoIface,
            SubscriberOnboardingDataRepoIface onboardingDataRepoIface,
            SubscriberStatusRepoIface statusRepoIface,
            SubscriberRaDataRepoIface raRepoIface,
            SubscriberCertificatesRepoIface subscriberCertificatesRepoIface,
            SubscriberCertPinHistoryRepoIface subscriberCertPinHistoryRepoIface,
            OnboardingLivelinessRepository livelinessRepository,
            TemplateServiceIface templateServiceIface,
            TrustedUserRepoIface trustedUserRepoIface,
            SubscriberDeletionRepository subscriberDeletionRepository,
            SubscriberCertificateDetailsRepoIface subscriberCertificateDetailsRepoIface,
            SubscriberCompleteDetailRepoIface subscriberCompleteDetailRepoIface,

            RestTemplate restTemplate,
            SubscriberViewRepoIface subscriberViewRepoIface,
            SubscriberConsentsRepo subscriberConsentsRepo,
            ConsentHistoryRepo consentHistoryRepo,
            SentryClientExceptions sentryClientExceptions,
            MinioStorageServiceImpl minioStorageService,
            LogModelServiceImpl logModelServiceImpl,
            SubscriberHistoryRepo subscriberHistoryRepo,
            OnBoardingMethodRepoIface onBoardingMethodRepoIface,
            OrgContactsEmailRepository orgContactsEmailRepository,
            DeviceUpdateIface deviceUpdateIface,

            ExceptionHandlerUtil exceptionHandlerUtil,
            SubscriberPersonalDocumentRepo subscriberPersonalDocumentRepo,
            @Value("${is.onboarding.fee}") boolean isOnboardingFee,
            @Value("${dtportal.base.url}") String dtportal,
            @Value("${ra.priauth.scheme}") String priAuthScheme,
            @Value("${priauth.url.boolean}") boolean priAuthSchemeBoolean,
            @Value("${email.url}") String emailBaseUrl,
            @Value("${ind.api.sms}") String indApiSMS,
            @Value("${nira.api.sms}") String niraApiSMS,
            @Value("${nira.username}") String niraUserName,
            @Value("${nira.password}") String niraPassword,
            @Value("${nira.api.token}") String niraApiToken,
            @Value("${uae.api.sms}") String uaeApiSMS,
            @Value("${registerface.url}") String registerFaceURL,
            @Value("${register.face.url}") boolean registerFaceBoolean,
            @Value("${test.android.email}") String testAndroidEmail,
            @Value("${test.ios.email}") String testIosEmail,
            @Value("${test.android.mobile.no}") String testAndroidOtp,
            @Value("${test.ios.mobile.no}") String testIosOtp,
            @Value("${border.control.photo.isrequired}") boolean boarderControllPhotoRequired,
            @Value("${remove.background.url}") String removeBackGroundFromImageURL,
            @Value("${remove.background.boolean}") boolean removeBackGroundFromImageBoolean,
            @Value("${nira.api.timetolive}") int timeToLive,
            @Value("${config.validation.allowTrustedUsersOnly}") String trustedUserStatus,
            @Value("${re.onboard.dateofbirth}") boolean checkDateOfBirth,
            @Value("${re.onboard.gender}") boolean checkGender,
            @Value("${re.onboard.documentnumber}") boolean checkDocumentNumber,
            @Value("${expiry.days}") int expiryDays,
            @Value("${signed.required.by.user}") boolean signRequired

    ) {
        this.subscriberRepoIface = subscriberRepoIface;
        this.deviceRepoIface = deviceRepoIface;
        this.subscriberDeviceHistoryRepoIface = subscriberDeviceHistoryRepoIface;
        this.fcmTokenRepoIface = fcmTokenRepoIface;
        this.onboardingDataRepoIface = onboardingDataRepoIface;
        this.statusRepoIface = statusRepoIface;

        this.subscriberCertificatesRepoIface = subscriberCertificatesRepoIface;
        this.subscriberCertPinHistoryRepoIface = subscriberCertPinHistoryRepoIface;
        this.livelinessRepository = livelinessRepository;
        this.templateServiceIface = templateServiceIface;
        this.trustedUserRepoIface = trustedUserRepoIface;
        this.subscriberDeletionRepository = subscriberDeletionRepository;
        this.subscriberCertificateDetailsRepoIface = subscriberCertificateDetailsRepoIface;
        this.subscriberCompleteDetailRepoIface = subscriberCompleteDetailRepoIface;

        this.restTemplate = restTemplate;
        this.subscriberViewRepoIface = subscriberViewRepoIface;
        this.subscriberConsentsRepo = subscriberConsentsRepo;
        this.consentHistoryRepo = consentHistoryRepo;
        this.sentryClientExceptions = sentryClientExceptions;
        this.minioStorageService = minioStorageService;
        this.logModelServiceImpl = logModelServiceImpl;
        this.subscriberHistoryRepo = subscriberHistoryRepo;

        this.orgContactsEmailRepository = orgContactsEmailRepository;
        this.deviceUpdateIface = deviceUpdateIface;

        this.exceptionHandlerUtil = exceptionHandlerUtil;
        this.subscriberPersonalDocumentRepo = subscriberPersonalDocumentRepo;

        this.isOnboardingFee = isOnboardingFee;
        this.dtportal = dtportal;
        this.priAuthScheme = priAuthScheme;
        this.priAuthSchemeBoolean = priAuthSchemeBoolean;
        this.emailBaseUrl = emailBaseUrl;
        this.indApiSMS = indApiSMS;
        this.niraApiSMS = niraApiSMS;
        this.niraUserName = niraUserName;
        this.niraPassword = niraPassword;
        this.niraApiToken = niraApiToken;
        this.uaeApiSMS = uaeApiSMS;
        this.registerFaceURL = registerFaceURL;
        this.registerFaceBoolean = registerFaceBoolean;
        this.testAndroidEmail = testAndroidEmail;
        this.testIosEmail = testIosEmail;
        this.testAndroidOtp = testAndroidOtp;
        this.testIosOtp = testIosOtp;

        this.removeBackGroundFromImageURL = removeBackGroundFromImageURL;
        this.removeBackGroundFromImageBoolean = removeBackGroundFromImageBoolean;
        this.timeToLive = timeToLive;
        this.trustedUserStatus = trustedUserStatus;
        this.checkDateOfBirth = checkDateOfBirth;
        this.checkGender = checkGender;
        this.checkDocumentNumber = checkDocumentNumber;
        this.expiryDays = expiryDays;
        this.signRequired = signRequired;
    }

    public String generateSubscriberUniqueId() {
        String uuid = UUID.randomUUID().toString();
        logger.info("{} Generate Subscriber UniqueId {}", CLASS, uuid);
        return uuid;
    }

    private String encryptedString(String s) {
        try {

            Result result = DAESService.encryptData(s);
            return new String(result.getResponse());
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            return e.getMessage();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public ApiResponse saveSubscribersData(MobileOTPDto subscriberDTO) throws ParseException, UnknownHostException {
        try {
            // 1️⃣ Validate request DTO and mandatory fields
            ApiResponse validationResponse = validateSubscriberDTO(subscriberDTO);
            if (validationResponse != null) return validationResponse;

            // 2️⃣ Check OTP verification
            if (!subscriberDTO.getOtpStatus()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.otp.not.verified");
            }

            // 3️⃣ Check existing subscriber
            ApiResponse existingSubscriberResponse = checkValidationForSubscriber(subscriberDTO);
            if (!existingSubscriberResponse.isSuccess()) {
                if (existingSubscriberResponse.getResult() != null) {
                    existingSubscriberResponse.setSuccess(true);
                }
                return existingSubscriberResponse;
            }

            // 4️⃣ Main save/update logic
            return processSubscriberSave(subscriberDTO);

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            sentryClientExceptions.captureTags(subscriberDTO.getSuID(), subscriberDTO.getSubscriberMobileNumber(),
                    "saveSubscribersData", SUBSCRIBER_CONTROLLER);
            sentryClientExceptions.captureExceptions(e);
            logger.error(CLASS + "saveSubscriberData Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private ApiResponse validateSubscriberDTO(MobileOTPDto dto) {
        String result = ValidationUtil.validate(dto);
        if (result != null) {
            logger.info("saveSubscribersData Validation errors: {}", result);
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage(result, null);
        }
        if (dto.getOsName() == null || dto.getAppVersion() == null
                || dto.getOsVersion() == null || dto.getDeviceInfo() == null) {
            return exceptionHandlerUtil.createErrorResponse("api.error.application.info.not.found");
        }
        if (dto.getFcmToken() == null || dto.getFcmToken().isEmpty()) {
            return exceptionHandlerUtil.createErrorResponse("api.error.fcmtoken.cant.be.null.or.empty");
        }
        return null;
    }

    private ApiResponse processSubscriberSave(MobileOTPDto dto) throws ParseException, UnknownHostException {
        Date startTime = new Date();
        SubscriberRegisterResponseDTO responseDTO = new SubscriberRegisterResponseDTO();
        String suid = generateSubscriberUniqueId();

        Subscriber subscriber = new Subscriber();
        SubscriberDevice subscriberDevice = new SubscriberDevice();
        SubscriberFcmToken fcmToken = new SubscriberFcmToken();
        SubscriberStatusModel subscriberStatus = new SubscriberStatusModel();

        Subscriber previousSuid = subscriberRepoIface.getSubscriberUidByEmailAndMobile(
                dto.getSubscriberEmail(), dto.getSubscriberMobileNumber());

        SubscriberSetupContext context = new SubscriberSetupContext();
        context.setDto(dto);
        context.setSuid(suid);
        context.setPreviousSubscriber(previousSuid);
        context.setSubscriber(subscriber);
        context.setDevice(subscriberDevice);
        context.setFcmToken(fcmToken);
        context.setStatus(subscriberStatus);
        context.setResponseDTO(responseDTO);

// Call the refactored setup method
        setupSubscriberObjects(context);

        subscriber = subscriberRepoIface.save(subscriber);
        updateOrSaveDevice(dto, previousSuid, subscriberDevice);

        setFirstTimeOnboardingFlag(previousSuid, responseDTO);

        return finalizeSubscriberResponse(startTime, subscriber, responseDTO);
    }

    private void setupSubscriberObjects(SubscriberSetupContext context) {
        MobileOTPDto dto = context.getDto();
        String suid = context.getSuid();
        Subscriber previousSuid = context.getPreviousSubscriber();
        Subscriber subscriber = context.getSubscriber();
        SubscriberDevice device = context.getDevice();
        SubscriberFcmToken fcmToken = context.getFcmToken();
        SubscriberStatusModel status = context.getStatus();
        SubscriberRegisterResponseDTO responseDTO = context.getResponseDTO();

        if (previousSuid != null) {
            // Fetch previous linked objects
            SubscriberDevice preDevice = deviceRepoIface.getSubscriber(previousSuid.getSubscriberUid());
            SubscriberFcmToken preFcm = fcmTokenRepoIface.findBysubscriberUid(previousSuid.getSubscriberUid());
            SubscriberStatusModel preStatus = statusRepoIface.findBysubscriberUid(previousSuid.getSubscriberUid());

            // Save device history if disabled
            if (preDevice.getDeviceStatus().equals(Constant.DEVICE_STATUS_DISABLED)) {
                SubscriberDeviceHistory history = new SubscriberDeviceHistory();
                history.setSubscriberUid(previousSuid.getSubscriberUid());
                history.setDeviceUid(preDevice.getDeviceUid());
                history.setDeviceStatus(Constant.DEVICE_STATUS_DISABLED);
                history.setCreatedDate(AppUtil.getDate());
                history.setUpdatedDate(AppUtil.getDate());
                subscriberDeviceHistoryRepoIface.save(history);
            }

            // Copy previous IDs
            subscriber.setSubscriberId(previousSuid.getSubscriberId());
            subscriber.setSubscriberUid(previousSuid.getSubscriberUid());
            device.setSubscriberDeviceId(preDevice.getSubscriberDeviceId());
            device.setSubscriberUid(previousSuid.getSubscriberUid());
            fcmToken.setSubscriberFcmTokenId(preFcm.getSubscriberFcmTokenId());
            fcmToken.setSubscriberUid(previousSuid.getSubscriberUid());
            status.setSubscriberStatusId(preStatus.getSubscriberStatusId());
            status.setSubscriberUid(previousSuid.getSubscriberUid());

            responseDTO.setSuID(previousSuid.getSubscriberUid());
        } else {
            // Setup for new subscriber
            subscriber.setSubscriberUid(suid);
            device.setSubscriberUid(suid);
            fcmToken.setSubscriberUid(suid);
            status.setSubscriberUid(suid);
            responseDTO.setSuID(suid);
        }

        // Common setup for both new and existing subscriber objects
        subscriber.setCreatedDate(AppUtil.getDate());
        subscriber.setUpdatedDate(AppUtil.getDate());
        subscriber.setEmailId(dto.getSubscriberEmail().toLowerCase());
        subscriber.setMobileNumber(dto.getSubscriberMobileNumber());
        subscriber.setFullName(dto.getSubscriberName());
        subscriber.setOsName(dto.getOsName());
        subscriber.setOsVersion(dto.getOsVersion());
        subscriber.setDeviceInfo(dto.getDeviceInfo());
        subscriber.setAppVersion(dto.getAppVersion());

        device.setCreatedDate(AppUtil.getDate());
        device.setUpdatedDate(AppUtil.getDate());
        device.setDeviceUid(dto.getDeviceId());
        device.setDeviceStatus(Constant.DEVICE_STATUS_ACTIVE);

        fcmToken.setCreatedDate(AppUtil.getDate());
        fcmToken.setFcmToken(dto.getFcmToken());

        status.setOtpVerifiedStatus(Constant.OTP_VERIFIED_STATUS);
        status.setSubscriberStatus(Constant.SUBSCRIBER_STATUS);
        status.setCreatedDate(AppUtil.getDate());
        status.setUpdatedDate(AppUtil.getDate());
    }

    private void updateOrSaveDevice(MobileOTPDto dto, Subscriber previousSuid, SubscriberDevice device) {
        if (previousSuid != null) {
            SubscriberDevice oldDevice = deviceRepoIface.getSubscriber(previousSuid.getSubscriberUid());
            deviceRepoIface.updateSubscriber(dto.getDeviceId(), ACTIVE, AppUtil.getDate(), oldDevice.getSubscriberDeviceId());
        } else {
            deviceRepoIface.save(device);
        }
    }

    private void setFirstTimeOnboardingFlag(Subscriber previousSuid, SubscriberRegisterResponseDTO responseDTO) {
        if (previousSuid != null) {
            List<String> firstTimeList = subscriberRepoIface.firstTimeOnboardingPaymentStatus(previousSuid.getSubscriberUid());
            responseDTO.setFirstTimeOnboarding(firstTimeList.isEmpty());
        } else {
            responseDTO.setFirstTimeOnboarding(true);
        }
    }

    private ApiResponse finalizeSubscriberResponse(Date startTime, Subscriber subscriber, SubscriberRegisterResponseDTO responseDTO) throws ParseException {
        responseDTO.setSubscriberStatus(Constant.SUBSCRIBER_STATUS);
        Date endTime = new Date();
        double totalTime = AppUtil.getDifferenceInSeconds(startTime, endTime);

        logModelServiceImpl.setLogModel(true, subscriber.getSubscriberUid(), null,
                "SUBSCRIBER_REGISTRATION", subscriber.getSubscriberUid(), String.valueOf(totalTime),
                startTime, endTime, null);

        // handle consents if required
        if (!signRequired) {
            handleConsents(responseDTO.getSuID());
        }

        return exceptionHandlerUtil.createSuccessResponse(
                "api.response.subscriber.email.and.mobile.number.is.verified", responseDTO);
    }

    private void handleConsents(String suid) {
        List<ConsentHistory> latestConsentList = consentHistoryRepo.findLatestConsent();
        if (!latestConsentList.isEmpty()) {
            ConsentHistory consent = latestConsentList.get(0);
            if (subscriberConsentsRepo.findSubscriberConsentBySuidAndConsentId(suid, consent.getId()) == null) {
                SubscriberConsents sc = new SubscriberConsents();
                sc.setCreatedOn(AppUtil.getDate());
                sc.setConsentData(TERMS_AND_CONDITIONS);
                sc.setSuid(suid);
                sc.setConsentId(consent.getId());
                subscriberConsentsRepo.save(sc);
            }
        }
    }

    public ApiResponse checkValidationForSubscriber(MobileOTPDto mobileOTPDto) throws UnknownHostException {
        logger.info("{}{} - Request received in checkValidationForSubscriber: {}", CLASS, Utility.getMethodName(),
                mobileOTPDto);

        SubscriberRegisterResponseDTO responseDTO = new SubscriberRegisterResponseDTO();

        try {
            if (!mobileOTPDto.getOtpStatus()) {
                logger.info("{}{} - OTP verification failed", CLASS, Utility.getMethodName());
                return exceptionHandlerUtil.createErrorResponse("api.error.otp.verification.is.failed");
            }

            int countDevice = subscriberRepoIface.countSubscriberDevice(mobileOTPDto.getDeviceId());
            int countMobile = subscriberRepoIface.countSubscriberMobile(mobileOTPDto.getSubscriberMobileNumber());
            int countEmail = subscriberRepoIface.countSubscriberEmailId(mobileOTPDto.getSubscriberEmail().toLowerCase());

            logger.info("{}{} - Counts: device={}, mobile={}, email={}", CLASS, Utility.getMethodName(),
                    countDevice, countMobile, countEmail);

            Subscriber previousSuid = getPreviousSubscriberIfExists(mobileOTPDto, countDevice, countMobile, countEmail);

            if (previousSuid != null) {
                return handleExistingSubscriber(mobileOTPDto, previousSuid, responseDTO);
            } else {
                return handleNewOrUnregisteredDevice(mobileOTPDto, countDevice, countMobile, countEmail);
            }

        } catch (Exception e) {
            logger.error("{}{} - Exception: {}", CLASS, Utility.getMethodName(), e.getMessage());
            logger.error(ECXEPTION, e);
            sentryClientExceptions.captureTags(mobileOTPDto.getSuID(), mobileOTPDto.getSubscriberMobileNumber(),
                    "checkValidationSubscriber", SUBSCRIBER_CONTROLLER);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

// ------------------------ Helper Methods ------------------------

    private Subscriber getPreviousSubscriberIfExists(MobileOTPDto dto, int countDevice, int countMobile, int countEmail) {
        // Only check if device, mobile, and email counts match criteria
        if (countDevice < 1 || countMobile != 1 || countEmail != 1) {
            return null;
        }

        Subscriber previousSubscriber = subscriberRepoIface.getSubscriberDetailsByEmailAndMobile(
                dto.getSubscriberEmail().toLowerCase(),
                dto.getSubscriberMobileNumber()
        );

        if (previousSubscriber == null) {
            throw new SubscriberValidationException(
                    "api.error.this.mobile.no.is.already.register.with.different.email.id"
            );
        }

        return previousSubscriber;
    }

    private ApiResponse handleExistingSubscriber(MobileOTPDto dto, Subscriber previousSuid,
                                                 SubscriberRegisterResponseDTO responseDTO) {

        SubscriberDevice deviceDetails = deviceRepoIface.getSubscriber(previousSuid.getSubscriberUid());
        SubscriberFcmToken fcmToken = fcmTokenRepoIface.findBysubscriberUid(previousSuid.getSubscriberUid());
        Subscriber subscriber = subscriberRepoIface.findBysubscriberUid(previousSuid.getSubscriberUid());
        SubscriberStatusModel subscriberStatus = statusRepoIface.findBysubscriberUid(previousSuid.getSubscriberUid());

        // Validate device ownership
        SubscriberDevice deviceFromRepo = deviceRepoIface.findBydeviceUidAndStatus(dto.getDeviceId(), ACTIVE);
        if (deviceFromRepo != null && !previousSuid.getSubscriberUid().equals(deviceFromRepo.getSubscriberUid())) {
            return exceptionHandlerUtil.createErrorResponse(
                    INVALID_PHONE_NUMBER);
        }

        // Prepare FCM Token
        if (fcmToken == null) {
            fcmToken = new SubscriberFcmToken();
            fcmToken.setSubscriberUid(previousSuid.getSubscriberUid());
            fcmToken.setCreatedDate(AppUtil.getDate());
        }
        fcmToken.setFcmToken(dto.getFcmToken());

        // Update device
        deviceDetails.setDeviceUid(dto.getDeviceId());
        deviceDetails.setDeviceStatus(Constant.DEVICE_STATUS_ACTIVE);
        deviceDetails.setUpdatedDate(AppUtil.getDate());
        deviceRepoIface.save(deviceDetails);

        // Check email/mobile mismatch
        if (!subscriber.getEmailId().equals(dto.getSubscriberEmail().toLowerCase())
                || !subscriber.getMobileNumber().equals(dto.getSubscriberMobileNumber())) {
            return exceptionHandlerUtil.createErrorResponse(
                    INVALID_PHONE_NUMBER);
        }

        // Set response status
        if (subscriberStatus != null) {
            responseDTO.setSubscriberStatus(subscriberStatus.getSubscriberStatus());
        } else {
            responseDTO.setSubscriberStatus(Constant.SUBSCRIBER_STATUS);
        }

        responseDTO.setSuID(previousSuid.getSubscriberUid());
        populateSubscriberDetails(subscriber, responseDTO);

        // Payment Status
        List<String> paymentStatus = subscriberRepoIface.subscriberPaymnetStatus(subscriber.getSubscriberUid());
        responseDTO.setOnboardingPaymentStatus(
                paymentStatus.isEmpty() ? Constant.PAYMENT_STATUS_PENDING : paymentStatus.get(0));

        responseDTO.setFirstTimeOnboarding(
                subscriberRepoIface.firstTimeOnboardingPaymentStatus(subscriber.getSubscriberUid()).isEmpty());

        // Consent
        addConsentIfRequired(deviceDetails.getSubscriberUid());

        return exceptionHandlerUtil.createErrorResponseWithResult(
                "api.error.this.device.is.already.registered.please.continue", responseDTO);
    }

    private void populateSubscriberDetails(Subscriber subscriber, SubscriberRegisterResponseDTO responseDTO) {
        SubscriberOnboardingData latestData = onboardingDataRepoIface
                .findLatestSubscriber(subscriber.getSubscriberUid()).stream().findFirst().orElse(null);

        if (latestData == null) {
            responseDTO.setSubscriberDetails(null);
            return;
        }

        SubscriberDetails details = new SubscriberDetails();
        details.setSubscriberName(subscriber.getFullName());
        details.setOnboardingMethod(latestData.getOnboardingMethod());

        ApiResponse editTemplateRes = templateServiceIface.getTemplateLatestById(latestData.getTemplateId());
        if (editTemplateRes.isSuccess()) {
            EditTemplateDTO templateDTO = (EditTemplateDTO) editTemplateRes.getResult();
            details.setTemplateDetails(templateDTO);
        }

        String certStatus = subscriberCertificatesRepoIface.getSubscriberCertificateStatus(
                subscriber.getSubscriberUid(), Constant.SIGN, Constant.ACTIVE).stream().findFirst().orElse(Constant.PENDING);

        details.setCertificateStatus(certStatus);

        PinStatus pinStatus = new PinStatus();
        if (Constant.ACTIVE.equals(certStatus)) {
            SubscriberCertificatePinHistory pinHistory = subscriberCertPinHistoryRepoIface
                    .findBysubscriberUid(subscriber.getSubscriberUid());
            if (pinHistory != null) {
                pinStatus.setAuthPinSet(pinHistory.getAuthPinList() != null);
                pinStatus.setSignPinSet(pinHistory.getSignPinList() != null);
            }
        }
        details.setPinStatus(pinStatus);
        responseDTO.setSubscriberDetails(details);
    }

    private void addConsentIfRequired(String suid) {
        if (!signRequired) {
            String consentData = TERMS_AND_CONDITIONS;
            ConsentHistory latestConsent = consentHistoryRepo.findLatestConsent().stream().findFirst().orElse(null);
            if (latestConsent != null) {
                SubscriberConsents existing = subscriberConsentsRepo.findSubscriberConsentBySuidAndConsentId(suid,
                        latestConsent.getId());
                if (existing == null) {
                    SubscriberConsents consent = new SubscriberConsents();
                    consent.setConsentData(consentData);
                    consent.setSuid(suid);
                    consent.setCreatedOn(AppUtil.getDate());
                    consent.setConsentId(latestConsent.getId());
                    subscriberConsentsRepo.save(consent);
                }
            }
        }
    }

    private ApiResponse handleNewOrUnregisteredDevice(MobileOTPDto dto, int countDevice, int countMobile, int countEmail) {
        // Handle unregistered/new subscriber validations exactly like original code
        if (countDevice == 0) {
            int activeDeviceCount = subscriberCompleteDetailRepoIface
                    .getActiveDeviceCountStatusByEmailAndMobileNo(Constant.ACTIVE,
                            dto.getSubscriberEmail(), dto.getSubscriberMobileNumber());
            if (activeDeviceCount != 0) {
                if (countEmail == 1) {
                    return exceptionHandlerUtil.createErrorResponse(
                            "api.error.this.email.id.is.already.register.with.different.device.please.deactivate.the.other.device");
                }
                if (countMobile == 1) {
                    return exceptionHandlerUtil.createErrorResponse(
                            "api.error.this.mobile.no.is.already.register.with.different.device.please.deactivate.the.other.device");
                }
            }
        } else if (countDevice >= 1) {
            if (countEmail == 0) {
                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.this.device.is.already.registered.with.different.email");
            }
            if (countMobile == 0) {
                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.this.device.is.already.register.with.different.mobile.number");
            }
        }

        return exceptionHandlerUtil.successResponse(SUCCESS);
    }

    @Override
    public ApiResponse saveSubscriberDocument(SubscriberDocumentDto subscriberDocumentDto) {
        try {
            SubscriberPersonalDocument personalDocument = subscriberPersonalDocumentRepo
                    .findBySubscriberUniqueId(subscriberDocumentDto.getSubscriberUID());

            if (personalDocument != null) {
                personalDocument.setSubscriberUniqueId(subscriberDocumentDto.getSubscriberUID());
                personalDocument.setDocument(subscriberDocumentDto.getDocument());
                personalDocument.setUpdatedDate(AppUtil.getDate());
                subscriberPersonalDocumentRepo.save(personalDocument);

                return exceptionHandlerUtil.successResponse("api.response.subscriber.document.updated.successfully");
            } else {
                SubscriberPersonalDocument subscriberPersonalDocument = new SubscriberPersonalDocument();
                subscriberPersonalDocument.setSubscriberUniqueId(subscriberDocumentDto.getSubscriberUID());
                subscriberPersonalDocument.setDocument(subscriberDocumentDto.getDocument());
                subscriberPersonalDocument.setCreatedDate(AppUtil.getDate());
                subscriberPersonalDocument.setUpdatedDate(AppUtil.getDate());
                subscriberPersonalDocumentRepo.save(subscriberPersonalDocument);

                return exceptionHandlerUtil.successResponse("api.response.subscriber.document.save.successfully");
            }

        } catch (JDBCConnectionException | ConstraintViolationException | DataException | LockAcquisitionException
                 | PessimisticLockException | QueryTimeoutException | SQLGrammarException | GenericJDBCException e) {
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);

        }
    }


    @Override
    public ApiResponse addSubscriberObData(SubscriberObRequestDTO obRequestDTO) {
        Date startTime = new Date();
        try {

            ApiResponse validationResponse = validateSubscriberObRequest(obRequestDTO);
            if (validationResponse != null) return validationResponse;

            Subscriber subscriber = subscriberRepoIface.findBysubscriberUid(obRequestDTO.getSuID());
            if (subscriber == null) return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_NOT_FOUND);

            SubscriberObData subscriberObData = obRequestDTO.getSubscriberData();


            ApiResponse optionalDataCheckResponse = validateOptionalData(obRequestDTO, subscriberObData);
            if (optionalDataCheckResponse != null) return optionalDataCheckResponse;

            ApiResponse deviceAndIdValidation = validateDeviceAndIdDoc(obRequestDTO, subscriberObData);
            if (deviceAndIdValidation != null) return deviceAndIdValidation;


            updateSubscriber(subscriber, obRequestDTO, subscriberObData);


            SubscriberOnboardingData onboardingData = prepareOnboardingData(obRequestDTO, subscriberObData);


            parseNiraResponseAndPhoto(obRequestDTO, subscriberObData, onboardingData, subscriber);


            handleSelfie(obRequestDTO, subscriberObData, onboardingData);


            Subscriber savedSubscriber = subscriberRepoIface.save(subscriber);
            onboardingData = saveOnboardingData(subscriberObData, onboardingData, savedSubscriber);

            updateSubscriberStatus(onboardingData.getSubscriberUid());


            logOnboardingTime(startTime, savedSubscriber, onboardingData);

            return exceptionHandlerUtil.createSuccessResponse(
                    "api.response.ugpass.application.submitted.successfully", savedSubscriber);

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            logger.error("{}{} - Subscriber OnBoarding Data Exception: {}", CLASS, Utility.getMethodName(), e.getMessage());
            sentryClientExceptions.captureExceptions(e);
            return AppUtil.createApiResponse(false, SOMETHING_WENT_WRONG, null);
        }
    }


    private ApiResponse validateSubscriberObRequest(SubscriberObRequestDTO obRequestDTO) {
        if (obRequestDTO == null) return exceptionHandlerUtil
                .createErrorResponse("api.error.subscriber.ob.request.cant.be.null.or.empty");

        String validationMessage = ValidationUtil.validate(obRequestDTO);
        if (validationMessage != null) {
            logger.info(" addSubscriberObData Validation errors:{} ", validationMessage);
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage(validationMessage, null);
        }

        String subscriberData = ValidationUtil.validate(obRequestDTO.getSubscriberData());
        if (subscriberData != null) {
            logger.info(" addSubscriberObData Validation errors: {}", subscriberData);
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage(subscriberData, null);
        }
        return null;
    }

    private ApiResponse validateOptionalData(SubscriberObRequestDTO obRequestDTO, SubscriberObData subscriberObData) {
        String type = obRequestDTO.getSubscriberType();
        if ((!isOnboardingFee && "Citizen".equals(type)) || (isOnboardingFee && !Constant.RESIDENT.equals(type))) {
            return checkOptionalData1(obRequestDTO, subscriberObData);
        }
        return null;
    }

    private ApiResponse checkOptionalData1(SubscriberObRequestDTO obRequestDTO, SubscriberObData subscriberObData) {
        if (subscriberObData.getOptionalData1() == null || subscriberObData.getOptionalData1().isEmpty())
            return exceptionHandlerUtil.createErrorResponse("api.error.optional.data.is.empty");

        int count = isOptionData1Present(subscriberObData.getOptionalData1());
        if (count == 1) {
            String suid = onboardingDataRepoIface.getOptionalData1Subscriber(subscriberObData.getOptionalData1());
            if (!suid.equals(obRequestDTO.getSuID())) {
                logger.info("{}{} - Onboarding cannot be processed because the same national ID already exists: {}",
                        CLASS, Utility.getMethodName(), count);
                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.onboarding.can.not.be.processed.because.the.same.national.id.already.exists");
            }
        }
        return null;
    }

    private ApiResponse validateDeviceAndIdDoc(SubscriberObRequestDTO obRequestDTO,
                                               SubscriberObData subscriberObData) {

        SubscriberDevice subscriberDevice = deviceRepoIface.getSubscriber(obRequestDTO.getSuID());
        if (Constant.DEVICE_STATUS_DISABLED.equals(subscriberDevice.getDeviceStatus()))
            return exceptionHandlerUtil.createErrorResponse("api.error.this.device.is.disabled");

        int idDocCount = subscriberRepoIface.getIdDocCount(subscriberObData.getDocumentNumber());
        int idDocNumberCount = subscriberRepoIface.getSubscriberIdDocNumber(subscriberObData.getDocumentNumber(),
                obRequestDTO.getSuID());
        if (idDocCount > 0 && idDocNumberCount == 0)
            return exceptionHandlerUtil.createErrorResponse("api.error.this.document.is.already.onboarded");

        return null;
    }

    private void updateSubscriber(Subscriber subscriber, SubscriberObRequestDTO obRequestDTO,
                                  SubscriberObData subscriberObData) {
        String fullName = Stream.of(subscriberObData.getSecondaryIdentifier(), subscriberObData.getPrimaryIdentifier())
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(s -> s.replaceAll("\\s+", " ").trim())
                .collect(Collectors.joining(" "));
        subscriber.setFullName(fullName);
        subscriber.setDateOfBirth(subscriberObData.getDateOfBirth());
        subscriber.setIdDocType(subscriberObData.getDocumentType());
        subscriber.setIdDocNumber(subscriberObData.getDocumentNumber());
        subscriber.setUpdatedDate(AppUtil.getDate());
        subscriber.setSubscriberUid(obRequestDTO.getSuID());

        if (!Constant.RESIDENT.equals(obRequestDTO.getSubscriberType()))
            subscriber.setNationalId(subscriberObData.getOptionalData1());
    }

    private SubscriberOnboardingData prepareOnboardingData(SubscriberObRequestDTO obRequestDTO,
                                                           SubscriberObData subscriberObData) {
        SubscriberOnboardingData onboardingData = new SubscriberOnboardingData();
        onboardingData.setCreatedDate(AppUtil.getDate());
        onboardingData.setIdDocType(subscriberObData.getDocumentType());
        onboardingData.setIdDocNumber(subscriberObData.getDocumentNumber());
        onboardingData.setOnboardingMethod(obRequestDTO.getOnboardingMethod());
        onboardingData.setSubscriberUid(obRequestDTO.getSuID());
        onboardingData.setTemplateId(obRequestDTO.getTemplateId());
        onboardingData.setSubscriberType(obRequestDTO.getSubscriberType());
        onboardingData.setIdDocCode(subscriberObData.getDocumentCode());
        onboardingData.setGender(subscriberObData.getGender());
        onboardingData.setGeolocation(subscriberObData.getGeoLocation());
        onboardingData.setOptionalData1(
                subscriberObData.getOptionalData1() != null && !subscriberObData.getOptionalData1().isEmpty()
                        ? subscriberObData.getOptionalData1() : subscriberObData.getDocumentNumber());
        onboardingData.setDateOfExpiry(subscriberObData.getDateOfExpiry());
        return onboardingData;
    }

    private void parseNiraResponseAndPhoto(SubscriberObRequestDTO obRequestDTO, SubscriberObData subscriberObData,
                                           SubscriberOnboardingData onboardingData, Subscriber subscriber) throws Exception {

        String photo = null;
        if (subscriberObData.getNiraResponse() != null && !subscriberObData.getNiraResponse().isEmpty()) {
            if (isOnboardingFee) {
                Result result = DAESService.decryptSecureWireData(subscriberObData.getNiraResponse());
                photo = new String(result.getResponse());
            } else {
                JsonNode root = objectMapper.readTree(subscriberObData.getNiraResponse());
                JsonNode dataNode = root.path(CUSTOMER_DETAILS).path(RESULT).path("Data");
                photo = dataNode.path("Documents").path("PersonFace").asText(null);

                Optional.ofNullable(dataNode.path("ActivePassport").path("DocumentNo").asText(null))
                        .ifPresent(subscriber::setPassportNumber);
                Optional.ofNullable(dataNode.path(RESIDENCE_INFO).path("EmiratesIdNumber").asText(null))
                        .ifPresent(subscriber::setNationalIdNumber);
                Optional.ofNullable(dataNode.path(RESIDENCE_INFO).path("DocumentNo").asText(null))
                        .ifPresent(subscriber::setNationalIdCardNumber);

                if ("NIN".equalsIgnoreCase(obRequestDTO.getOnboardingMethod()) &&
                        subscriber.getNationalIdNumber() != null)
                    subscriber.setIdDocNumber(subscriber.getNationalIdNumber());
                else if ("PASSPORT".equalsIgnoreCase(obRequestDTO.getOnboardingMethod()) &&
                        subscriber.getPassportNumber() != null)
                    subscriber.setIdDocNumber(subscriber.getPassportNumber());

                String hash = AppUtil.hmacSha256Base64(root.toString());
                onboardingData.setDocumentResponseHash(hash);
            }
            onboardingData.setVerifierProvidedPhoto(photo);
            onboardingData.setNiraResponse(subscriberObData.getNiraResponse());
        } else {
            onboardingData.setVerifierProvidedPhoto(subscriberObData.getSubscriberSelfie());
        }
    }


    private void handleSelfie(SubscriberObRequestDTO obRequestDTO, SubscriberObData subscriberObData,
                              SubscriberOnboardingData onboardingData) throws SelfieProcessingException {

        try {
            Selfie selfie = new Selfie();
            selfie.setSubscriberUniqueId(obRequestDTO.getSuID());


            if (removeBackGroundFromImageBoolean) {
                ApiResponse apiResponseGetImage = getImageWithOutBackGround(
                        subscriberObData.getDocumentNumber(),
                        subscriberObData.getSubscriberSelfie()
                );
                selfie.setSubscriberSelfie(apiResponseGetImage.getResult() != null
                        ? apiResponseGetImage.getResult().toString()
                        : subscriberObData.getSubscriberSelfie());
            } else {
                selfie.setSubscriberSelfie(subscriberObData.getSubscriberSelfie());
            }

            if (isOnboardingFee) {
                ApiResponse apiResponse = minioStorageService.saveFileToMinio(selfie, "selfie", null).get();
                if (!apiResponse.isSuccess()) {
                    throw new SelfieProcessingException("Failed to save selfie: " + apiResponse.getMessage());
                }
                onboardingData.setSelfieUri(apiResponse.getResult().toString());
            } else {
                onboardingData.setSelfie(subscriberObData.getSubscriberSelfie());
            }

            // Create thumbnail
            ApiResponse selfieApiResponse = minioStorageService.createThumbnailOfSelfie(selfie).get();
            if (selfieApiResponse.isSuccess()) {
                onboardingData.setSelfieThumbnailUri(selfieApiResponse.getResult().toString());
            } else {
                throw new SelfieProcessingException("Failed to create selfie thumbnail: " + selfieApiResponse.getMessage());
            }

        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new SelfieProcessingException("Error processing selfie asynchronously", e);
        } catch (Exception e) {
            throw new SelfieProcessingException("Unexpected error while handling selfie", e);
        }
    }

    private SubscriberOnboardingData saveOnboardingData(
            SubscriberObData subscriberObData,
            SubscriberOnboardingData onboardingData,
            Subscriber savedSubscriber) throws JsonProcessingException {
        SubscriberObData additionalFile = subscriberObData;
        additionalFile.setSubscriberSelfie(null);
        additionalFile.setSubscriberUniqueId(onboardingData.getSubscriberUid());
        String additionalFieldSaved = objectMapper.writeValueAsString(additionalFile);
        onboardingData.setOnboardingDataFieldsJson(additionalFieldSaved);
        onboardingData.setRemarks(subscriberObData.getRemarks());

        FaceFeaturesDto faceFeaturesDto = new FaceFeaturesDto();
        faceFeaturesDto.setSuid(onboardingData.getSubscriberUid());
        faceFeaturesDto.setSubscriberName(savedSubscriber.getFullName());
        faceFeaturesDto.setSubscriberDataJson(additionalFieldSaved);

        if (registerFaceBoolean) executeRegisterFace(faceFeaturesDto);

        return onboardingDataRepoIface.save(onboardingData);
    }

    private void updateSubscriberStatus(String suid) {
        SubscriberStatusModel status = statusRepoIface.findBysubscriberUid(suid);
        String subStatus = subscriberRepoIface.getSubscriberStatus(suid);

        if (subStatus == null) {
            status.setSubscriberStatus(isOnboardingFee ? "ONBOARDED" : ACTIVE);
            status.setSubscriberStatusDescription(Constant.ONBOARDED_SUCESSFULLY);
        } else if (Constant.ACTIVE.equals(subStatus) || Constant.PIN_SET_REQUIRED.equals(subStatus)) {
            status.setSubscriberStatus(subStatus);
            status.setSubscriberStatusDescription(Constant.LOA_UPDATED);
        } else {
            status.setSubscriberStatus(isOnboardingFee ? "ONBOARDED" : ACTIVE);
            status.setSubscriberStatusDescription(Constant.ONBOARDED_SUCESSFULLY);
        }
        status.setUpdatedDate(AppUtil.getDate());
        statusRepoIface.save(status);
    }

    private void logOnboardingTime(Date startTime, Subscriber subscriber, SubscriberOnboardingData onboardingData) throws ParseException {
        Date endTime = new Date();
        double totalTime = AppUtil.getDifferenceInSeconds(startTime, endTime);
        logModelServiceImpl.setLogModel(true, subscriber.getSubscriberUid(), onboardingData.getGeolocation(),
                Constant.SUBSCRIBER_ONBOARDED, subscriber.getSubscriberUid(), String.valueOf(totalTime),
                startTime, endTime, null);
    }

    private void executeRegisterFace(FaceFeaturesDto faceFeaturesDto) {
        try {
            ExecutorService executor1 = Executors.newFixedThreadPool(1000);
            Runnable registerFaceWorkerThread =
                    new RegisterFaceWorkerThread(registerFaceURL, faceFeaturesDto);
            executor1.execute(registerFaceWorkerThread);
            executor1.shutdown();
        } catch (Exception e) {
            logger.error("{} Error while executing register face thread", CLASS, e);
        }
    }


    public ApiResponse getImageWithOutBackGround(String idDocNumber, String selfie) {
        String boarderControlImage;
        logger.info(" Fetch Border Controll image getImageWithOutBackGround doc number {}", idDocNumber);
        String image = subscriberRepoIface.getSimulatedBoarderControlImage(idDocNumber);
        try {

            String jsonString;
            if (selfie != null || !selfie.isEmpty()) {
                jsonString = createJsonString(selfie);
            } else {
                jsonString = createJsonString(image);
            }
            HttpHeaders headers2 = new HttpHeaders();
            headers2.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> reqEntity2 = new HttpEntity<>(jsonString, headers2);
            ResponseEntity<ApiResponse> res2 = restTemplate.exchange(removeBackGroundFromImageURL, HttpMethod.POST,
                    reqEntity2, ApiResponse.class);
            if (res2.getStatusCodeValue() == 200 || res2.getStatusCodeValue() == 201) {
                boarderControlImage = res2.getBody().getResult().toString();
            } else {
                boarderControlImage = image;
            }
        } catch (Exception e) {
            logger.info(" getImageWithOutBackGround ");
            logger.error(ECXEPTION, e);
            boarderControlImage = image;
        }
        return AppUtil.createApiResponse(true, "fetch image without background", boarderControlImage);
    }

    public static String createJsonString(String image) {

        return "{\n" + "\"image\": \"" + image + "\"\n" + "}";
    }

    private void validateUrl(String url) throws InvalidUrlException {
        if (url == null || url.trim().isEmpty()) {
            throw new InvalidUrlException("URL cannot be null or empty");
        }

        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new InvalidUrlException("URL is not valid: " + e.getMessage());
        }


        if (!"https".equalsIgnoreCase(uri.getScheme())) {
            throw new InvalidUrlException("Only HTTPS protocol is allowed");
        }


        String allowedHost = "internal-edms.company.com"; // replace with your host
        if (!allowedHost.equalsIgnoreCase(uri.getHost())) {
            throw new InvalidUrlException("Unauthorized host detected: " + uri.getHost());
        }


        InetAddress address;
        try {
            address = InetAddress.getByName(uri.getHost());
        } catch (UnknownHostException e) {
            throw new InvalidUrlException("Unable to resolve host: " + e.getMessage());
        }

        if (address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isSiteLocalAddress()) {
            throw new InvalidUrlException("Access to internal/private IPs is not allowed");
        }
    }

    @Override
    public ApiResponse getSubscriberObData(HttpServletRequest request, GetSubscriberObDataDTO subscriberUID) {
        logger.info(CLASS + " getSubscriberObData req {}", subscriberUID);


        String validationResult = ValidationUtil.validate(subscriberUID);
        if (validationResult != null) {
            logger.info("Validation errors getSubscriberObData: {}", validationResult);
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage(validationResult, null);
        }

        try {

            Subscriber subscriber = fetchSubscriber(subscriberUID);
            if (subscriber == null) {
                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_DETAILS_NOT_FOUND);
            }


            executeVersionComparator(subscriber, request);


            SubscriberOnboardingData onboardingData = fetchLatestOnboardingData(subscriberUID.getSuid());
            SubscriberStatusModel status = statusRepoIface.findBysubscriberUid(subscriberUID.getSuid());
            CertificateDetailDto certificateDetailDto = fetchCertificateDetails(subscriberUID.getSuid());


            SubscriberObRequestDTO obRequestDTO = buildSubscriberObRequestDTO(subscriberUID, subscriber, onboardingData,
                    status, certificateDetailDto);

            return exceptionHandlerUtil.createSuccessResponse("api.response.subscriber.onboarding.data", obRequestDTO);

        } catch (Exception e) {
            logger.error(CLASS + " getSubscriberObData Exception {}", e.getMessage(), e);
            return exceptionHandlerUtil.handleException(e);
        }
    }


    private Subscriber fetchSubscriber(GetSubscriberObDataDTO subscriberUID) {
        if (subscriberUID == null) return null;
        return subscriberRepoIface.findBysubscriberUid(subscriberUID.getSuid());
    }

    private SubscriberOnboardingData fetchLatestOnboardingData(String suid) {
        List<SubscriberOnboardingData> dataList = onboardingDataRepoIface.getBySubUid(suid);
        if (dataList == null || dataList.isEmpty()) return null;
        return dataList.size() > 1 ? findLatestOnboardedSub(dataList) : dataList.get(0);
    }

    private CertificateDetailDto fetchCertificateDetails(String suid) throws JsonProcessingException {
        List<SubscriberCertificate> certificates = subscriberCertificatesRepoIface.findBySubscriberUniqueId(suid);
        SubscriberCertificate latestCert = certificates.isEmpty() ? null : certificates.get(0);
        if (latestCert == null) return new CertificateDetailDto();

        CertificateDetailDto dto = new CertificateDetailDto();


        if (latestCert.getCertificateIssueDate() != null) {
            LocalDate issueDate = latestCert.getCertificateIssueDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            dto.setIssueDate(issueDate.toString());
        }

        if (latestCert.getCerificateExpiryDate() != null) {
            LocalDate expiryDate = latestCert.getCerificateExpiryDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            dto.setExpiryDate(expiryDate.toString());
        }

        dto.setCertStatus(latestCert.getCertificateStatus());

        if (latestCert.getUpdatedDate() != null) {
            LocalDate revokeDate = latestCert.getUpdatedDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            dto.setRevokeDate(revokeDate.toString());
        }

        return dto;
    }

    private SubscriberObRequestDTO buildSubscriberObRequestDTO(GetSubscriberObDataDTO subscriberUID, Subscriber subscriber,
                                                               SubscriberOnboardingData data, SubscriberStatusModel status, CertificateDetailDto certificateDetailDto)
            throws JsonProcessingException {

        SubscriberObRequestDTO obRequestDTO = new SubscriberObRequestDTO();
        if (data != null) {
            ObjectMapper mapper = new ObjectMapper();
            SubscriberObData obData = mapper.readValue(data.getOnboardingDataFieldsJson(), SubscriberObData.class);

            obData.setSubscriberSelfie(resolveSelfie(subscriberUID.isSelfieRequired(), data));
            forceExpiryUpdateIfNIN(data, obData);

            obRequestDTO.setSubscriberData(obData);
            obRequestDTO.setSubscriberType(data.getSubscriberType());
            obRequestDTO.setConsentId(1);
            obRequestDTO.setSuID(data.getSubscriberUid());
            obRequestDTO.setOnboardingMethod(data.getOnboardingMethod());
            obRequestDTO.setLevelOfAssurance(data.getLevelOfAssurance());
            obRequestDTO.setTemplateId(data.getTemplateId());
            obRequestDTO.setOnboardingApprovalStatus(status != null ? status.getSubscriberStatus() : null);
            obRequestDTO.setCertificateDetailDto(certificateDetailDto);

            obRequestDTO.setMobileNo(subscriber.getMobileNumber());
            obRequestDTO.setEmailId(subscriber.getEmailId());
            obRequestDTO.setTitle(subscriber.getTitle());

            setPaymentAndCertStatus(obRequestDTO, subscriberUID.getSuid());
            setTotpResponseIfRequired(obRequestDTO, subscriber);
        }

        return obRequestDTO;
    }


    private void forceExpiryUpdateIfNIN(SubscriberOnboardingData data, SubscriberObData obData) {
        if ("NIN".equalsIgnoreCase(data.getOnboardingMethod())) {
            LocalDateTime expiry = LocalDateTime.now().plusYears(1).toLocalDate().atStartOfDay();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            obData.setDateOfExpiry(expiry.format(formatter));
        }
    }

    private void setPaymentAndCertStatus(SubscriberObRequestDTO obRequestDTO, String suid) {
        List<String> paymentStatus = subscriberRepoIface.subscriberPaymnetStatus(suid);
        List<String> statuses = subscriberRepoIface.subscriberPaymnetInitaiatedStatus(suid);


        String paymentIntiatiedStatus = statuses.isEmpty() ? null : statuses.get(0);


        if (!paymentStatus.isEmpty()) {
            obRequestDTO.setOnboardingPaymentStatus(paymentStatus.get(0));
        } else if (paymentIntiatiedStatus != null) {
            obRequestDTO.setOnboardingPaymentStatus(Constant.PAYMENT_STATUS_INITIATED);
        } else {
            obRequestDTO.setOnboardingPaymentStatus(Constant.PAYMENT_STATUS_PENDING);
        }


        obRequestDTO.setCertStatus(resolveCertificateStatus(obRequestDTO.getCertStatus()));
    }

    private String resolveCertificateStatus(String certStatus) {
        if (certStatus == null) {
            return Constant.PENDING;
        }

        String statusUpper = certStatus.toUpperCase();

        if (statusUpper.equals(Constant.FAIL) || statusUpper.equals(Constant.FAILED)) {
            return Constant.FAILED;
        }

        if (statusUpper.equals(Constant.CERT_REVOKED) || statusUpper.equals(Constant.REVOKED) ||
                statusUpper.equals(Constant.CERT_EXPIRED) || statusUpper.equals(Constant.EXPIRED)) {

            return statusUpper;
        }
        return certStatus;
    }

    private void setTotpResponseIfRequired(SubscriberObRequestDTO obRequestDTO, Subscriber subscriber)
            throws JsonProcessingException {

        if (!priAuthSchemeBoolean) return;

        TotpDto totpDto = new TotpDto();
        totpDto.setSuid(subscriber.getSubscriberUid());
        totpDto.setFullName(subscriber.getFullName());
        totpDto.setPriAuthScheme(priAuthScheme);

        ApiResponse totpApiResponse = getTotp(totpDto);

        String totpResp = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(totpApiResponse.getResult());

        TotpDtoResp totpDtoResp = objectMapper.readValue(totpResp, TotpDtoResp.class);

        if (totpDtoResp == null) {
            throw new TotpDataException(
                    "api.error.authentication.data.should.not.be.null.or.empty.subscriber.onBoarding.data.not.saved");
        }

        obRequestDTO.setTotpResp(totpDtoResp);
    }


    private void executeVersionComparator(Subscriber subscriber,
                                          HttpServletRequest httpServletRequest) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(20);
            Runnable visitorWorkerThread =
                    new VersionComparatorThread(subscriber, subscriberRepoIface, httpServletRequest);
            executor.execute(visitorWorkerThread);
            executor.shutdown();
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
        }
    }

    @Override
    public ApiResponse getVerificationChannelResponse(HttpServletRequest request, String subscriberUID) {
        try {
            logger.info(" getVerificationChannelResponse suid :: {}", subscriberUID);
            JsonNode root = null;
            SubscriberOnboardingData subscriberOnboardingData = new SubscriberOnboardingData();
            List<SubscriberOnboardingData> subscriberOnboardingDataList = onboardingDataRepoIface
                    .getBySubUid(subscriberUID);
            if (!subscriberOnboardingDataList.isEmpty()) {
                if (subscriberOnboardingDataList.size() > 1) {
                    subscriberOnboardingData = findLatestOnboardedSub(subscriberOnboardingDataList);
                } else {
                    subscriberOnboardingData = subscriberOnboardingDataList.get(0);
                }
            }

            if (subscriberOnboardingData != null) {
                root = objectMapper.readTree(subscriberOnboardingData.getNiraResponse());
                ObjectNode dataNode = (ObjectNode) root.path(CUSTOMER_DETAILS).path(RESULT).path("Data");
                // Set fields to explicit JSON null
                dataNode.putNull("ImmigrationFile");
                dataNode.putNull("TravelDetail");
                dataNode.putNull("Documents");
                return exceptionHandlerUtil.createSuccessResponse("api.response.verification.channel.response",
                        dataNode);
            } else {
                return exceptionHandlerUtil.createErrorResponse(NO_DATA_FOUND);
            }
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse resetPin(GetSubscriberObDataDTO subscriberObDataDTO) {
        logger.info(CLASS + " Reset Pin request {}", subscriberObDataDTO);

        String validationResult = ValidationUtil.validate(subscriberObDataDTO);
        if (validationResult != null) {
            logger.info(VALIDATION_ERROR, validationResult);
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage(validationResult, null);
        }

        try {

            List<SubscriberOnboardingData> onboardingDataList = onboardingDataRepoIface
                    .getBySubUid(subscriberObDataDTO.getSuid());

            SubscriberOnboardingData onboardingData = getLatestOnboardingData(onboardingDataList);
            if (onboardingData == null) {
                return exceptionHandlerUtil.createErrorResponse(NO_DATA_FOUND);
            }


            ResetPinDTO pinDTO = new ResetPinDTO();
            pinDTO.setIdDocNumber(onboardingData.getIdDocNumber());
            pinDTO.setSelfie(resolveSelfie(subscriberObDataDTO.isSelfieRequired(), onboardingData));

            return exceptionHandlerUtil.createSuccessResponse("api.response.reset.pin.data", pinDTO);

        } catch (Exception e) {
            logger.error(CLASS + " resetPin Exception {}", e.getMessage(), e);
            return exceptionHandlerUtil.handleException(e);
        }
    }


    private SubscriberOnboardingData getLatestOnboardingData(List<SubscriberOnboardingData> list) {
        if (list == null || list.isEmpty()) return null;
        return list.size() > 1 ? findLatestOnboardedSub(list) : list.get(0);
    }


    private String resolveSelfie(boolean isSelfieRequired, SubscriberOnboardingData onboardingData) {
        if (!isSelfieRequired) return null;

        if (isOnboardingFee) return onboardingData.getSelfie();

        ApiResponse response = getSubscriberSelfie(onboardingData.getSelfieUri());
        return response.isSuccess() ? (String) response.getResult() : null;
    }

    @Override
    public ApiResponse getSubscriberSelfie(String uri) {
        logger.info(CLASS + " getBase64String uri {}", uri);

        try {
            validateUrl(uri);

            HttpHeaders headersForGet = new HttpHeaders();
            HttpEntity<Object> requestEntityForGet = new HttpEntity<>(headersForGet);
            ResponseEntity<Resource> downloadUrlResult = restTemplate.exchange(uri, HttpMethod.GET, requestEntityForGet,
                    Resource.class);
            byte[] buffer = IOUtils.toByteArray(downloadUrlResult.getBody().getInputStream());
            String image2 = new String(Base64.getEncoder().encode(buffer));
            return exceptionHandlerUtil.createSuccessResponse("api.response.base64.of.image.fetched.successfully",
                    image2);
        } catch (Exception e) {
            logger.error(CLASS + "getBase64String Exception {}", e.getMessage());
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.handleException(e);

        }
    }

    @Override
    public ResponseEntity<Object> getVideoLiveStreaming(String subscriberUid) {
        logger.info(CLASS + "getVideoLiveStreaming subscriberUid {}", subscriberUid);
        try {

            if (subscriberUid == null || subscriberUid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_UID_CANNOT_NULL));
            }

            if (!(Objects.isNull(subscriberUid) || subscriberUid.isEmpty())) {
                String url = subscriberRepoIface.getSubscriberUid(subscriberUid);
                if (!Objects.isNull(url)) {
                    validateUrl(url);
                    HttpHeaders headersForGet = new HttpHeaders();
                    HttpEntity<Object> requestEntityForGet = new HttpEntity<>(headersForGet);
                    ResponseEntity<Resource> downloadUrlResult = restTemplate.exchange(url, HttpMethod.GET,
                            requestEntityForGet, Resource.class);

                    return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "video/mp4")
                            .body(downloadUrlResult.getBody());
                } else {
                    logger.info(CLASS + "getVideoLiveStreaming No video found {}", HttpStatus.NOT_FOUND);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(exceptionHandlerUtil.createErrorResponse("api.error.no.video.found"));

                }

            } else {
                logger.info(CLASS + "getVideoLiveStreaming Subscriber not found {}", HttpStatus.NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_NOT_FOUND));

            }
        } catch (Exception e) {
            logger.error(CLASS + "getVideoLiveStreaming Exception {}", e.getMessage());
            logger.error(ECXEPTION, e);

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(exceptionHandlerUtil.handleHttpException(e));

        }

    }

    public static SubscriberOnboardingData findLatestOnboardedSub(
            List<SubscriberOnboardingData> subscriberOnboardingData) {
        Date[] dates = new Date[subscriberOnboardingData.size()];

        int i = 0;
        SimpleDateFormat simpleDateFormat = null;
        for (SubscriberOnboardingData s : subscriberOnboardingData) {

            try {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = simpleDateFormat.parse(s.getCreatedDate());

                dates[i] = date;
                i++;
            } catch (Exception e) {
                logger.error(ECXEPTION, e);
            }
        }
        Date latestDate = getLatestDate(dates);
        String latestDateString = simpleDateFormat.format(latestDate);
        for (SubscriberOnboardingData s : subscriberOnboardingData) {
            if (s.getCreatedDate().equals(latestDateString)) {
                return s;
            }
        }
        return null;
    }

    public static Date getLatestDate(Date[] dates) {
        Date latestDate = null;
        if ((dates != null) && (dates.length > 0)) {
            for (Date date : dates) {
                if (date != null) {
                    if (latestDate == null) {
                        latestDate = date;
                    }
                    latestDate = date.after(latestDate) ? date : latestDate;
                }
            }
        }
        return latestDate;
    }


    @Override
    public ResponseEntity<Object> getVideoLiveStreamingLocalEdms(String subscriberUid) {
        try {

            if (subscriberUid == null || subscriberUid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_UID_CANNOT_NULL));
            }

            logger.info(CLASS + "getVideoLiveStreamingLocalEdms req subscriberUid {}", subscriberUid);
            if (!(Objects.isNull(subscriberUid) || subscriberUid.isEmpty())) {
                String url = livelinessRepository.getSubscriberUid(subscriberUid);
                if (!Objects.isNull(url)) {
                    validateUrl(url);
                    HttpHeaders headersForGet = new HttpHeaders();
                    HttpEntity<Object> requestEntityForGet = new HttpEntity<>(headersForGet);
                    ResponseEntity<Resource> downloadUrlResult = restTemplate.exchange(url, HttpMethod.GET,
                            requestEntityForGet, Resource.class);

                    return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "video/mp4")
                            .body(downloadUrlResult.getBody());
                } else {

                    logger.error(CLASS + "getVideoLiveStreamingLocalEdms No video found {}", HttpStatus.NOT_FOUND);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(exceptionHandlerUtil.createErrorResponse("api.error.no.video.found"));
                }
            } else {
                logger.error(CLASS + "getVideoLiveStreamingLocalEdms Subscriber not found {}", HttpStatus.NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_NOT_FOUND));

            }
        } catch (JDBCConnectionException | ConstraintViolationException | DataException | LockAcquisitionException
                 | PessimisticLockException | QueryTimeoutException | SQLGrammarException | GenericJDBCException ex) {
            ex.printStackTrace();
            logger.error(CLASS + "saveSubscriberData Exception {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    AppUtil.createApiResponse(false, SOMETHING_WENT_WRONG, null));
        } catch (Exception e) {
            logger.error(CLASS + "getVideoLiveStreamingLocalEdms Exception {}", e.getMessage());
            logger.error(ECXEPTION, e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    AppUtil.createApiResponse(false, SOMETHING_WENT_WRONG, null));
        }
    }

    @Override
    public ApiResponse addTrustedUsers(TrustedUserDto emails) {
        try {

            if (emails == null || emails.getEmails() == null || emails.getEmails().isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.trusted.user.email.list.is.empty");
            }

            List<String> emailsListDb = trustedUserRepoIface.getTrustedEmails();
            if (emailsListDb == null || emailsListDb.isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.trusted.user.email.list.is.empty");
            }


            List<String> incomingEmails = new ArrayList<>();
            for (TrustedEmails trustedEmail : emails.getEmails()) {
                incomingEmails.add(trustedEmail.getEmail());
            }


            List<String> duplicates = new ArrayList<>();
            for (String email : incomingEmails) {
                if (emailsListDb.contains(email)) {
                    duplicates.add(email);
                }
            }

            if (!duplicates.isEmpty()) {
                return exceptionHandlerUtil
                        .createErrorResponseWithResult("api.error.duplicate.emails.are.present", duplicates);
            }


            List<TrustedUser> saveTrustedUser = new ArrayList<>();
            for (TrustedEmails trustedEmail : emails.getEmails()) {
                saveTrustedUser.add(saveTrustedUsers(trustedEmail));
            }

            trustedUserRepoIface.saveAll(saveTrustedUser);

            return exceptionHandlerUtil.successResponse("api.response.list.save.successfully");

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    public TrustedUser saveTrustedUsers(TrustedEmails trustedEmails) {
        TrustedUser trustedUser = new TrustedUser();
        trustedUser.setEmailId(trustedEmails.getEmail());
        trustedUser.setFullName(trustedEmails.getName());
        trustedUser.setMobileNumber(trustedEmails.getMobileNo());
        trustedUser.setTrustedUserStatus(trustedUserStatus);
        return trustedUser;
    }

    @Override
    public ApiResponse getSubscriberDetailsReports(String startDate, String endDate) {
        try {
            logger.info(CLASS + "getSubscriberDetailsReport req startDate {} and endDate {}", startDate, endDate);
            if (startDate != null && endDate != null) {
                List<SubscriberCertificateDetails> completeDetail = subscriberCertificateDetailsRepoIface
                        .getSubscriberReports(startDate, endDate);
                List<SubscriberReportsResponseDto> details = new ArrayList<>();

                if (Objects.nonNull(completeDetail) && !completeDetail.isEmpty()) {

                    for (SubscriberCertificateDetails subscriberCompleteDetail : completeDetail) {
                        SubscriberReportsResponseDto reportsResponseDto = new SubscriberReportsResponseDto();
                        reportsResponseDto.setFullName(subscriberCompleteDetail.getFullName());
                        reportsResponseDto.setIdDocNumber(subscriberCompleteDetail.getIdDocNumber());
                        reportsResponseDto.setOnboardingMethod(subscriberCompleteDetail.getOnboardingMethod());
                        reportsResponseDto
                                .setCertificateSerialNumber(subscriberCompleteDetail.getCertificateSerialNumber());
                        reportsResponseDto
                                .setCertificateIssueDate(subscriberCompleteDetail.getCertificateIssueDate().toString());
                        reportsResponseDto
                                .setCerificateExpiryDate(subscriberCompleteDetail.getCerificateExpiryDate().toString());
                        details.add(reportsResponseDto);
                    }
                    logger.info(
                            CLASS + "getSubscriberDetailsReports Succssfully fetched subscriber certificate details {}",
                            details);

                    return exceptionHandlerUtil.createSuccessResponse(
                            "api.error.successfully.fetched.subscriber.certificate.details", details);
                } else {
                    logger.info(CLASS + " getSubscriberDetailsReports No Records Found");
                    return exceptionHandlerUtil.createErrorResponse("api.response.no.records.found");

                }
            } else {
                logger.info(CLASS + "getSubscriberDetailsReports Date cant should be null or empty");
                return exceptionHandlerUtil.createErrorResponse("api.error.date.cant.should.be.null.or.empty");

            }
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            logger.error(CLASS + " getSubscriberDetailsReports Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    int isOptionData1Present(String optionalData1) {
        return onboardingDataRepoIface.getOptionalData1(optionalData1);
    }

    @Override
    public ApiResponse updatePhoneNumber(UpdateDto updateDto) {
        try {
            logger.info(CLASS + " updatePhoneNumber Suid {}", updateDto.toString());
            if (updateDto.getSuid() == null || updateDto.getSuid().isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_UID_CANNOT_NULL);

            }
            if (updateDto.getMobileNumber() == null || updateDto.getMobileNumber().isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.mobile.number.cant.be.null.or.empty");
            }

            Date d1 = subscriberHistoryRepo.getLatestForMobile(updateDto.getSuid());
            logger.info(CLASS + " updatePhoneNumber latest date {} ", d1);
            if (d1 != null) {
                Date d2 = AppUtil.getCurrentDate();
                long differenceInTime = d2.getTime() - d1.getTime();
                long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInTime) % 365;
                logger.info("Difference in days: {}", differenceInDays);

                if (differenceInDays <= 30) {

                    return exceptionHandlerUtil.createErrorResponse(
                            "api.error.cant.change.the.phone.number.because.you.changed.it.recently");

                }
            }

            Subscriber sub = subscriberRepoIface.findBymobileNumber(updateDto.getMobileNumber());

            Subscriber subscriber = subscriberRepoIface.findBysubscriberUid(updateDto.getSuid());
            if (subscriber == null) {
                return AppUtil.createApiResponse(false, SUBSCRIBER_NOT_FOUND, null);
            }
            if (subscriber.getMobileNumber().equals(updateDto.getMobileNumber())) {

                return exceptionHandlerUtil
                        .createErrorResponse("api.error.your.old.number.and.entered.mobile.number.are.same");
            }
            if (sub != null) {

                return exceptionHandlerUtil.createErrorResponse("api.error.this.mobile.number.is.already.in.use");

            }
            SubscriberContactHistory subscriberContactHistory = new SubscriberContactHistory();
            subscriberContactHistory.setSubscriberUid(subscriber.getSubscriberUid());
            subscriberContactHistory.setMobileNumber(subscriber.getMobileNumber());

            subscriberContactHistory.setCreatedDate(AppUtil.getCurrentDate());
            subscriberHistoryRepo.save(subscriberContactHistory);


            subscriber.setMobileNumber(updateDto.getMobileNumber());
            subscriberRepoIface.save(subscriber);

            return exceptionHandlerUtil.createSuccessResponse("api.error.phone.number.updated", subscriber);

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            logger.error(CLASS + " updatePhoneNumber Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);

        }
    }

    @Override
    public ApiResponse updateEmail(UpdateDto updateDto) {
        try {
            logger.error(CLASS + " updatePhoneNumber Suid {}", updateDto.getSuid());
            if (updateDto.getSuid() == null || updateDto.getSuid().isEmpty()) {

                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_UID_CANNOT_NULL);

            }
            if (updateDto.getEmail() == null || updateDto.getEmail().isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.email.id.cant.be.empty");
            }
            Date d1 = subscriberHistoryRepo.getLatestForEmail(updateDto.getSuid());
            if (d1 != null) {
                Date d2 = AppUtil.getCurrentDate();
                long differenceInTime = d2.getTime() - d1.getTime();
                long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInTime) % 365;
                logger.info("Difference in days: {}", differenceInDays);
                if (differenceInDays <= 30) {

                    return exceptionHandlerUtil
                            .createErrorResponse("api.error.cant.change.the.email.because.you.changed.it.recently");
                }
            }

            Subscriber sub = subscriberRepoIface.findByemailId(updateDto.getEmail());

            Subscriber subscriber = subscriberRepoIface.findBysubscriberUid(updateDto.getSuid());
            if (subscriber == null) {

                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_NOT_FOUND);

            }
            if (subscriber.getEmailId().equals(updateDto.getEmail())) {

                return exceptionHandlerUtil
                        .createErrorResponse("api.error.your.old.email.and.entered.emailId.are.same");

            }
            if (sub != null) {

                return exceptionHandlerUtil.createErrorResponse("api.error.this.email.is.already.in.use");

            }

            int orgEmailCount = orgContactsEmailRepository.findByOrgEmailAndNotUgPassEmail(updateDto.getEmail(),
                    subscriber.getEmailId());
            int orgMobileCount = orgContactsEmailRepository.findByOrgEmailAndNotMobile(updateDto.getEmail(),
                    subscriber.getMobileNumber());
            int orgNinCount = orgContactsEmailRepository.findByOrgEmailAndNotNin(updateDto.getEmail(),
                    subscriber.getIdDocNumber());
            int orgPassportCount = orgContactsEmailRepository.findByOrgEmailAndNotPassport(updateDto.getEmail(),
                    subscriber.getIdDocNumber());
            if (orgEmailCount != 0 || orgPassportCount != 0 || orgMobileCount != 0 || orgNinCount != 0) {

                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.this.email.is.already.registered.with.another.organization.subscriber.email");

            }

            SubscriberContactHistory subscriberContactHistory = new SubscriberContactHistory();
            subscriberContactHistory.setSubscriberUid(subscriber.getSubscriberUid());

            subscriberContactHistory.setEmailId(subscriber.getEmailId());
            subscriberContactHistory.setCreatedDate(AppUtil.getCurrentDate());
            subscriberHistoryRepo.save(subscriberContactHistory);


            subscriber.setEmailId(updateDto.getEmail());
            subscriberRepoIface.save(subscriber);

            return exceptionHandlerUtil.createSuccessResponse("api.response.email.updated", subscriber);

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            logger.error(CLASS + " updatePhoneNumber Exception ", e.getMessage());
            return exceptionHandlerUtil.handleException(e);

        }
    }

    @Override
    public ApiResponse sendOtpEmail(UpdateOtpDto otpDto) {
        try {
            if (otpDto.getEmail() == null || otpDto.getEmail().isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.email.id.cant.be.empty");

            }
            OTPResponseDTO otpResponse = new OTPResponseDTO();

            if (otpDto.getEmail().equals(testAndroidEmail) || otpDto.getEmail().equals(testIosEmail)) {
                ApiResponse apiResponseDemo = verifyOtp(null, otpDto.getEmail());
                if (apiResponseDemo.isSuccess()) {

                    return exceptionHandlerUtil.createSuccessResponse(RESPONSE_OK, apiResponseDemo.getResult());

                }
            }

            String emailOTP = generateOtp(6);

            EmailReqDto dto = new EmailReqDto();
            dto.setEmailOtp(emailOTP);
            dto.setEmailId(otpDto.getEmail());
            dto.setTtl(timeToLive);

            ApiResponse res = sendEmailToSubscriber(dto);

            otpResponse.setMobileOTP(null);
            otpResponse.setEmailOTP(null);
            otpResponse.setTtl(timeToLive);
            otpResponse.setEmailEncrptyOTP(encryptedString(emailOTP));

            if (res.isSuccess()) {
                logger.info("email res >> {} ", res.getMessage());
                logger.info("Email Sent Successfully");
                return exceptionHandlerUtil.createSuccessResponse(RESPONSE_OK, otpResponse);
            } else {
                logger.info("IN Email Excption >> {}", res);
                return exceptionHandlerUtil
                        .createErrorResponse(SOMETHING_WENT_WRONG);
            }
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    public ApiResponse verifyOtp(String mobNo, String email) {
        ApiResponse apiResponse = new ApiResponse();

        OTPResponseDTO otpResponse = new OTPResponseDTO();

        if (email != null) {
            otpResponse.setEmailEncrptyOTP(AppUtil.encryptedString("12345"));
        } else {
            otpResponse.setMobileEncrptyOTP(AppUtil.encryptedString("123456"));
        }
        otpResponse.setTtl(180);
        apiResponse.setMessage("Otp verfication done");
        apiResponse.setSuccess(true);
        apiResponse.setResult(otpResponse);
        return apiResponse;

    }

    @Override
    public ApiResponse sendOtpMobile(UpdateOtpDto otpDto) {
        try {
            if (otpDto == null || !StringUtils.hasText(otpDto.getMobileNumber())) {
                return exceptionHandlerUtil.createErrorResponse("api.error.mobile.number.cant.be.empty");
            }

            String mobileNumber = otpDto.getMobileNumber();
            String mobileOTP = generateOtp(6);


            if (mobileNumber.equals(testIosOtp) || mobileNumber.equals(testAndroidOtp)) {
                ApiResponse demoResponse = verifyOtp(mobileNumber, null);
                if (demoResponse.isSuccess()) {
                    return exceptionHandlerUtil.createSuccessResponse(RESPONSE_OK, demoResponse.getResult());
                }
            }

            DeviceOtpHandler handler = getHandlerByCountryCode(mobileNumber);
            if (handler == null) {
                return exceptionHandlerUtil.createErrorResponse("api.error.invalid.country.code");
            }

            if (!handler.isValidNumber(mobileNumber)) {
                return exceptionHandlerUtil.createErrorResponse(INVALID_PHONE_NUMBER);
            }

            return handler.sendOtp(mobileNumber, mobileOTP);

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private DeviceOtpHandler getHandlerByCountryCode(String mobileNumber) {
        if (mobileNumber.startsWith("+91")) return new IndiaOtpHandler();
        if (mobileNumber.startsWith("+256")) return new UgandaOtpHandler();
        if (mobileNumber.startsWith("+971")) return new UaeOtpHandler();
        return null;
    }

    private interface DeviceOtpHandler {
        boolean isValidNumber(String mobileNumber);

        ApiResponse sendOtp(String mobileNumber, String otp);
    }

    private class IndiaOtpHandler implements DeviceOtpHandler {
        @Override
        public boolean isValidNumber(String mobileNumber) {
            return mobileNumber.length() == 13;
        }

        @Override
        public ApiResponse sendOtp(String mobileNumber, String otp) {
            String localNumber = mobileNumber.substring(3);
            ApiResponse response = sendSMSIND(otp, localNumber);
            if (!response.isSuccess()) {
                return AppUtil.createApiResponse(false, "Unable to perform action. Please try after sometime", null);
            }
            return createOtpResponse(otp);
        }
    }

    private class UgandaOtpHandler implements DeviceOtpHandler {
        @Override
        public boolean isValidNumber(String mobileNumber) {
            return mobileNumber.length() == 13;
        }

        @Override
        public ApiResponse sendOtp(String mobileNumber, String otp) {
            try {
                ApiResponse response = sendSMSUGA(otp, mobileNumber, timeToLive);
                SmsOtpResponseDTO smsOtpResponse = objectMapper.readValue(response.getResult().toString(),
                        SmsOtpResponseDTO.class);
                if (smsOtpResponse.getNonFieldErrors() != null) {
                    return exceptionHandlerUtil.createFailedResponseWithCustomMessage(
                            smsOtpResponse.getNonFieldErrors().get(0), null);
                }
                return createOtpResponse(otp);
            } catch (Exception e) {
                sentryClientExceptions.captureExceptions(e);
                logger.error(ECXEPTION, e);
                return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);
            }
        }
    }

    private class UaeOtpHandler implements DeviceOtpHandler {
        @Override
        public boolean isValidNumber(String mobileNumber) {
            return mobileNumber.length() == 13;
        }

        @Override
        public ApiResponse sendOtp(String mobileNumber, String otp) {
            try {
                Object obj = sendSMSUAE(otp, mobileNumber, timeToLive);
                LinkedHashMap<String, String> smsOtpResponse = objectMapper.convertValue(obj, LinkedHashMap.class);
                if ("406".equals(smsOtpResponse.get("code"))) {
                    return exceptionHandlerUtil.createErrorResponse("api.error.invalid.number");
                }
                return createOtpResponse(otp);
            } catch (Exception e) {
                sentryClientExceptions.captureExceptions(e);
                logger.error(ECXEPTION, e);
                return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);
            }
        }
    }

    private ApiResponse createOtpResponse(String otp) {
        OTPResponseDTO otpResponse = new OTPResponseDTO();
        otpResponse.setMobileOTP(null);
        otpResponse.setEmailOTP(null);
        otpResponse.setTtl(timeToLive);
        otpResponse.setMobileEncrptyOTP(encryptedString(otp));
        return exceptionHandlerUtil.createSuccessResponse(RESPONSE_OK, otpResponse);
    }

    public ApiResponse sendSMSIND(String otp, String mobileNumber) {
        logger.info(CLASS + "sendSMSIND req  otp {} and mobileNumber {}", otp, mobileNumber);
        String smsBody = "Dear Subscriber, " + otp + " is your DigitalTrust Mobile verification one-time code";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String smsUrlWithBody = indApiSMS
                + "?APIKey=E2X4Ixz65kKlawWUBVUKkA&senderid=DGTRST&channel=2&DCS=0&flashsms=0&number=" + mobileNumber
                + "&text=" + smsBody + "&route=1&dlttemplateid=1307162619898313468";

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        try {
            logger.info(CLASS + "sendSMSIND req for restTemplate smsUrlWithBody {} and requestEntity {}",
                    smsUrlWithBody, requestEntity);

            ResponseEntity<Object> res = restTemplate.exchange(smsUrlWithBody, HttpMethod.GET, requestEntity,
                    Object.class);
            String smsResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(res.getBody());
            LinkedHashMap<String, String> indiaSmsOtpResponse = objectMapper.readValue(smsResponse,
                    LinkedHashMap.class);
            if ("000".equals(indiaSmsOtpResponse.get("ErrorCode"))) {
                logger.info(CLASS + "sendSMSIND res for restTemplate {}", indiaSmsOtpResponse);
                return exceptionHandlerUtil
                        .createSuccessResponseWithCustomMessage(indiaSmsOtpResponse.get("ErrorMessage"), null);
            } else {
                return exceptionHandlerUtil
                        .createFailedResponseWithCustomMessage(indiaSmsOtpResponse.get("ErrorMessage"), null);
            }
        } catch (Exception e) {
            logger.error(CLASS + "sendSMSIND Exception {}", e.getMessage());
            logger.error(ECXEPTION, e);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleHttpException(e);
        }
    }

    public ApiResponse sendSMSUGA(String otp, String mobileNumber, int timeToLive) throws ParseException {
        logger.info(CLASS + "sendSMSUGA otp {} and mobileNumber {} and timeToLive {} ", otp, mobileNumber, timeToLive);
        String url = niraApiSMS;
        String basicAuth = getBasicAuth();
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setPhoneNumber(mobileNumber);
        smsDTO.setSmsText("Dear Customer, your OTP for UgPass Registration is " + otp
                + ", Please use this OTP to validate your Mobile number. This OTP is valid for " + timeToLive
                + " Seconds - UgPass System");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("daes-authorization", basicAuth);
        headers.set("access_token", getToken());
        HttpEntity<Object> requestEntity = new HttpEntity<>(smsDTO, headers);
        try {
            logger.info(CLASS + " sendSMSUGA req for restTemplate url {} and requestEntity {} ", url, requestEntity);
            ResponseEntity<ApiResponse> res = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    ApiResponse.class);
            ApiResponse api = res.getBody();
            logger.info("sendSMSUGA res for restTemplate {}", res);
            return api;
        } catch (Exception e) {
            logger.error(CLASS + "sendSMSUGA Exception {}", e.getMessage());
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.handleHttpException(e);
        }
    }

    public Object sendSMSUAE(String otp, String mobileNumber, int timeToLive) throws ParseException {
        logger.info("sendSMSUAE  otp {} and mobileNumber {} and timeToLive {}", otp, mobileNumber, timeToLive);
        String url = uaeApiSMS;

        String text = "Your OTP for UAEID Registration is " + otp +
                ". Please use this OTP to validate your Phone Number. " +
                "This OTP is valid for 180 Seconds. - UAEID System";
        Map<String, String> uaeSmsBody = new HashMap<>();
        uaeSmsBody.put("mobileno", mobileNumber);
        uaeSmsBody.put("smstext", text);


        HttpEntity<Object> requestEntity = new HttpEntity<>(uaeSmsBody);
        try {
            logger.info(CLASS + "sendSMSUAE req for restTemplate url {} and requestEntity {} ", url, requestEntity);
            ResponseEntity<Object> res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Object.class);
            ApiResponse api = new ApiResponse();
            api.setSuccess(true);
            api.setMessage("");
            api.setResult(res.getBody());
            logger.info(CLASS + "sendSMSUAE res for restTemplate {}", res);
            return api.getResult();
        } catch (Exception e) {
            logger.error("sendSMSUAE Exception {}", e.getMessage());
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.handleHttpException(e);


        }
    }

    public String getBasicAuth() {
        String userCredentials = niraUserName + ":" + niraPassword;
        return new String(Base64.getEncoder().encode(userCredentials.getBytes()));
    }

    public String getToken() {
        String url = niraApiToken;
        logger.info(CLASS + "getToken req url {}", url);
        String basicAuth = getBasicAuth();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("daes-authorization", basicAuth);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        try {
            logger.info(CLASS + "getToken req for restTemplate {}", requestEntity);
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            logger.info(CLASS + "getToken res for restTemplate {}", res);
            return res.getBody();
        } catch (Exception e) {
            logger.error(CLASS + "getToken Exception {}", e.getMessage());
            logger.error(ECXEPTION, e);
            return e.getMessage();
        }

    }


    public String generateOtp(int maxLength) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            StringBuilder otp = new StringBuilder(maxLength);

            for (int i = 0; i < maxLength; i++) {
                otp.append(secureRandom.nextInt(9));
            }
            return otp.toString();
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            return null;
        }
    }

    public ApiResponse sendEmailToSubscriber(EmailReqDto emailReqDto) {
        try {
            sentryClientExceptions.captureTags(null, emailReqDto.getEmailId(), "sendEmailToSubscriber",
                    CLASS);
            String url = emailBaseUrl;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> requestEntity = new HttpEntity<>(emailReqDto, headers);
            logger.info("requestEntity >> {}", requestEntity);
            ResponseEntity<ApiResponse> res = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    ApiResponse.class);
            logger.info("res >> " + res);
            if (res.getStatusCodeValue() == 200) {
                logger.info(" sendEmailToSubscriber res.getBody().getMessage()  {}", res.getBody().getMessage());
                return exceptionHandlerUtil.createSuccessResponse("api.response.sent", res);
            } else if (res.getStatusCodeValue() == 400) {

                return exceptionHandlerUtil.createSuccessResponse(BAD_REQUEST, res);

            } else if (res.getStatusCodeValue() == 500) {

                return exceptionHandlerUtil.createSuccessResponse("api.error.internal.server.error", res);

            }
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage(res.getBody().getMessage(), null);
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleHttpException(e);

        }

    }

    @SuppressWarnings("squid:MethodCyclomaticComplexity")
    @Override
    public ApiResponse reOnboardAddSubscriberObData(SubscriberObRequestDTO obRequestDTO) {
        try {
            // Validate input
            ApiResponse validationResponse = validateRequest(obRequestDTO);
            if (validationResponse != null) return validationResponse;

            // Fetch subscriber data
            Subscriber subscriberData = subscriberRepoIface.findBysubscriberUid(obRequestDTO.getSuID());
            if (subscriberData == null) {
                return AppUtil.createApiResponse(false, SUBSCRIBER_NOT_FOUND, null);
            }

            SubscriberOnboardingData latestOnboarding = onboardingDataRepoIface
                    .findLatestSubscriber(subscriberData.getSubscriberUid()).stream().findFirst().orElse(null);

            long differenceInDays = AppUtil.getDifferenceBetDates(latestOnboarding.getCreatedDate());

            ApiResponse genderDobResponse = validateGenderAndDob(obRequestDTO, latestOnboarding, subscriberData);
            if (genderDobResponse != null) return genderDobResponse;

            SubscriberObData subscriberObData = obRequestDTO.getSubscriberData();

            ApiResponse docResponse = validateDocumentNumber(obRequestDTO, subscriberObData);
            if (docResponse != null) return docResponse;

            return processExpiryAndLOA(obRequestDTO, latestOnboarding, differenceInDays);

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            sentryClientExceptions.captureTags(obRequestDTO.getSuID(), null, "reOnboardAddSubscriberObData",
                    SUBSCRIBER_CONTROLLER);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private ApiResponse validateRequest(SubscriberObRequestDTO obRequestDTO) {
        if (Objects.isNull(obRequestDTO)) {
            return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.ob.request.cant.be.null.or.empty");
        }

        String validationMessage = ValidationUtil.validate(obRequestDTO);
        if (validationMessage != null) {
            logger.info(VALIDATION_ERROR, validationMessage);
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage(validationMessage, null);
        }

        String subscriberOBDataValidation = ValidationUtil.validate(obRequestDTO.getSubscriberData());
        if (subscriberOBDataValidation != null) {
            logger.info(VALIDATION_ERROR, subscriberOBDataValidation);
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage(subscriberOBDataValidation, null);
        }

        return null;
    }

    private ApiResponse validateGenderAndDob(SubscriberObRequestDTO obRequestDTO, SubscriberOnboardingData latest,
                                             Subscriber subscriberData) throws ParseException {
        SubscriberObData subscriberObData = obRequestDTO.getSubscriberData();
        if (checkGender) {
            String gender1 = normalizeGender(subscriberObData.getGender());
            String gender2 = normalizeGender(latest.getGender());
            if (!gender1.equals(gender2)) {
                return exceptionHandlerUtil.createErrorResponse("api.error.gender.must.be.same");
            }
        }

        String dob = AppUtil.removeTimeStamp(subscriberData.getDateOfBirth());
        String reOnboardDOB = AppUtil.removeTimeStamp(subscriberObData.getDateOfBirth());
        if (checkDateOfBirth && !Objects.equals(reOnboardDOB, dob)) {
            return exceptionHandlerUtil.createErrorResponse("api.error.date.of.birth.must.be.same");
        }

        return null;
    }

    private ApiResponse validateDocumentNumber(SubscriberObRequestDTO obRequestDTO, SubscriberObData subscriberObData
    ) {
        if (subscriberObData.getDocumentNumber() == null) {
            return exceptionHandlerUtil.createErrorResponse("api.error.id.document.number.cant.be.null");
        }

        Subscriber subscriber2 = subscriberRepoIface.findbyDocumentNumber(subscriberObData.getDocumentNumber());
        if (subscriber2 != null && !Objects.equals(subscriber2.getSubscriberUid(), obRequestDTO.getSuID())) {
            return exceptionHandlerUtil.createErrorResponse("api.error.this.document.is.already.onboarded");
        }

        if (checkDocumentNumber) {
            SubscriberView subscriberCertRevoked = subscriberViewRepoIface
                    .findSubscriberByDocIdCertRevoked(subscriberObData.getDocumentNumber());
            if (subscriberCertRevoked != null && Objects.equals(subscriberObData.getDocumentNumber(),
                    subscriberCertRevoked.getIdDocNumber())) {
                return exceptionHandlerUtil.createErrorResponse("api.error.id.document.number.must.be.different");
            }
        }

        return null;
    }

    private ApiResponse processExpiryAndLOA(SubscriberObRequestDTO obRequestDTO, SubscriberOnboardingData latest,
                                            long differenceInDays) throws Exception {

        String oldExpireDate = latest.getDateOfExpiry();
        SubscriberObData subscriberObData = obRequestDTO.getSubscriberData();
        String latestDate = AppUtil.getDate().toString();

        if (oldExpireDate.compareTo(latestDate) < 0) {
            return handleExpiredDoc(obRequestDTO, latest, subscriberObData);
        } else {
            return handleNonExpiredDoc(obRequestDTO, latest, subscriberObData, differenceInDays);
        }

    }

    private ApiResponse handleExpiredDoc(SubscriberObRequestDTO obRequestDTO,
                                         SubscriberOnboardingData latest,
                                         SubscriberObData subscriberObData) throws Exception {

        LocalDateTime newExpiryDate = AppUtil.getLocalDateTime(subscriberObData.getDateOfExpiry().toString());
        LocalDateTime currentDateTime = AppUtil.getLocalDateTime(AppUtil.getDate().toString());

        if (!"NIN".equalsIgnoreCase(obRequestDTO.getOnboardingMethod())) {
            if (newExpiryDate.isAfter(currentDateTime)) {
                long daysBetween = Duration.between(currentDateTime, newExpiryDate).toDays();
                logger.info("Days: {}", daysBetween);
                if (daysBetween <= 1) {
                    return exceptionHandlerUtil.createErrorResponse(
                            "api.error.you.cant.do.reonboard.because.your.document.date.of.expiry.is.less.then.days");
                }
            } else {
                String message = newExpiryDate.isBefore(currentDateTime)
                        ? "api.error.the.expiry.date.with.time.is.before.the.current.date.with.time"
                        : "api.error.the.expiry.date.with.time.is.the.same.as.the.current.date.with.time";
                return exceptionHandlerUtil.createErrorResponse(message);
            }
        }

        String loa = latest.getLevelOfAssurance();
        String method = obRequestDTO.getOnboardingMethod();
        if ((loa.equals(Constant.LOA2) && Constant.UNID.equalsIgnoreCase(method)) ||
                (loa.equals(Constant.LOA3) && (Constant.UNID.equalsIgnoreCase(method) || Constant.PASSPORT.equalsIgnoreCase(method)))) {
            return exceptionHandlerUtil.createErrorResponse(LOW_LEVEL_ASSURENCE);
        }

        return addSubscriberObData(obRequestDTO);
    }

    private ApiResponse handleNonExpiredDoc(SubscriberObRequestDTO obRequestDTO,
                                            SubscriberOnboardingData latest,
                                            SubscriberObData subscriberObData,
                                            long differenceInDays) throws Exception {

        ApiResponse recentUpdateResponse = checkRecentUpdate(subscriberObData, latest, differenceInDays);
        if (recentUpdateResponse != null) return recentUpdateResponse;

        ApiResponse expiryResponse = validateDocumentExpiry(obRequestDTO.getOnboardingMethod(), subscriberObData);
        if (expiryResponse != null) return expiryResponse;

        ApiResponse revokedResponse = checkRevokedCertificate(subscriberObData);
        if (revokedResponse != null) return revokedResponse;

        ApiResponse loaResponse = validateLOA(obRequestDTO.getOnboardingMethod(), latest.getLevelOfAssurance());
        if (loaResponse != null) return loaResponse;

        return addSubscriberObData(obRequestDTO);
    }

    private ApiResponse checkRecentUpdate(SubscriberObData obData,
                                          SubscriberOnboardingData latest,
                                          long differenceInDays) {
        if (differenceInDays < expiryDays &&
                !obData.getDocumentNumber().equals(latest.getIdDocNumber())) {
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage(
                    "We can't processed. it's seem your last updation of your id document is less than "
                            + expiryDays + " days.",
                    null);
        }
        return null;
    }

    private ApiResponse validateDocumentExpiry(String method, SubscriberObData obData) {
        if ("NIN".equalsIgnoreCase(method)) return null;

        LocalDateTime newExpiryDate = AppUtil.getLocalDateTime(obData.getDateOfExpiry().toString());
        LocalDateTime currentDateTime = AppUtil.getLocalDateTime(AppUtil.getDate().toString());

        if (newExpiryDate.isAfter(currentDateTime)) {
            long daysBetween = Duration.between(currentDateTime, newExpiryDate).toDays();
            logger.info("Days: {}", daysBetween);
            if (daysBetween <= 1) {
                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.you.cant.do.reonboard.because.your.document.date.of.expiry.is.less.then.days");
            }
        } else {
            String message = newExpiryDate.isBefore(currentDateTime)
                    ? "api.error.the.expiry.date.with.time.is.before.the.current.date.with.time"
                    : "api.error.the.expiry.date.with.time.is.the.same.as.the.current.date.with.time";
            return exceptionHandlerUtil.createErrorResponse(message);
        }
        return null;
    }

    private ApiResponse checkRevokedCertificate(SubscriberObData obData) {
        if (!checkDocumentNumber) return null;

        SubscriberView revoked = subscriberViewRepoIface
                .findSubscriberByDocIdCertRevoked(obData.getDocumentNumber());

        if (revoked != null &&
                Objects.equals(obData.getDocumentNumber(), revoked.getIdDocNumber())) {
            return exceptionHandlerUtil.createErrorResponse(
                    "api.error.id.document.number.must.be.different");
        }
        return null;
    }

    private ApiResponse validateLOA(String method, String loa) {
        if ((loa.equals(Constant.LOA2) && Constant.UNID.equalsIgnoreCase(method)) ||
                (loa.equals(Constant.LOA3) &&
                        (Constant.UNID.equalsIgnoreCase(method) || Constant.PASSPORT.equalsIgnoreCase(method)))) {
            return exceptionHandlerUtil.createErrorResponse(LOW_LEVEL_ASSURENCE);
        }
        return null;
    }

    private String normalizeGender(String gender) {
        if (gender == null)
            return "";
        gender = gender.trim().toLowerCase();
        if (gender.equals("m") || gender.equals("male")) {
            return "male";
        } else if (gender.equals("f") || gender.equals("female")) {
            return "female";
        }
        return gender;
    }

    @Override
    public ApiResponse deleteRecord(String mobileNo, String email) {
        try {
            if (!mobileNo.equals("")) {
                Optional<Subscriber> subscriber = Optional
                        .ofNullable(subscriberRepoIface.findBymobileNumber("+" + mobileNo));
                if (subscriber.isPresent()) {
                    String suid = subscriber.get().getSubscriberUid();

                    subscriberDeletionRepository.deleteSubscriberRecord(suid);
                    int a = 1;
                    if (a == 1) {
                        return exceptionHandlerUtil
                                .successResponse(SUBSCRIBER_DETAILS_DELETED);
                    } else {
                        return exceptionHandlerUtil
                                .createErrorResponse("api.error.subscriber.record.not.deleted.successfully");
                    }
                }
                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_NOT_FOUND);
            } else {
                Optional<Subscriber> subscriber = Optional.ofNullable(subscriberRepoIface.findByemailId(email));
                if (subscriber.isPresent()) {
                    String suid = subscriber.get().getSubscriberUid();
                    subscriberDeletionRepository.deleteSubscriberRecord(suid);
                    int a = 1;
                    if (a == 1) {
                        return exceptionHandlerUtil
                                .successResponse(SUBSCRIBER_DETAILS_DELETED);
                    } else {
                        return exceptionHandlerUtil
                                .createErrorResponse("api.error.subscriber.record.not.deleted.successfully");
                    }
                }
                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.handleException(e);

        }
    }

    @Override
    public ApiResponse getDeviceStatus(HttpServletRequest httpServletRequest) {
        try {
            String deviceId = httpServletRequest.getHeader(DEVICE_ID);
            if (deviceId == null || deviceId.isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.deviceid.not.coming.please.send.deviceid");
            }

            SubscriberDevice latestDevice = getLatestDevice(deviceId);
            DeviceStatusDto deviceStatusDto = new DeviceStatusDto();

            if (latestDevice != null) {
                populateDeviceDto(deviceStatusDto, latestDevice);
                if (Constant.DEVICE_STATUS_DISABLED.equals(latestDevice.getDeviceStatus())) {
                    deviceStatusDto.setConsentRequired(false);
                    return exceptionHandlerUtil.createSuccessResponse(
                            "api.response.device.status.is.disabled", deviceStatusDto);
                }
                deviceStatusDto.setConsentRequired(isConsentRequired(latestDevice));
                return exceptionHandlerUtil.createSuccessResponse("api.response.device.status", deviceStatusDto);
            }

            // Handle device history if no current device
            populateFromDeviceHistory(deviceStatusDto, deviceId);
            return exceptionHandlerUtil.createSuccessResponse("api.response.device.status", deviceStatusDto);

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private SubscriberDevice getLatestDevice(String deviceId) {
        List<SubscriberDevice> devices = deviceRepoIface.findBydeviceUid(deviceId);
        return getLatest(devices);
    }

    private void populateDeviceDto(DeviceStatusDto dto, SubscriberDevice device) {
        SubscriberFcmToken fcmToken = fcmTokenRepoIface.findBysubscriberUid(device.getSubscriberUid());
        dto.setFcmToken(fcmToken != null ? fcmToken.getFcmToken() : null);
        dto.setDeviceStatus(device.getDeviceStatus());
    }

    private boolean isConsentRequired(SubscriberDevice device) {
        if (!signRequired) return checkLatestConsent(device);

        SubscriberStatusModel status = statusRepoIface.findBysubscriberUid(device.getSubscriberUid());
        if (!ACTIVE.equals(status.getSubscriberStatus())) return false;

        return checkLatestConsent(device);
    }

    private boolean checkLatestConsent(SubscriberDevice device) {
        List<ConsentHistory> latestList = consentHistoryRepo.findLatestConsent();
        ConsentHistory latest = latestList.isEmpty() ? null : latestList.get(0);
        if (latest == null) return false;

        SubscriberConsents consent = subscriberConsentsRepo
                .findSubscriberConsentBySuidAndConsentId(device.getSubscriberUid(), latest.getId());
        return consent == null;
    }

    private void populateFromDeviceHistory(DeviceStatusDto dto, String deviceId) {
        List<SubscriberDeviceHistory> history = subscriberDeviceHistoryRepoIface.findBydeviceUid(deviceId);
        SubscriberDeviceHistory latest = history.isEmpty() ? null : history.get(0);

        if (latest != null) {
            dto.setDeviceStatus(latest.getDeviceStatus());
        } else {
            dto.setDeviceStatus(Constant.NEW_DEVICE);
        }
        dto.setConsentRequired(false);
    }

    public SubscriberDevice getLatest(List<SubscriberDevice> list) {


        return list.stream()
                .sorted(Comparator.comparing(sd -> parseDate(sd.getUpdatedDate()), Comparator.reverseOrder()))
                .findFirst().orElse(null);
    }

    private LocalDateTime parseDate(String date) {
        if (date.contains("T")) {
            return LocalDateTime.parse(date);
        } else {
            return LocalDateTime.parse(date.replace(" ", "T"));
        }
    }

    @Override
    public ApiResponse getSubscriberDetailsBySerachType(String searchType, String searchValue) {
        try {
            logger.info(CLASS + GET_SUBSCRIBER_DETAILS_BY_SEARCH, searchType,
                    searchValue);

            if (searchType == null || searchType.isEmpty() || searchValue == null || searchValue.isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse(BAD_REQUEST);
            }

            Subscriber subscriber = null;
            SubscriberDeviceUpdateDto subscriberDeviceUpdateDto = new SubscriberDeviceUpdateDto();
            switch (searchType) {
                case EMAIL:
                    subscriber = subscriberRepoIface.findByemailId(searchValue);
                    break;
                case MOBILE_NUMBER:
                    subscriber = subscriberRepoIface.findBymobileNumber(searchValue);
                    break;
                default:
                    return exceptionHandlerUtil.createErrorResponse(BAD_REQUEST);
            }
            if (subscriber == null) {
                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_DETAILS_NOT_FOUND);
            } else {

                SubscriberStatusModel subscriberStatus = statusRepoIface.findBysubscriberUid(subscriber.getSubscriberUid());

                SubscriberDevice subscriberDevice = deviceRepoIface.getSubscriber(subscriber.getSubscriberUid());
                subscriberDeviceUpdateDto.setSubscriberUid(subscriber.getSubscriberUid());
                subscriberDeviceUpdateDto.setFullName(subscriber.getFullName());
                subscriberDeviceUpdateDto.setDateOfBirth(subscriber.getDateOfBirth());
                subscriberDeviceUpdateDto.setIdDocType(subscriber.getIdDocType());
                subscriberDeviceUpdateDto.setIdDocNumber(subscriber.getIdDocNumber());
                subscriberDeviceUpdateDto.seteMail(subscriber.getEmailId());
                subscriberDeviceUpdateDto.setMobileNumber(subscriber.getMobileNumber());
                subscriberDeviceUpdateDto.setOsName(subscriber.getOsName());
                subscriberDeviceUpdateDto.setAppVersion(subscriber.getAppVersion());
                subscriberDeviceUpdateDto.setOsVersion(subscriber.getOsVersion());
                subscriberDeviceUpdateDto.setDeviceInfo(subscriber.getDeviceInfo());

                subscriberDeviceUpdateDto.setCreatedDate(subscriber.getCreatedDate());
                subscriberDeviceUpdateDto.setUpdatedDate(subscriber.getUpdatedDate());

                subscriberDeviceUpdateDto.setSubscriberStatus(subscriberStatus.getSubscriberStatus());

                subscriberDeviceUpdateDto.setDeviceUid(subscriberDevice.getDeviceUid());
                subscriberDeviceUpdateDto.setDeviceStatus(subscriberDevice.getDeviceStatus());


                return exceptionHandlerUtil.createSuccessResponse(SUBSCRIBER_DETAILS,
                        subscriberDeviceUpdateDto);
            }

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            logger.error(CLASS + " getSubscriberDetailsBySerachType Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse updateSusbcriberDeviceStatus(String suid) {
        try {
            logger.info(CLASS + "updateSusbcriberDeviceStatus request suid {}", suid);
            if (suid == null || suid.trim().equals("")) {
                return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.unique.id.cant.be.null");
            } else {
                SubscriberDevice subscriberDevice = (SubscriberDevice) deviceRepoIface.getSubscriberDeviceStatus(suid);
                if (subscriberDevice == null) {
                    return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_DETAILS_NOT_FOUND);
                } else {
                    subscriberDevice.setDeviceStatus(Constant.DEVICE_STATUS_DISABLED);
                    subscriberDevice.setUpdatedDate(AppUtil.getDate());
                    deviceRepoIface.save(subscriberDevice);
                    return exceptionHandlerUtil.successResponse("api.response.subscriber.device.status.updated");
                }
            }

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            logger.error(CLASS + " updateSusbcriberDeviceStatus Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse getSubscriberListBySerachType(String searchType, String searchValue) {
        try {
            logger.info(CLASS + GET_SUBSCRIBER_DETAILS_BY_SEARCH, searchType,
                    searchValue);
            if (searchType == null || searchType.isEmpty() || searchValue == null || searchValue.isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse(BAD_REQUEST);
            }

            List<String> subscriberList = null;
            switch (searchType) {
                case EMAIL:
                    subscriberList = subscriberRepoIface.getSubscriberListByEmailId(searchValue);
                    break;
                case MOBILE_NUMBER:
                    subscriberList = subscriberRepoIface.getSubscriberListByMobileNo(searchValue);
                    break;
                default:
                    return exceptionHandlerUtil.createErrorResponse(BAD_REQUEST);
            }
            if (subscriberList == null) {

                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_DETAILS_NOT_FOUND);
            } else {

                String jsonToString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(subscriberList);
                return AppUtil.createApiResponse(true, SUBSCRIBER_DETAILS, jsonToString);
            }

        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            logger.error(CLASS + " getSubscriberDetailsBySerachType Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse updateFcmTokenDetails(String suid, String fcmToken) {
        try {
            logger.info("{}{} - Received request to update FCM token for suid: {} with fcmToken: {}", CLASS,
                    Utility.getMethodName(), suid, fcmToken);
            Date startTime = new Date();
            if (suid != null && !suid.trim().isEmpty()) {
                if (fcmToken != null && !fcmToken.trim().isEmpty()) {
                    SubscriberFcmToken subscriberFcmToken = fcmTokenRepoIface.findBysubscriberUid(suid);
                    if (subscriberFcmToken != null) {
                        String message = "OLD FCMTOKEN | " + subscriberFcmToken.getFcmToken() + " NEW FCMTOKEN |"
                                + fcmToken;
                        logger.info("{}{} - message {}", CLASS, Utility.getMethodName(), message);
                        subscriberFcmToken.setFcmToken(fcmToken);
                        subscriberFcmToken.setCreatedDate(AppUtil.getDate());
                        fcmTokenRepoIface.save(subscriberFcmToken);
                        Date endTime = new Date();
                        logModelServiceImpl.setLogModelFCMToken(true, suid, null, "OTHER", null, message, startTime,
                                endTime, null);
                        return exceptionHandlerUtil.createSuccessResponse("api.response.fcmtoken.updated.successfully",
                                subscriberFcmToken);
                    } else {
                        return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_NOT_FOUND);
                    }
                } else {
                    return exceptionHandlerUtil.createErrorResponse("api.error.fcmtoken.cant.be.null.or.empty");
                }
            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.suid.cantbe.null.or.empty");
            }
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            logger.error(CLASS + " updateFcmTokenDetails Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse getSubDetailsBySerachType(HttpServletRequest httpServletRequest, String searchType,
                                                 String searchValue) {
        try {
            logger.info(CLASS + GET_SUBSCRIBER_DETAILS_BY_SEARCH, searchType,
                    searchValue);
            if (searchType == null || searchType.isEmpty() || searchValue == null || searchValue.isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse(BAD_REQUEST);
            }
            MobileOTPDto mobileOTPDto = new MobileOTPDto();
            DeviceInfo deviceInfo = new DeviceInfo();
            Subscriber subscriber = null;
            switch (searchType) {
                case EMAIL:
                    subscriber = subscriberRepoIface.findByemailId(searchValue);
                    break;
                case MOBILE_NUMBER:
                    subscriber = subscriberRepoIface.findBymobileNumber(searchValue);
                    break;
                case "idDocNumber":
                    subscriber = subscriberRepoIface.findByIdDocNumber(searchValue);
                    break;
                case "nationalId":
                    subscriber = subscriberRepoIface.findByNationalId(searchValue);
                    break;
                default:
                    return exceptionHandlerUtil.createErrorResponse(BAD_REQUEST);
            }

            if (subscriber == null) {
                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_DETAILS_NOT_FOUND);
            } else {

                if (subscriber.getMobileNumber().equalsIgnoreCase(testAndroidOtp)
                        && subscriber.getEmailId().equalsIgnoreCase(testAndroidEmail)) {
                    SubscriberDevice subscriberDevice = (SubscriberDevice) deviceRepoIface
                            .findBysubscriberUid(subscriber.getSubscriberUid());
                    subscriberDevice.setDeviceUid(httpServletRequest.getHeader(DEVICE_ID));
                    deviceRepoIface.save(subscriberDevice);
                }

                if (subscriber.getMobileNumber().equalsIgnoreCase(testIosOtp)
                        && subscriber.getEmailId().equalsIgnoreCase(testIosEmail)) {
                    SubscriberDevice subscriberDevice = (SubscriberDevice) deviceRepoIface
                            .findBysubscriberUid(subscriber.getSubscriberUid());

                    subscriberDevice.setDeviceUid(httpServletRequest.getHeader(DEVICE_ID));
                    deviceRepoIface.save(subscriberDevice);
                }

                deviceInfo.setDeviceId(httpServletRequest.getHeader(DEVICE_ID));
                deviceInfo.setAppVersion(httpServletRequest.getHeader("appVersion"));
                deviceInfo.setOsVersion(httpServletRequest.getHeader("osVersion"));

                mobileOTPDto.setSubscriberEmail(subscriber.getEmailId());
                mobileOTPDto.setSubscriberMobileNumber(subscriber.getMobileNumber());
                ApiResponse apiResponse = deviceUpdateIface.validateSubscriberAndDevice(deviceInfo, mobileOTPDto);
                if (apiResponse.isSuccess()) {
                    return exceptionHandlerUtil.createSuccessResponse(SUBSCRIBER_DETAILS,
                            apiResponse.getResult());
                } else {
                    return apiResponse;
                }
            }
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            logger.error(CLASS + " getSubDetailsBySerachType Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse getSusbcriberDeviceHistory(String suid) {
        try {
            logger.info("{}{} - Reuest for SusbcriberDeviceHistory suid {}", CLASS, Utility.getMethodName(), suid);
            if (!StringUtils.hasText(suid)) {
                return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.id.can.be.null.or.empty");
            }

            Subscriber subscriber = subscriberRepoIface.findBysubscriberUid(suid);
            if (subscriber == null) {
                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_DETAILS_NOT_FOUND);
            } else {
                SubscriberDeviceHistoryDetails subscriberDeviceHistoryDetails = new SubscriberDeviceHistoryDetails();


                List<SubscriberDevice> devices = deviceRepoIface.findBysubscriberUid(suid);
                SubscriberDevice subscriberDevice = null;
                if (!devices.isEmpty()) {
                    subscriberDevice = devices.get(0);
                }

                List<SubscriberDeviceHistory> subscriberDeviceHistory = subscriberDeviceHistoryRepoIface
                        .findSubscriberDeviceHistory(suid);

                List<HashMap<String, String>> listOfMaps = subscriberDeviceHistory.stream().map(s -> {
                    HashMap<String, String> strMap = new HashMap<>();
                    strMap.put("device_uid", s.getDeviceUid());
                    strMap.put("created_date", s.getCreatedDate());
                    return strMap;
                }).collect(Collectors.toList());

                subscriberDeviceHistoryDetails.setSubscriber(subscriber);
                subscriberDeviceHistoryDetails.setSubscriberDevice(subscriberDevice);
                subscriberDeviceHistoryDetails.setSubscriberDeviceHistory(listOfMaps);
                return exceptionHandlerUtil.createSuccessResponse(SUBSCRIBER_DETAILS,
                        subscriberDeviceHistoryDetails);
            }
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            logger.error(CLASS + " getSusbcriberDeviceHistory Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse getTotp(TotpDto totpDto) {
        ResponseEntity<ApiResponse> res = null;
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TotpDto> requestEntity = new HttpEntity<>(totpDto, headers);
            res = restTemplate.exchange(dtportal, HttpMethod.POST, requestEntity, ApiResponse.class);

            if (res.getStatusCodeValue() == 400 || res.getStatusCodeValue() == 401 || res.getStatusCodeValue() == 403
                    || res.getStatusCodeValue() == 404 || res.getStatusCodeValue() == 415
                    || res.getStatusCodeValue() == 500 || res.getStatusCodeValue() == 501
                    || res.getStatusCodeValue() == 503) {
                return exceptionHandlerUtil
                        .createErrorResponse(SOMETHING_WENT_WRONG);

            } else if (res.getStatusCodeValue() == 200 || res.getStatusCodeValue() == 201) {

                return exceptionHandlerUtil.createSuccessResponseWithCustomMessage("", res.getBody().getResult());
            }
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.handleHttpException(e);
        }
        return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);

    }

    @Override
    public ApiResponse getFCMToken(String subscriberUid) {
        try {
            if (!StringUtils.hasText(subscriberUid)) {
                return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.id.can.be.null.or.empty");
            } else {
                SubscriberFcmToken subscriberFcmToken = fcmTokenRepoIface.findBysubscriberUid(subscriberUid);
                if (subscriberFcmToken != null) {
                    return exceptionHandlerUtil.createSuccessResponse(
                            "api.response.subscriber.fcm.token.found.successfully", subscriberFcmToken);
                } else {
                    return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.fcm.token.not.found");
                }
            }
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse deleteRecordBySuid(String subscriberUid) {
        try {
            Subscriber subscriber = subscriberRepoIface.findBysubscriberUid(subscriberUid);
            if (subscriber != null) {

                subscriberDeletionRepository.deleteSubscriberRecord(subscriberUid);
                return exceptionHandlerUtil.successResponse(SUBSCRIBER_DETAILS_DELETED);
            } else
                return exceptionHandlerUtil.createErrorResponse(SUBSCRIBER_NOT_FOUND);
        } catch (JDBCConnectionException | ConstraintViolationException | DataException | LockAcquisitionException
                 | PessimisticLockException | QueryTimeoutException | SQLGrammarException | GenericJDBCException e) {
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);
        } catch (Exception e) {
            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public ApiResponse getAllSubscribersDataFromView() {
        try {
            logger.info(" inside getAllSubscribersDataFromView implimentation");
            List<SubscriberCompleteDetail> subscriberCompleteDetailsList = subscriberCompleteDetailRepoIface
                    .getAllActiveSubscribersDetails(Constant.ACTIVE);
            if (subscriberCompleteDetailsList == null) {
                return exceptionHandlerUtil.createErrorResponse(NO_DATA_FOUND);
            }
            ArrayList<SubscriberDetailsDto> subscriberDetailsDtoList = new ArrayList<>();
            for (SubscriberCompleteDetail details : subscriberCompleteDetailsList) {
                SubscriberDetailsDto subscriberDetailsDto = new SubscriberDetailsDto();
                subscriberDetailsDto.setEmail(details.getEmailId());
                subscriberDetailsDto.setPhoneNo(details.getMobileNumber());
                subscriberDetailsDto.setFullName(details.getFullName());
                subscriberDetailsDto.setSubscriberStatus(details.getSubscriberStatus());
                subscriberDetailsDtoList.add(subscriberDetailsDto);
            }
            return exceptionHandlerUtil.successResponse(SUBSCRIBER_DETAILS);

        } catch (JDBCConnectionException | ConstraintViolationException | DataException | LockAcquisitionException

                 | PessimisticLockException | QueryTimeoutException | SQLGrammarException | GenericJDBCException e) {

            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);

        } catch (Exception e) {

            logger.error(ECXEPTION, e);
            return exceptionHandlerUtil.createErrorResponse(SOMETHING_WENT_WRONG);

        }

    }

}