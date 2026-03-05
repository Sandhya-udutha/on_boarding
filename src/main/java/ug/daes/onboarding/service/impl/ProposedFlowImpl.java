package ug.daes.onboarding.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Lazy;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.InvalidUrlException;
import ug.daes.DAESService;

import ug.daes.Result;

import ug.daes.onboarding.config.SentryClientExceptions;
import ug.daes.onboarding.constant.ApiResponse;

import ug.daes.onboarding.dto.*;

import ug.daes.onboarding.exceptions.DuplicateIdentificationException;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.*;
import ug.daes.onboarding.repository.*;

import ug.daes.onboarding.service.iface.ProposedFlowIface;
import ug.daes.onboarding.service.iface.SubscriberServiceIface;

import ug.daes.onboarding.util.AppUtil;
import ug.daes.onboarding.util.Utility;

import javax.sql.rowset.serial.SerialBlob;

import java.io.IOException;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.sql.Blob;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;


@Service
public class ProposedFlowImpl implements ProposedFlowIface {
    private static final Logger logger = LoggerFactory.getLogger(ProposedFlowImpl.class);
    private static final String CLASS = "ProposedFlowImpl";
    private static final String EXCEPTION = "{}{} - Exception: {}";
    private static final String DOCUMENT_NUMBER = "DocumentNo";
    private static final String COMPLETED = "COMPLETED";
    private static final String NO_RECORD_FOUND = "api.error.no.record.found.for.given.document.id.number";
    private static final String TEMP_TABLE_RECORD_DELETED = "api.response.temporary.table.record.deleted.successfully";
    private static final String TEMP_TABLE_DTO_CANNOT_NULL = "api.error.temporary.table.dto.cannot.be.null";
    private static final String APPLICATION_INFO_NOT_FOUND = "api.error.application.info.not.found";
    private static final String RESPOSNE_DETAILS_FOUND = "api.response.details.found";
    private static final String ID_DOC_CANNOT_NULL = "api.error.id.doc.number.cannot.be.null";
    private static final String TEMP_TABLE_RECORD_CANNOT_DELETED_USING_DEVICEID = "api.error.temporary.table.record.is.not.deleted.by.using.device.id";
    private static final String UNEXPECTED_EXCEPTION = "Unexpected exception";
    private static final String RESIDENCE_INFO = "ResidenceInfo";

    // Config values
    @Value("${is.onboarding.fee}")
    private boolean isOnboardingFee;

    @Value("${verify.photo}")
    private boolean verifyPhoto;

    @Value("${age.validation.enabled}")
    private boolean ageValidationEnabled;

    @Value("${age.validation.min-age}")
    private int minAge;

    @Value("${id.document.verification.enabled}")
    private boolean idDocumentVerificationEnabled;


    @Value("${extract.features}")
    private String exractFeatures;

    @Value("${find.details}")
    private String findDetails;

    private final SentryClientExceptions sentryClientExceptions;
    private final RestTemplate restTemplate;
    private final SubscriberRepoIface subscriberRepoIface;
    private final SubscriberDeviceRepoIface subscriberDeviceRepoIface;
    private final SubscriberOnboardingDataRepoIface onboardingDataRepoIface;
    private final TemporaryTableRepo temporaryTableRepo;
    private final OnboardingStepDetailsRepoIface onboardingStepsRepoIface;
    private final SubscriberOnboardingDataRepoIface subscriberOnboardingDataRepoIface;
    private final PhotoFeaturesRepo photoFeaturesRepo;
    private final SubscriberServiceIface subscriberServiceIface;
    private final ExceptionHandlerUtil exceptionHandlerUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ProposedFlowImpl(@Lazy SubscriberServiceIface subscriberServiceIface,
                            SentryClientExceptions sentryClientExceptions,
                            RestTemplate restTemplate,
                            SubscriberRepoIface subscriberRepoIface,
                            SubscriberDeviceRepoIface subscriberDeviceRepoIface,
                            SubscriberOnboardingDataRepoIface onboardingDataRepoIface,
                            TemporaryTableRepo temporaryTableRepo,
                            OnboardingStepDetailsRepoIface onboardingStepsRepoIface,
                            SubscriberOnboardingDataRepoIface subscriberOnboardingDataRepoIface,
                            PhotoFeaturesRepo photoFeaturesRepo,
                            ExceptionHandlerUtil exceptionHandlerUtil) {
        this.subscriberServiceIface = subscriberServiceIface;
        this.sentryClientExceptions = sentryClientExceptions;
        this.restTemplate = restTemplate;
        this.subscriberRepoIface = subscriberRepoIface;
        this.subscriberDeviceRepoIface = subscriberDeviceRepoIface;
        this.onboardingDataRepoIface = onboardingDataRepoIface;
        this.temporaryTableRepo = temporaryTableRepo;
        this.onboardingStepsRepoIface = onboardingStepsRepoIface;
        this.subscriberOnboardingDataRepoIface = subscriberOnboardingDataRepoIface;
        this.photoFeaturesRepo = photoFeaturesRepo;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }

    @Override
    public ApiResponse saveDataTemporyTable(TemporaryTableDTO temporaryTableDTO) {
        try {
            if (Objects.isNull(temporaryTableDTO)) {
                return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_DTO_CANNOT_NULL);
            }
            if (temporaryTableDTO.getIdDocNumber() == null || temporaryTableDTO.getIdDocNumber().isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse(ID_DOC_CANNOT_NULL);
            }
            if (temporaryTableDTO.getStep() == 1) {
                logger.info("{}{} - Processing saveDataTemporyTable for step: {}", CLASS, Utility.getMethodName(),
                        temporaryTableDTO.getStep());
                ApiResponse response = flag1method(temporaryTableDTO);
                if (!response.isSuccess()) {
                    return exceptionHandlerUtil.createFailedResponseWithCustomMessage(response.getMessage(),
                            response.getResult());
                }
                return exceptionHandlerUtil.createSuccessResponseWithCustomMessage(response.getMessage(),
                        response.getResult());
            } else if (temporaryTableDTO.getStep() == 3) {
                logger.info("{}{} - saveDataTemporaryTable step: {}", CLASS, Utility.getMethodName(),
                        temporaryTableDTO.getStep());
                ApiResponse response = flag3method(temporaryTableDTO);
                if (!response.isSuccess()) {
                    return exceptionHandlerUtil.createFailedResponseWithCustomMessage(response.getMessage(),
                            response.getResult());
                }
                return exceptionHandlerUtil.createSuccessResponseWithCustomMessage(response.getMessage(),
                        response.getResult());
            } else if (temporaryTableDTO.getStep() == 4) {
                logger.info("{}{} - saveDataTemporaryTable step: {}", CLASS, Utility.getMethodName(),
                        temporaryTableDTO.getStep());
                ApiResponse response = flag4method(temporaryTableDTO);
                if (!response.isSuccess()) {
                    return exceptionHandlerUtil.createFailedResponseWithCustomMessage(response.getMessage(),
                            response.getResult());
                }
                return exceptionHandlerUtil.createSuccessResponseWithCustomMessage(response.getMessage(),
                        response.getResult());
            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.step.not.found");
            }

        } catch (Exception e) {
            logger.error("{}{} - Exception in saveDataTemporyTable: {}", CLASS, Utility.getMethodName(),
                    e.getMessage());
            logger.error(UNEXPECTED_EXCEPTION, e);
            e.getCause();
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }


    public ApiResponse flag1method(TemporaryTableDTO temporaryTableDTO) {
        try {
            // 1️⃣ Validate incoming DTO and essential fields
            ApiResponse validationResponse = validateFlag1Input(temporaryTableDTO);
            if (validationResponse != null) return validationResponse;

            // 2️⃣ Check if subscriber already exists
            Subscriber subscriber = fetchExistingSubscriber(temporaryTableDTO);
            if (subscriber != null) {
                return handleExistingSubscriber(subscriber);
            }

            // 3️⃣ Check if device already onboarded
            ApiResponse deviceResponse = checkDeviceAlreadyOnboarded(temporaryTableDTO);
            if (deviceResponse != null) return deviceResponse;

            // 4️⃣ Fetch temporary table records
            TemporaryTable temporaryTable = temporaryTableRepo.getbyidDocNumber(temporaryTableDTO.getIdDocNumber());
            TemporaryTable temporaryTableDevice = temporaryTableRepo.getByDevice(temporaryTableDTO.getDeviceId());
            List<OnboardingStepDetails> onboardingStepDetailslist = onboardingStepsRepoIface.getAllSteps();

            // 5️⃣ Handle temporary table scenarios
            ApiResponse tempTableResponse = handleTemporaryTableRecords(
                    temporaryTableDTO, temporaryTable, temporaryTableDevice, onboardingStepDetailslist
            );
            if (tempTableResponse != null) return tempTableResponse;


            ApiResponse typeAndAgeResponse = validateSubscriberTypeAndAge(temporaryTableDTO);


            if (typeAndAgeResponse != null) return typeAndAgeResponse;

            ApiResponse documentExpiryResponse = validateSubscriberDocumentExpiry(temporaryTableDTO);


            if (documentExpiryResponse != null) return documentExpiryResponse;


            return exceptionHandlerUtil.createSuccessResponse(
                    "api.response.details.of.step1.saved.successfully.in.temporary.table",
                    buildTemporaryResponseDTO(temporaryTableDTO, onboardingStepDetailslist)
            );

        } catch (Exception e) {
            logger.error(EXCEPTION, CLASS, Utility.getMethodName(), e.getMessage());
            logger.error(UNEXPECTED_EXCEPTION, e);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private ApiResponse handleTemporaryTableRecords(
            TemporaryTableDTO temporaryTableDTO,
            TemporaryTable temporaryTable,
            TemporaryTable temporaryTableDevice,
            List<OnboardingStepDetails> onboardingStepDetailslist
    ) throws Exception {
        // Copy the logic you already had for temporary table handling
        if (temporaryTable != null) {

            // Case 1: Existing record with same device
            if (temporaryTableDTO.getIdDocNumber().equals(temporaryTable.getIdDocNumber())
                    && temporaryTableDTO.getDeviceId().equals(temporaryTable.getDeviceId())) {

                TemporaryResponseDto temporaryResponseDto = new TemporaryResponseDto();
                temporaryResponseDto.setIdDocNumber(temporaryTable.getIdDocNumber());
                temporaryResponseDto.setDeviceId(temporaryTable.getDeviceId());
                temporaryResponseDto.setOptionalData1(
                        (temporaryTable.getOptionalData1() != null
                                && !temporaryTable.getOptionalData1().isEmpty()
                                && !"0".equals(temporaryTable.getOptionalData1()))
                                ? temporaryTable.getOptionalData1()
                                : temporaryTable.getIdDocNumber()
                );

                SubscriberObDetails subscriberObDetails = objectMapper.readValue(
                        temporaryTable.getStep1Data(), SubscriberObDetails.class
                );
                temporaryResponseDto.setSubscriberObDetails(subscriberObDetails);
                temporaryResponseDto.setStep1Status(temporaryTable.getStep1Status());
                temporaryResponseDto.setStep2Status(temporaryTable.getStep2Status());
                temporaryResponseDto.setMobileNumber(temporaryTable.getStep3Data());
                temporaryResponseDto.setStep3Status(temporaryTable.getStep3Status());
                temporaryResponseDto.setEmailId(temporaryTable.getStep4Data());
                temporaryResponseDto.setStep4Status(temporaryTable.getStep4Status());
                temporaryResponseDto.setStep5Details(temporaryTable.getStep5Data());
                temporaryResponseDto.setStep5Status(temporaryTable.getStep5Status());
                temporaryResponseDto.setStepCompleted(temporaryTable.getStepCompleted());
                temporaryResponseDto.setNextStep(temporaryTable.getNextStep());
                temporaryResponseDto.setOnboardingStepDetails(onboardingStepDetailslist);
                temporaryResponseDto.setSelfieImage(temporaryTable.getSelfie());
                temporaryResponseDto.setDataInTemporaryTable(true);

                return exceptionHandlerUtil.createSuccessResponse(RESPOSNE_DETAILS_FOUND, temporaryResponseDto);
            }

            // Case 2: Existing record with new device
            else if (temporaryTableDevice == null
                    && temporaryTableDTO.getIdDocNumber().equals(temporaryTable.getIdDocNumber())
                    && !temporaryTableDTO.getDeviceId().equals(temporaryTable.getDeviceId())) {

                TemporaryResponseDto temporaryResponseDto = new TemporaryResponseDto();
                temporaryResponseDto.setNewDevice(true);

                return exceptionHandlerUtil.createSuccessResponse(
                        "api.response.do.you.want.to.continue.on.this.new.device", temporaryResponseDto
                );
            }
        }

        // Case 3: Device already exists in temp table with different ID
        if (temporaryTableDevice != null && !temporaryTableDevice.getIdDocNumber().equals(temporaryTableDTO.getIdDocNumber())) {
            TemporaryResponseDto temporaryResponseDto = new TemporaryResponseDto();
            temporaryResponseDto.setUsedDevice(true);
            return exceptionHandlerUtil.createSuccessResponse(
                    "api.response.this.device.is.already.registered.with.different.details.use.the.same.document.to.proceed.or.delete.the.existing.data.and.try.again",
                    temporaryResponseDto
            );
        }

        // If none of the above, return null to continue normal processing
        return null;
    }

    private ApiResponse validateFlag1Input(TemporaryTableDTO dto) {
        if (Objects.isNull(dto)) return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_DTO_CANNOT_NULL);
        if (dto.getDeviceId() == null || dto.getDeviceId().isEmpty())
            return exceptionHandlerUtil.createErrorResponse(APPLICATION_INFO_NOT_FOUND);

        SubscriberObDetails obData = dto.getSubscriberObDataDTO();
        if (Objects.isNull(obData)) return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_DTO_CANNOT_NULL);

        if (isEmpty(obData.getDocumentCode()))
            return exceptionHandlerUtil.createErrorResponse("api.error.doc.cant.be.null.or.empty");
        if (isEmpty(obData.getDocumentType()))
            return exceptionHandlerUtil.createErrorResponse("api.error.doctype.cant.be.null.or.empty");
        if (isEmpty(obData.getNationality()))
            return exceptionHandlerUtil.createErrorResponse("api.error.nationality.cant.be.null.or.empty");
        if (isEmpty(obData.getSubscriberType()))
            return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.type.cant.be.null.or.empty");
        if (isEmpty(obData.getDateOfBirth()))
            return exceptionHandlerUtil.createErrorResponse("api.error.date.of.birth.cant.be.null.or.empty");
        if (isEmpty(obData.getDateOfExpiry()))
            return exceptionHandlerUtil.createErrorResponse("api.error.date.of.expiry.cant.be.null.or.empty");
        if (isEmpty(obData.getGeoLocation()))
            return exceptionHandlerUtil.createErrorResponse("api.error.geolocation.cant.be.null.or.empty");

        if (dto.getSubscriberDeviceInfoDto() == null)
            return exceptionHandlerUtil.createErrorResponse(APPLICATION_INFO_NOT_FOUND);
        if (isEmpty(dto.getSubscriberDeviceInfoDto().getFcmToken()))
            return exceptionHandlerUtil.createErrorResponse(APPLICATION_INFO_NOT_FOUND);

        return null;
    }

    private boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private Subscriber fetchExistingSubscriber(TemporaryTableDTO dto) throws Exception {
        List<SubscriberOnboardingData> records = subscriberOnboardingDataRepoIface.findSubscriberByDocIdLatestRecord(dto.getIdDocNumber());
        SubscriberOnboardingData onboardingData = records.isEmpty() ? null : records.get(0);

        if (onboardingData != null) {
            return subscriberRepoIface.findBysubscriberUid(onboardingData.getSubscriberUid());
        } else {
            return subscriberRepoIface.findbyDocumentNumber(dto.getIdDocNumber());
        }
    }

    private ApiResponse handleExistingSubscriber(Subscriber subscriber) {
        try {
            TemporaryResponseDto tempResponse = new TemporaryResponseDto();
            tempResponse.setSubscriber(subscriber);
            tempResponse.setExistingSubscriber(true);

            // Get selfie
            SubscriberOnboardingData latest = onboardingDataRepoIface.findLatestSubscriber(subscriber.getSubscriberUid())
                    .stream().findFirst().orElse(null);
            if (latest != null && latest.getSelfieUri() != null) {
                tempResponse.setSelfieImage(fetchSelfieBase64(latest.getSelfieUri()));
            }

            return exceptionHandlerUtil.createSuccessResponse(
                    "api.response.it.seems.your.already.have.an.ugpass.account.kindly.log.in.to.access.your.account",
                    tempResponse
            );
        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private String fetchSelfieBase64(String selfieUri) {
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Object> request = new HttpEntity<>(headers);
            ResponseEntity<byte[]> resp = restTemplate.exchange(selfieUri, HttpMethod.GET, request, byte[].class);
            return AppUtil.getBase64FromByteArr(resp.getBody());
        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            sentryClientExceptions.captureExceptions(e);
            return null;
        }
    }

    private ApiResponse checkDeviceAlreadyOnboarded(TemporaryTableDTO dto) {
        List<SubscriberDevice> devices = subscriberDeviceRepoIface.findDeviceDetailsById(dto.getDeviceId());
        SubscriberDevice subscriberDevice = devices.isEmpty() ? null : devices.get(0);

        if (subscriberDevice != null && "ACTIVE".equals(subscriberDevice.getDeviceStatus())) {
            return exceptionHandlerUtil.createErrorResponse("api.error.device.is.already.registered.with.onboarded.user");
        }
        return null;
    }

    private ApiResponse validateSubscriberTypeAndAge(TemporaryTableDTO dto) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(dto.getSubscriberObDataDTO()));
        String subscriberType = jsonNode.get("subscriberType").asText();
        if ("null".equals(subscriberType) || subscriberType.isEmpty()) {
            return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.type");
        }

        if (ageValidationEnabled) {
            LocalDate dob = AppUtil.parseToLocalDate(jsonNode.get("dateOfBirth").asText());
            int age = AppUtil.calculateAge(dob);
            if (age < minAge) {
                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.you.cannot.onboard.Minimum.allowed.age.is.16.as.per.current.policy"
                );
            }
        }
        return null;
    }

    private ApiResponse validateSubscriberDocumentExpiry(TemporaryTableDTO dto) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(dto.getSubscriberObDataDTO()));
        String doe = jsonNode.get("dateOfExpiry").asText();
        if (idDocumentVerificationEnabled) {
            if (doe == null) {
                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.id.document.expiry.date.cannot.be.null");
            }

            LocalDate dateOfExpiry = AppUtil.parseToLocalDate(doe);

            LocalDate today = LocalDate.now();
            logger.info("Parsed Date of Expiry:::{}", dateOfExpiry);

            if (dateOfExpiry.isBefore(today)) {
                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.id.document.expiry.is.expired");
            }
        }

        return null;
    }

    private TemporaryResponseDto buildTemporaryResponseDTO(TemporaryTableDTO dto, List<OnboardingStepDetails> steps) {
        TemporaryResponseDto tempResponse = new TemporaryResponseDto();
        tempResponse.setSubscriberObDetails(dto.getSubscriberObDataDTO());
        tempResponse.setSubscriberDeviceInfoDto(dto.getSubscriberDeviceInfoDto());
        tempResponse.setStep1Status(COMPLETED);
        tempResponse.setStepCompleted(dto.getStep());
        tempResponse.setOnboardingStepDetails(steps);
        return tempResponse;
    }

    @SuppressWarnings("null")
    public ApiResponse flag2method(TemporaryTableDTO temporaryTableDTO, MultipartFile livelinessVideo, String selfie) {
        try {
            // 1️⃣ Basic validations
            ApiResponse validationResponse = validateFlag2Dto(temporaryTableDTO, selfie);
            if (validationResponse != null) return validationResponse;

            TemporaryTable temporaryTable = temporaryTableRepo.getbyidDocNumber(temporaryTableDTO.getIdDocNumber());
            List<OnboardingStepDetails> onboardingStepDetailslist = onboardingStepsRepoIface.getAllSteps();
            ApiResponse tempTableCheck = validateTemporaryTableFlag2(temporaryTable, onboardingStepDetailslist);
            if (tempTableCheck != null) return tempTableCheck;

            // 2️⃣ If step2 already completed, return existing data
            if (temporaryTable.getStepCompleted() == 2 || temporaryTable.getSelfie() != null) {
                return buildStep2Response(temporaryTable, onboardingStepDetailslist, true);
            }

            // 3️⃣ Update temporary table with new video & selfie
            updateTemporaryTableStep2(temporaryTable, temporaryTableDTO, selfie);

            // 4️⃣ Handle NiraResponse
            handleNiraResponse(temporaryTableDTO, temporaryTable);

            // 5️⃣ Save and return success response
            temporaryTableRepo.save(temporaryTable);
            return buildStep2Response(temporaryTable, onboardingStepDetailslist, false);

        } catch (Exception e) {
            logger.error("{}{} - Exception occurred in flag2method: {}", CLASS, Utility.getMethodName(), e.getMessage(), e);
            logger.error(UNEXPECTED_EXCEPTION, e);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    /* ===== Helper Methods ===== */

    private ApiResponse validateFlag2Dto(TemporaryTableDTO dto, String selfie) {
        if (Objects.isNull(dto)) return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_DTO_CANNOT_NULL);
        if (!StringUtils.hasText(selfie))
            return exceptionHandlerUtil.createErrorResponse("api.error.selfie.cannot.be.null");
        return null;
    }

    private ApiResponse validateTemporaryTableFlag2(TemporaryTable table, List<OnboardingStepDetails> stepsList) {
        if (stepsList == null || stepsList.isEmpty())
            return exceptionHandlerUtil.createErrorResponse("api.error.onboarding.steps.cannot.be.null.or.empty");
        if (Objects.isNull(table))
            return exceptionHandlerUtil.createErrorResponse("api.error.document.details.not.found");
        return null;
    }

    private void updateTemporaryTableStep2(TemporaryTable table, TemporaryTableDTO dto, String selfie) throws Exception {
        table.setStep2Status(COMPLETED);
        table.setStep2Data(objectMapper.writeValueAsString(dto.getVideoDetailsDto()));
        table.setSelfie(selfie);
        table.setUpdatedOn(AppUtil.getDate());

        ApiResponse res = nextStepDetails(dto.getStep());
        if (!res.isSuccess()) {
            table.setNextStep(dto.getStep());
        } else {
            String responseStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(res.getResult());
            OnboardingStepDetails stepDetails = objectMapper.readValue(responseStr, OnboardingStepDetails.class);
            table.setNextStep(stepDetails.getStepId());
        }
        table.setStepCompleted(dto.getStep());
    }

    private void handleNiraResponse(TemporaryTableDTO dto, TemporaryTable table) throws Exception {
        if (dto.getNiraResponse() == null || dto.getNiraResponse().toString().isEmpty()) return;

        if (isOnboardingFee) {
            Result r = DAESService.createSecureWireData(objectMapper.writeValueAsString(dto.getNiraResponse()));
            table.setNiraResponse(new String(r.getResponse()));
        } else {
            JsonNode root = objectMapper.readTree(dto.getNiraResponse().toString());
            JsonNode dataNode = root.path("customerDetails").path("Result").path("Data");
            String passportNumber = dataNode.path("ActivePassport").path(DOCUMENT_NUMBER).asText(null);
            String emiratesIdNumber = dataNode.path(RESIDENCE_INFO).path("EmiratesIdNumber").asText(null);
            String emiratesIdDocumentNumber = dataNode.path(RESIDENCE_INFO).path(DOCUMENT_NUMBER).asText(null);

            List<String> reasons = subscriberRepoIface.findDuplicateReason(passportNumber, emiratesIdNumber, emiratesIdDocumentNumber);
            if (!reasons.isEmpty()) {
                String reason = reasons.get(0);
                switch (reason) {
                    case "PASSPORT":
                        throw new DuplicateIdentificationException(reason, "Passport number already used");
                    case "NATIONAL_ID":
                        throw new DuplicateIdentificationException(reason, "Emirates ID number already used");
                    case "NATIONAL_ID_CARD":
                        throw new DuplicateIdentificationException(reason, "Emirates ID card number already used");
                    default:
                        throw new DuplicateIdentificationException(reason, "Unknown identification type: " + reason);
                }
            }
            table.setNiraResponse(dto.getNiraResponse().toString());
        }
    }

    private ApiResponse buildStep2Response(TemporaryTable table, List<OnboardingStepDetails> stepsList, boolean dataInTable) throws Exception {
        TemporaryResponseDto responseDto = new TemporaryResponseDto();
        responseDto.setIdDocNumber(table.getIdDocNumber());
        responseDto.setDeviceId(table.getDeviceId());
        responseDto.setOptionalData1((table.getOptionalData1() != null && !table.getOptionalData1().isEmpty() && !"0".equals(table.getOptionalData1()))
                ? table.getOptionalData1() : table.getIdDocNumber());
        responseDto.setSubscriberObDetails(objectMapper.readValue(table.getStep1Data(), SubscriberObDetails.class));
        responseDto.setStep1Status(table.getStep1Status());
        responseDto.setVideoDetailsDto(objectMapper.readValue(table.getStep2Data(), VideoDetailsDto.class));
        responseDto.setStep2Status(table.getStep2Status());
        responseDto.setMobileNumber(table.getStep3Data());
        responseDto.setStep3Status(table.getStep3Status());
        responseDto.setEmailId(table.getStep4Data());
        responseDto.setStep4Status(table.getStep4Status());
        responseDto.setStep5Details(table.getStep5Data());
        responseDto.setStep5Status(table.getStep5Status());
        responseDto.setStepCompleted(table.getStepCompleted());
        responseDto.setNextStep(table.getNextStep());
        responseDto.setSelfieImage(table.getSelfie());
        responseDto.setOnboardingStepDetails(stepsList);
        responseDto.setDataInTemporaryTable(dataInTable);

        return exceptionHandlerUtil.createSuccessResponse(RESPOSNE_DETAILS_FOUND, responseDto);
    }

    public ApiResponse flag3method(TemporaryTableDTO temporaryTableDTO) {
        try {
            // 1️⃣ Validate DTO
            ApiResponse validationResponse = validateFlag3Dto(temporaryTableDTO);
            if (validationResponse != null) return validationResponse;

            Subscriber subscriber = subscriberRepoIface.findBymobileNumber(temporaryTableDTO.getMobileNumber());
            TemporaryTable temporaryTable = temporaryTableRepo.getbyidDocNumber(temporaryTableDTO.getIdDocNumber());
            TemporaryTable temporaryTableMobile = temporaryTableRepo.getByMobNumber(temporaryTableDTO.getMobileNumber());
            List<OnboardingStepDetails> onboardingStepDetailslist = onboardingStepsRepoIface.getAllSteps();

            // 2️⃣ Handle existing subscriber
            if (subscriber != null) {
                return handleExistingMobileSubscriber(subscriber);
            }

            // 3️⃣ Handle missing temporary table
            if (Objects.isNull(temporaryTable)) {
                return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_DTO_CANNOT_NULL);
            }

            // 4️⃣ Handle temporary table mobile checks
            ApiResponse tempMobileResponse = handleTemporaryMobile(temporaryTableDTO, temporaryTable, temporaryTableMobile, onboardingStepDetailslist);
            if (tempMobileResponse != null) return tempMobileResponse;

            // 5️⃣ Update temporary table with new mobile number
            return updateTemporaryTableMobile(temporaryTableDTO, temporaryTable, onboardingStepDetailslist);

        } catch (Exception e) {
            logger.error(EXCEPTION, CLASS, Utility.getMethodName(), e.getMessage());
            logger.error(UNEXPECTED_EXCEPTION, e);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    /* ===== Helper Methods ===== */

    // 1️⃣ DTO validation
    private ApiResponse validateFlag3Dto(TemporaryTableDTO dto) {
        if (Objects.isNull(dto)) return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_DTO_CANNOT_NULL);
        if (!StringUtils.hasText(dto.getIdDocNumber()))
            return exceptionHandlerUtil.createErrorResponse(ID_DOC_CANNOT_NULL);
        if (!StringUtils.hasText(dto.getMobileNumber()))
            return exceptionHandlerUtil.createErrorResponse("api.error.mobile.number.cant.be.empty");
        return null;
    }

    // 2️⃣ Existing subscriber handling
    private ApiResponse handleExistingMobileSubscriber(Subscriber subscriber) {
        logger.info("{}{} - details of onboarded subscriber: {}", CLASS, Utility.getMethodName(), subscriber);
        TemporaryResponseDto responseDto = new TemporaryResponseDto();
        responseDto.setSubscriber(subscriber);
        responseDto.setExistingSubscriber(true);
        return exceptionHandlerUtil.createErrorResponse("api.error.this.mobile.number.belongs.to.onboard.user");
    }

    // 3️⃣ Temporary table mobile checks
    private ApiResponse handleTemporaryMobile(TemporaryTableDTO dto, TemporaryTable temporaryTable,
                                              TemporaryTable temporaryTableMobile, List<OnboardingStepDetails> onboardingStepDetailslist) throws Exception {

        // Case: Mobile already exists in this temp table
        if (temporaryTable != null
                && dto.getIdDocNumber().equals(temporaryTable.getIdDocNumber())
                && dto.getMobileNumber().equals(temporaryTable.getStep3Data())) {
            return buildTemporaryMobileResponse(temporaryTable, onboardingStepDetailslist, true);
        }

        // Case: New mobile for same doc
        if (temporaryTableMobile == null
                && temporaryTable.getIdDocNumber().equals(dto.getIdDocNumber())
                && (temporaryTable.getStep3Data() != null && !temporaryTable.getStep3Data().equals(dto.getMobileNumber()))) {

            TemporaryResponseDto responseDto = new TemporaryResponseDto();
            responseDto.setNewMobileNumber(true);
            logger.info("{}{} - Do you want to continue with this new Mobile number for idDocNumber: {}", CLASS,
                    Utility.getMethodName(), temporaryTable.getIdDocNumber());
            return exceptionHandlerUtil.createErrorResponseWithResult(
                    "api.error.do.you.want.to.continue.with.this.new.mobile.number", responseDto
            );
        }

        // Case: Mobile used by another onboarding user
        if (temporaryTableMobile != null && !temporaryTableMobile.getIdDocNumber().equals(dto.getIdDocNumber())) {
            TemporaryResponseDto responseDto = new TemporaryResponseDto();
            responseDto.setUsedMobNumber(true);
            logger.info("{}{} - This Mobile number belongs to another onboarding user for idDocNumber: {}", CLASS,
                    Utility.getMethodName(), temporaryTable.getIdDocNumber());
            return exceptionHandlerUtil.createErrorResponseWithResult(
                    "api.error.this.mobile.number.belongs.to.onboard.user", responseDto
            );
        }

        return null;
    }

    // 4️⃣ Build TemporaryResponseDto for existing temp table
    private ApiResponse buildTemporaryMobileResponse(TemporaryTable temporaryTable,
                                                     List<OnboardingStepDetails> onboardingStepDetailslist,
                                                     boolean dataInTable) throws Exception {

        TemporaryResponseDto responseDto = new TemporaryResponseDto();
        responseDto.setIdDocNumber(temporaryTable.getIdDocNumber());
        responseDto.setDeviceId(temporaryTable.getDeviceId());
        responseDto.setOptionalData1(
                (temporaryTable.getOptionalData1() != null && !temporaryTable.getOptionalData1().isEmpty()
                        && !"0".equals(temporaryTable.getOptionalData1()))
                        ? temporaryTable.getOptionalData1() : temporaryTable.getIdDocNumber()
        );
        responseDto.setSubscriberObDetails(objectMapper.readValue(temporaryTable.getStep1Data(), SubscriberObDetails.class));
        responseDto.setStep1Status(temporaryTable.getStep1Status());
        responseDto.setStep2Status(temporaryTable.getStep2Status());
        responseDto.setMobileNumber(temporaryTable.getStep3Data());
        responseDto.setStep3Status(temporaryTable.getStep3Status());
        responseDto.setEmailId(temporaryTable.getStep4Data());
        responseDto.setStep4Status(temporaryTable.getStep4Status());
        responseDto.setStep5Details(temporaryTable.getStep5Data());
        responseDto.setStep5Status(temporaryTable.getStep5Status());
        responseDto.setStepCompleted(temporaryTable.getStepCompleted());
        responseDto.setNextStep(temporaryTable.getNextStep());
        responseDto.setOnboardingStepDetails(onboardingStepDetailslist);
        responseDto.setSelfieImage(temporaryTable.getSelfie());
        responseDto.setDataInTemporaryTable(dataInTable);

        return exceptionHandlerUtil.createSuccessResponse(RESPOSNE_DETAILS_FOUND, responseDto);
    }

    // 5️⃣ Update temporary table with new mobile number
    private ApiResponse updateTemporaryTableMobile(TemporaryTableDTO dto, TemporaryTable temporaryTable,
                                                   List<OnboardingStepDetails> onboardingStepDetailslist) throws Exception {

        TemporaryResponseDto responseDto = new TemporaryResponseDto();
        responseDto.setIdDocNumber(temporaryTable.getIdDocNumber());
        responseDto.setSubscriberObDetails(objectMapper.readValue(temporaryTable.getStep1Data(), SubscriberObDetails.class));
        responseDto.setStep1Status(temporaryTable.getStep1Status());
        responseDto.setStep2Status(temporaryTable.getStep2Status());
        responseDto.setDeviceId(temporaryTable.getDeviceId());
        responseDto.setOptionalData1(
                (temporaryTable.getOptionalData1() != null && !temporaryTable.getOptionalData1().isEmpty() && !"0".equals(temporaryTable.getOptionalData1()))
                        ? temporaryTable.getOptionalData1() : temporaryTable.getIdDocNumber()
        );
        responseDto.setCreatedOn(temporaryTable.getCreatedOn());
        responseDto.setUpdatedOn(temporaryTable.getUpdatedOn());

        // Update mobile
        temporaryTable.setStep3Data(dto.getMobileNumber());
        responseDto.setMobileNumber(dto.getMobileNumber());
        temporaryTable.setStep3Status(COMPLETED);
        responseDto.setStep3Status(COMPLETED);
        temporaryTable.setStepCompleted(dto.getStep());
        responseDto.setStepCompleted(dto.getStep());

        ApiResponse res = nextStepDetails(dto.getStep());
        if (!res.isSuccess()) {
            temporaryTable.setNextStep(dto.getStep());
            responseDto.setNextStep(dto.getStep());
        } else {
            String responseStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(res.getResult());
            OnboardingStepDetails responseStep = objectMapper.readValue(responseStr, OnboardingStepDetails.class);
            temporaryTable.setNextStep(responseStep.getStepId());
            responseDto.setNextStep(responseStep.getStepId());
        }

        responseDto.setOnboardingStepDetails(onboardingStepDetailslist);
        temporaryTableRepo.save(temporaryTable);

        return exceptionHandlerUtil.createSuccessResponse(
                "api.response.details.of.step3.saved.successfully.in.temporary.table", responseDto
        );
    }

    public ApiResponse flag4method(TemporaryTableDTO temporaryTableDTO) {
        try {
            // 1️⃣ Validate DTO
            ApiResponse validationResponse = validateFlag4Dto(temporaryTableDTO);
            if (validationResponse != null) return validationResponse;

            // 2️⃣ Fetch DB objects
            String emailIdLower = temporaryTableDTO.getEmailId().toLowerCase();
            temporaryTableDTO.setEmailId(emailIdLower);
            Subscriber subscriber = subscriberRepoIface.findByemailId(emailIdLower);
            TemporaryTable temporaryTableEmail = temporaryTableRepo.getByEmail(emailIdLower);
            TemporaryTable temporaryTable = temporaryTableRepo.getbyidDocNumber(temporaryTableDTO.getIdDocNumber());
            List<OnboardingStepDetails> onboardingStepDetailslist = onboardingStepsRepoIface.getAllSteps();

            // 3️⃣ Handle existing onboarded subscriber
            if (subscriber != null) return handleExistingEmailSubscriber(subscriber);

            // 4️⃣ Validate temporary table and step3Data
            ApiResponse tempTableValidation = validateTemporaryTableStep3(temporaryTable);
            if (tempTableValidation != null) return tempTableValidation;

            // 5️⃣ Handle cases where email already exists or new
            ApiResponse tempEmailResponse = handleTemporaryEmailCases(temporaryTableDTO, temporaryTable, temporaryTableEmail, onboardingStepDetailslist);
            if (tempEmailResponse != null) return tempEmailResponse;

            // 6️⃣ Update temporary table with new email and next step
            return updateTemporaryTableEmail(temporaryTableDTO, temporaryTable, onboardingStepDetailslist);

        } catch (Exception e) {
            logger.error(EXCEPTION, CLASS, Utility.getMethodName(), e.getMessage());
            logger.error(UNEXPECTED_EXCEPTION, e);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    /* ===== Helper Methods ===== */

    // 1️⃣ DTO validation
    private ApiResponse validateFlag4Dto(TemporaryTableDTO dto) {
        if (Objects.isNull(dto)) return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_DTO_CANNOT_NULL);
        if (!StringUtils.hasText(dto.getIdDocNumber()))
            return exceptionHandlerUtil.createErrorResponse(ID_DOC_CANNOT_NULL);
        if (!StringUtils.hasText(dto.getEmailId()))
            return exceptionHandlerUtil.createErrorResponse("api.error.email.id.cant.be.empty");
        logger.info("{}{} - flag4method EmailID: {}", CLASS, Utility.getMethodName(), dto.getEmailId());
        return null;
    }

    // 2️⃣ Existing subscriber handling
    private ApiResponse handleExistingEmailSubscriber(Subscriber subscriber) {
        logger.info("{}{} - details of onboarded subscriber: {}", CLASS, Utility.getMethodName(), subscriber);
        TemporaryResponseDto responseDto = new TemporaryResponseDto();
        responseDto.setSubscriber(subscriber);
        responseDto.setExistingSubscriber(true);
        return exceptionHandlerUtil.createErrorResponse("api.error.this.email.id.belongs.to.onboard.user");
    }

    // 3️⃣ Temporary table step3 validation
    private ApiResponse validateTemporaryTableStep3(TemporaryTable temporaryTable) {
        if (temporaryTable == null) return exceptionHandlerUtil.createErrorResponse("api.error.details.not.found");
        if (!StringUtils.hasText(temporaryTable.getStep3Data()))
            return exceptionHandlerUtil.createErrorResponse("api.error.mobile.number.not.found");
        return null;
    }

    // 4️⃣ Handle email-specific cases
    private ApiResponse handleTemporaryEmailCases(TemporaryTableDTO dto, TemporaryTable temporaryTable,
                                                  TemporaryTable temporaryTableEmail, List<OnboardingStepDetails> stepsList) throws Exception {

        // Case: email already exists in temp table
        if (dto.getEmailId().equals(temporaryTable.getStep4Data())) {
            return buildTemporaryEmailResponse(temporaryTable, stepsList, true);
        }

        // Case: new email for same doc
        if (temporaryTableEmail == null
                && dto.getIdDocNumber().equals(temporaryTable.getIdDocNumber())
                && !dto.getEmailId().equals(temporaryTable.getStep4Data())) {

            TemporaryResponseDto responseDto = new TemporaryResponseDto();
            responseDto.setNewEmailId(true);
            logger.info("{}{} - Do you want to continue with this new Email id for idDocNumber: {}", CLASS,
                    Utility.getMethodName(), temporaryTable.getIdDocNumber());
            return exceptionHandlerUtil.createErrorResponseWithResult(
                    "api.error.do.you.want.to.continue.with.this.new.email.id", responseDto);
        }

        // Case: email used by another onboarding user
        if (temporaryTableEmail != null
                && !temporaryTableEmail.getIdDocNumber().equals(dto.getIdDocNumber())) {
            TemporaryResponseDto responseDto = new TemporaryResponseDto();
            responseDto.setUsedEmail(true);
            logger.info("{}{} - This email ID belongs to another onboarding user for idDocNumber: {}", CLASS,
                    Utility.getMethodName(), temporaryTable.getIdDocNumber());
            return exceptionHandlerUtil.createErrorResponseWithResult(
                    "api.error.this.email.id.belongs.to.onboard.user", responseDto);
        }

        return null;
    }

    // 5️⃣ Build TemporaryResponseDto for email
    private ApiResponse buildTemporaryEmailResponse(TemporaryTable temporaryTable,
                                                    List<OnboardingStepDetails> stepsList,
                                                    boolean dataInTable) throws Exception {

        TemporaryResponseDto responseDto = new TemporaryResponseDto();
        responseDto.setIdDocNumber(temporaryTable.getIdDocNumber());
        responseDto.setDeviceId(temporaryTable.getDeviceId());
        responseDto.setOptionalData1((temporaryTable.getOptionalData1() != null
                && !temporaryTable.getOptionalData1().isEmpty()
                && !"0".equals(temporaryTable.getOptionalData1()))
                ? temporaryTable.getOptionalData1() : temporaryTable.getIdDocNumber());
        responseDto.setSubscriberObDetails(objectMapper.readValue(temporaryTable.getStep1Data(), SubscriberObDetails.class));
        responseDto.setStep1Status(temporaryTable.getStep1Status());
        responseDto.setStep2Status(temporaryTable.getStep2Status());
        responseDto.setMobileNumber(temporaryTable.getStep3Data());
        responseDto.setStep3Status(temporaryTable.getStep3Status());
        responseDto.setEmailId(temporaryTable.getStep4Data());
        responseDto.setStep4Status(temporaryTable.getStep4Status());
        responseDto.setStep5Details(temporaryTable.getStep5Data());
        responseDto.setStep5Status(temporaryTable.getStep5Status());
        responseDto.setStepCompleted(temporaryTable.getStepCompleted());
        responseDto.setNextStep(temporaryTable.getNextStep());
        responseDto.setSelfieImage(temporaryTable.getSelfie());
        responseDto.setOnboardingStepDetails(stepsList);
        responseDto.setDataInTemporaryTable(dataInTable);

        return exceptionHandlerUtil.createSuccessResponse(RESPOSNE_DETAILS_FOUND, responseDto);
    }

    // 6️⃣ Update temporary table with new email
    private ApiResponse updateTemporaryTableEmail(TemporaryTableDTO dto, TemporaryTable temporaryTable,
                                                  List<OnboardingStepDetails> stepsList) throws Exception {

        TemporaryResponseDto responseDto = new TemporaryResponseDto();
        responseDto.setIdDocNumber(temporaryTable.getIdDocNumber());
        responseDto.setSubscriberObDetails(objectMapper.readValue(temporaryTable.getStep1Data(), SubscriberObDetails.class));
        responseDto.setStep1Status(temporaryTable.getStep1Status());
        responseDto.setStep2Status(temporaryTable.getStep2Status());
        responseDto.setDeviceId(temporaryTable.getDeviceId());
        responseDto.setOptionalData1((temporaryTable.getOptionalData1() != null && !temporaryTable.getOptionalData1().isEmpty() && !"0".equals(temporaryTable.getOptionalData1()))
                ? temporaryTable.getOptionalData1() : temporaryTable.getIdDocNumber());
        responseDto.setCreatedOn(temporaryTable.getCreatedOn());
        responseDto.setUpdatedOn(temporaryTable.getUpdatedOn());
        responseDto.setMobileNumber(temporaryTable.getStep3Data());

        // Update email and step info
        temporaryTable.setStep4Data(dto.getEmailId());
        responseDto.setEmailId(dto.getEmailId());
        temporaryTable.setStep4Status(COMPLETED);
        responseDto.setStep4Status(COMPLETED);
        temporaryTable.setStepCompleted(dto.getStep());
        responseDto.setStepCompleted(dto.getStep());

        // Next step handling
        ApiResponse res = nextStepDetails(dto.getStep());
        if (!res.isSuccess()) {
            temporaryTable.setNextStep(dto.getStep());
            responseDto.setNextStep(dto.getStep());
        } else {
            String responseStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(res.getResult());
            OnboardingStepDetails responseStep = objectMapper.readValue(responseStr, OnboardingStepDetails.class);
            temporaryTable.setNextStep(responseStep.getStepId());
            responseDto.setNextStep(responseStep.getStepId());
        }

        responseDto.setOnboardingStepDetails(stepsList);
        temporaryTableRepo.save(temporaryTable);

        return exceptionHandlerUtil.createSuccessResponse(
                "api.response.details.of.step4.saved.successfully.in.temporary.table", responseDto
        );
    }

    public static SubscriberOnboardingData findLatestOnboardedSub(
            List<SubscriberOnboardingData> subscriberOnboardingData) {
        Date[] dates = new Date[subscriberOnboardingData.size() - 1];
        int i = 0;
        SimpleDateFormat simpleDateFormat = null;
        for (SubscriberOnboardingData s : subscriberOnboardingData) {

            try {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = simpleDateFormat.parse(s.getCreatedDate());

                dates[i] = date;
                i++;
            } catch (Exception e) {
                logger.error(UNEXPECTED_EXCEPTION, e);
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
    public ApiResponse submitObData(String idDocumentNumber) {
        try {
            logger.info("{}{} - Received request to submit data for idDocumentNumber: {}", CLASS,
                    Utility.getMethodName(), idDocumentNumber);
            if (!StringUtils.hasText(idDocumentNumber) || "null".equalsIgnoreCase(idDocumentNumber)) {
                return exceptionHandlerUtil.createErrorResponse("api.error.id.document.number.cannot.be.null");
            }
            TemporaryTable temporaryTable = temporaryTableRepo.getbyidDocNumber(idDocumentNumber);
            int countOfValues = onboardingStepsRepoIface.getNoOfOnboardingSteps();
            if (Objects.isNull(temporaryTable)) {
                return exceptionHandlerUtil.createErrorResponse("api.error.no.data.found.for.given.id.doc.number");
            } else if (temporaryTable.getStepCompleted() != countOfValues) {
                return exceptionHandlerUtil.createErrorResponse("api.error.please.complete.all.steps");
            }

            String featuresBase64 = null;

            if (verifyPhoto) {
                ApiResponse responseOfFaceVerification = verifyFaceFeatures(temporaryTable.getSelfie());
                if (!responseOfFaceVerification.isSuccess()) {
                    return AppUtil.createApiResponse(false, responseOfFaceVerification.getMessage(),
                            responseOfFaceVerification.getResult());
                }
                featuresBase64 = responseOfFaceVerification.getResult().toString();
            }

            String step1Json = temporaryTable.getStep1Data();

            String deviceInfo = temporaryTable.getDeviceInfo();

            MobileOTPDto mobileOTPDto = new MobileOTPDto();
            JsonNode documentDetailsJson = objectMapper.readTree(step1Json);
            JsonNode deviceDetailsJson = objectMapper.readTree(deviceInfo);

            mobileOTPDto.setSubscriberName(documentDetailsJson.get("subscriberName").asText());
            mobileOTPDto.setDeviceId(temporaryTable.getDeviceId());
            mobileOTPDto.setSubscriberMobileNumber(temporaryTable.getStep3Data());
            mobileOTPDto.setSubscriberEmail(temporaryTable.getStep4Data());
            mobileOTPDto.setFcmToken(deviceDetailsJson.get("fcmToken").asText());
            mobileOTPDto.setOtpStatus(true);
            mobileOTPDto.setOsName(deviceDetailsJson.get("osName").asText());
            mobileOTPDto.setOsVersion(deviceDetailsJson.get("osVersion").asText());
            mobileOTPDto.setAppVersion(deviceDetailsJson.get("appVersion").asText());
            mobileOTPDto.setDeviceInfo(deviceDetailsJson.get("deviceInfo").asText());
            mobileOTPDto.setIdDocNumber(idDocumentNumber);

            ApiResponse response = subscriberServiceIface.saveSubscribersData(mobileOTPDto);

            if (!response.isSuccess()) {
                logger.info(CLASS + " submitObData  saveSubscribersData: 2  " + response);
                subscriberServiceIface.deleteRecord("", mobileOTPDto.getSubscriberEmail());
                return exceptionHandlerUtil.createFailedResponseWithCustomMessage(response.getMessage(),
                        response.getResult());
            }

            SubscriberRegisterResponseDTO responseDTO = (SubscriberRegisterResponseDTO) response.getResult();

            // Access the suID field
            String suID = responseDTO.getSuID();

            // saving data into photo features

            if (verifyPhoto) {
                byte[] decodedData = Base64.getDecoder().decode(featuresBase64);

                Blob blob = new SerialBlob(decodedData);

                PhotoFeaturesModel photoFeatures = new PhotoFeaturesModel();
                photoFeatures.setPhotoFeatures(blob);
                photoFeatures.setSuid(suID);
                photoFeatures.setCreatedOn(AppUtil.getDate());
                photoFeatures.setUpdatedOn(AppUtil.getDate());
                photoFeaturesRepo.save(photoFeatures);

            }


            SubscriberObRequestDTO subscriberObRequestDTO = createSubscriberObRequestDTO(suID, documentDetailsJson,
                    temporaryTable, idDocumentNumber);
            ApiResponse res = subscriberServiceIface.addSubscriberObData(subscriberObRequestDTO);

            if (!res.isSuccess()) {
                ApiResponse deleteResponse = subscriberServiceIface.deleteRecord("", mobileOTPDto.getSubscriberEmail());
                logger.info("{}{} - deleteResponse: {}", CLASS, Utility.getMethodName(), deleteResponse);
                return exceptionHandlerUtil.createFailedResponseWithCustomMessage(res.getMessage(), res.getResult());
            }

            int deleteValue = temporaryTableRepo.deleteRecordByIdDocumentNumber(idDocumentNumber);
            if (deleteValue != 1) {
                logger.info("{}{} - deleteValue: {}", CLASS, Utility.getMethodName(), deleteValue);
                return exceptionHandlerUtil.createErrorResponse("api.error.Record.not.deleted.from.temporary.table");
            }
            return exceptionHandlerUtil.createSuccessResponseWithCustomMessage(res.getMessage(), res.getResult());
        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            logger.error("{}{} - submitObData Exception : {}", CLASS, Utility.getMethodName(), e);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    public SubscriberObRequestDTO createSubscriberObRequestDTO(String suID, JsonNode documentDetailsJson,
                                                               TemporaryTable temporaryTable, String idDocumentNumber) {
        try {
            SubscriberObRequestDTO subscriberObRequestDTO = new SubscriberObRequestDTO();
            subscriberObRequestDTO.setSuID(suID);
            subscriberObRequestDTO.setOnboardingMethod(documentDetailsJson.get("onboardingMethod").asText());
            subscriberObRequestDTO.setTemplateId(documentDetailsJson.get("templateID").asInt());
            subscriberObRequestDTO.setSubscriberType(documentDetailsJson.get("subscriberType").asText());
            subscriberObRequestDTO.setNiraResponse(temporaryTable.getNiraResponse());

            SubscriberObData subscriberObData = new SubscriberObData();
            subscriberObData.setDateOfBirth(documentDetailsJson.get("dateOfBirth").asText());
            subscriberObData.setDateOfExpiry(documentDetailsJson.get("dateOfExpiry").asText());
            subscriberObData.setNationality(documentDetailsJson.get("nationality").asText());
            subscriberObData.setGender(documentDetailsJson.get("gender").asText());
            subscriberObData.setPrimaryIdentifier(documentDetailsJson.get("primaryIdentifier").asText());
            subscriberObData.setSecondaryIdentifier(documentDetailsJson.get("secondaryIdentifier").asText());
            subscriberObData.setDocumentType(documentDetailsJson.get("documentType").asText());
            subscriberObData.setDocumentCode(documentDetailsJson.get("documentCode").asText());
            subscriberObData.setOptionalData1(documentDetailsJson.get("optionalData1").asText());
            subscriberObData.setOptionalData2(documentDetailsJson.get("optionalData2").asText());
            subscriberObData.setDocumentNumber(idDocumentNumber);
            subscriberObData.setIssuingState(documentDetailsJson.get("issuingState").asText());
            subscriberObData.setSubscriberSelfie(temporaryTable.getSelfie());
            subscriberObData.setGeoLocation(documentDetailsJson.get("geoLocation").asText());
            subscriberObData.setRemarks(documentDetailsJson.get("remarks").asText());
            subscriberObData.setSubscriberUniqueId(suID);

            subscriberObData.setNiraResponse(temporaryTable.getNiraResponse());
            subscriberObRequestDTO.setSubscriberData(subscriberObData);

            return subscriberObRequestDTO;
        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            return null;
        }

    }

    public FileUploadDTO populateFileUploadDTO(String step2Json, String suID) throws IOException {
        FileUploadDTO videoUploadReq = objectMapper.readValue(step2Json, FileUploadDTO.class);

        FileUploadDTO fileUploadDTO = new FileUploadDTO();
        fileUploadDTO.setSubscriberUid(suID);
        fileUploadDTO.setRecordedTime(videoUploadReq.getRecordedTime());
        fileUploadDTO.setRecordedGeoLocation(videoUploadReq.getRecordedGeoLocation());
        fileUploadDTO.setVerificationFirst(videoUploadReq.getVerificationFirst());
        fileUploadDTO.setVerificationSecond(videoUploadReq.getVerificationSecond());
        fileUploadDTO.setVerificationThird(videoUploadReq.getVerificationThird());
        fileUploadDTO.setTypeOfService(videoUploadReq.getTypeOfService());
        return fileUploadDTO;
    }


    public ApiResponse nextStepDetails(int currentStepId) {
        try {
            int countNoOfSteps = onboardingStepsRepoIface.getNoOfOnboardingSteps();
            if (countNoOfSteps == currentStepId) {
                return AppUtil.createApiResponse(false, "Last Step", countNoOfSteps);
            }
            OnboardingStepDetails onboardingSteps = onboardingStepsRepoIface.getStepDetails(currentStepId + 1);
            return exceptionHandlerUtil.createSuccessResponse("api.response.next.step.details", onboardingSteps);
        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }

    }

    @Override
    public ApiResponse updateRecord(UpdateTemporaryTableDto updateTemporaryTableDto) {
        try {
            logger.info("{}{} - Request for update record: {}", CLASS, Utility.getMethodName(),
                    updateTemporaryTableDto);
            if (updateTemporaryTableDto.getIdDocNumber() == null
                    || updateTemporaryTableDto.getIdDocNumber().isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse(ID_DOC_CANNOT_NULL);
            }
            List<OnboardingStepDetails> onboardingStepDetailsList = onboardingStepsRepoIface.getAllSteps();
            if (updateTemporaryTableDto.getSubscriberDeviceInfoDto() != null) {
                TemporaryTable temporaryTable1 = temporaryTableRepo
                        .getbyidDocNumber(updateTemporaryTableDto.getIdDocNumber());

                if (Objects.isNull(temporaryTable1)) {
                    return exceptionHandlerUtil
                            .createErrorResponse(NO_RECORD_FOUND);
                }

                String deviceDetailsJson = objectMapper
                        .writeValueAsString(updateTemporaryTableDto.getSubscriberDeviceInfoDto());
                JsonNode jsonNode = objectMapper.readTree(deviceDetailsJson);
                String deviceId = jsonNode.get("deviceId").asText();

                temporaryTable1.setDeviceId(deviceId);
                temporaryTable1.setDeviceInfo(deviceDetailsJson);

                TemporaryResponseDto temporaryResponseDto = new TemporaryResponseDto();
                temporaryResponseDto.setIdDocNumber(temporaryTable1.getIdDocNumber());
                SubscriberObDetails subscriberObDetails = objectMapper.readValue(temporaryTable1.getStep1Data(),
                        SubscriberObDetails.class);
                temporaryResponseDto.setSubscriberObDetails(subscriberObDetails);
                temporaryResponseDto.setStep1Status(temporaryTable1.getStep1Status());
                temporaryResponseDto.setStep2Status(temporaryTable1.getStep2Status());
                temporaryResponseDto.setDeviceId(temporaryTable1.getDeviceId());
                SubscriberDeviceInfoDto subscriberDeviceInfoDto = objectMapper
                        .readValue(temporaryTable1.getDeviceInfo(), SubscriberDeviceInfoDto.class);
                temporaryResponseDto.setSubscriberDeviceInfoDto(subscriberDeviceInfoDto);
                temporaryResponseDto.setOptionalData1(temporaryTable1.getOptionalData1());
                temporaryResponseDto.setCreatedOn(temporaryTable1.getCreatedOn());
                temporaryResponseDto.setCreatedOn(temporaryTable1.getCreatedOn());
                temporaryResponseDto.setUpdatedOn(temporaryTable1.getUpdatedOn());
                temporaryResponseDto.setMobileNumber(temporaryTable1.getStep3Data());
                temporaryResponseDto.setStep3Status(temporaryTable1.getStep3Status());

                temporaryResponseDto.setStepCompleted(temporaryTable1.getStepCompleted());
                temporaryResponseDto.setNextStep(temporaryTable1.getNextStep());
                temporaryResponseDto.setStep4Status(temporaryTable1.getStep4Status());
                temporaryResponseDto.setEmailId(temporaryTable1.getStep4Data());
                temporaryResponseDto.setOnboardingStepDetails(onboardingStepDetailsList);

                temporaryTableRepo.save(temporaryTable1);
                return exceptionHandlerUtil.createSuccessResponse("api.response.device.updated.successfully",
                        temporaryResponseDto);
            } else if (updateTemporaryTableDto.getMobileNumber() != null
                    || !updateTemporaryTableDto.getMobileNumber().isEmpty()) {
                TemporaryTable temporaryTable1 = temporaryTableRepo
                        .getbyidDocNumber(updateTemporaryTableDto.getIdDocNumber());

                if (Objects.isNull(temporaryTable1)) {
                    return exceptionHandlerUtil
                            .createErrorResponse(NO_RECORD_FOUND);
                }
                temporaryTable1.setStep3Data(updateTemporaryTableDto.getMobileNumber());

                TemporaryResponseDto temporaryResponseDto = new TemporaryResponseDto();
                temporaryResponseDto.setIdDocNumber(temporaryTable1.getIdDocNumber());
                SubscriberObDetails subscriberObDetails = objectMapper.readValue(temporaryTable1.getStep1Data(),
                        SubscriberObDetails.class);
                temporaryResponseDto.setSubscriberObDetails(subscriberObDetails);
                temporaryResponseDto.setStep1Status(temporaryTable1.getStep1Status());
                temporaryResponseDto.setStep2Status(temporaryTable1.getStep2Status());
                temporaryResponseDto.setDeviceId(temporaryTable1.getDeviceId());
                SubscriberDeviceInfoDto subscriberDeviceInfoDto = objectMapper
                        .readValue(temporaryTable1.getDeviceInfo(), SubscriberDeviceInfoDto.class);
                temporaryResponseDto.setSubscriberDeviceInfoDto(subscriberDeviceInfoDto);
                temporaryResponseDto.setOptionalData1(temporaryTable1.getOptionalData1());
                temporaryResponseDto.setCreatedOn(temporaryTable1.getCreatedOn());
                temporaryResponseDto.setCreatedOn(temporaryTable1.getCreatedOn());
                temporaryResponseDto.setUpdatedOn(temporaryTable1.getUpdatedOn());
                temporaryResponseDto.setMobileNumber(temporaryTable1.getStep3Data());
                temporaryResponseDto.setStep3Status(temporaryTable1.getStep3Status());

                temporaryResponseDto.setStepCompleted(temporaryTable1.getStepCompleted());
                temporaryResponseDto.setStep4Status(temporaryTable1.getStep4Status());
                temporaryResponseDto.setEmailId(temporaryTable1.getStep4Data());
                temporaryResponseDto.setOnboardingStepDetails(onboardingStepDetailsList);

                temporaryTableRepo.save(temporaryTable1);
                return exceptionHandlerUtil.createSuccessResponse("api.response.mobile.number.updated.successfully",
                        temporaryResponseDto);
            } else if (updateTemporaryTableDto.getEmailId() != null
                    || !updateTemporaryTableDto.getEmailId().isEmpty()) {
                TemporaryTable temporaryTable1 = temporaryTableRepo
                        .getbyidDocNumber(updateTemporaryTableDto.getIdDocNumber());

                if (Objects.isNull(temporaryTable1)) {
                    return exceptionHandlerUtil
                            .createErrorResponse(NO_RECORD_FOUND);

                }
                temporaryTable1.setStep4Data(updateTemporaryTableDto.getEmailId());

                TemporaryResponseDto temporaryResponseDto = new TemporaryResponseDto();
                temporaryResponseDto.setIdDocNumber(temporaryTable1.getIdDocNumber());
                SubscriberObDetails subscriberObDetails = objectMapper.readValue(temporaryTable1.getStep1Data(),
                        SubscriberObDetails.class);
                temporaryResponseDto.setSubscriberObDetails(subscriberObDetails);
                temporaryResponseDto.setStep1Status(temporaryTable1.getStep1Status());
                temporaryResponseDto.setStep2Status(temporaryTable1.getStep2Status());
                temporaryResponseDto.setDeviceId(temporaryTable1.getDeviceId());
                SubscriberDeviceInfoDto subscriberDeviceInfoDto = objectMapper
                        .readValue(temporaryTable1.getDeviceInfo(), SubscriberDeviceInfoDto.class);
                temporaryResponseDto.setSubscriberDeviceInfoDto(subscriberDeviceInfoDto);
                temporaryResponseDto.setOptionalData1(temporaryTable1.getOptionalData1());
                temporaryResponseDto.setCreatedOn(temporaryTable1.getCreatedOn());
                temporaryResponseDto.setCreatedOn(temporaryTable1.getCreatedOn());
                temporaryResponseDto.setUpdatedOn(temporaryTable1.getUpdatedOn());
                temporaryResponseDto.setMobileNumber(temporaryTable1.getStep3Data());
                temporaryResponseDto.setStep3Status(temporaryTable1.getStep3Status());

                temporaryResponseDto.setStepCompleted(temporaryTable1.getStepCompleted());
                temporaryResponseDto.setStep4Status(temporaryTable1.getStep4Status());
                temporaryResponseDto.setEmailId(temporaryTable1.getStep4Data());
                temporaryResponseDto.setOnboardingStepDetails(onboardingStepDetailsList);

                temporaryTableRepo.save(temporaryTable1);
                return exceptionHandlerUtil.createSuccessResponse("api.response.email.id.updated.successfully",
                        temporaryResponseDto);
            }
            return exceptionHandlerUtil.createErrorResponse("api.error.update.record.type.not.valid");
        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            logger.error("{}{} - Exception for update record: {}", CLASS, Utility.getMethodName(), e);
            return exceptionHandlerUtil.handleException(e);

        }
    }

    @Override
    public ApiResponse deleteRecord(UpdateTemporaryTableDto dto) {
        try {
            ApiResponse response;

            response = deleteByMobileNumber(dto.getMobileNumber());
            if (response != null) return response;

            response = deleteByEmail(dto.getEmailId());
            if (response != null) return response;

            response = deleteByDeviceId(dto.getDeviceId());
            if (response != null) return response;

            return exceptionHandlerUtil.createErrorResponse(
                    "api.error.something.went.wrong.please.try.after.sometime");

        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    // Helper methods to reduce duplication
    private ApiResponse deleteByMobileNumber(String mobileNumber) {
        if (mobileNumber == null) return null;
        TemporaryTable table = temporaryTableRepo.getByMobNumber(mobileNumber);
        if (table == null) return null;

        int deleted = temporaryTableRepo.deleteRecord(mobileNumber, null, null);
        if (deleted != 1) {
            return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_RECORD_CANNOT_DELETED_USING_DEVICEID);
        }
        return exceptionHandlerUtil.successResponse(TEMP_TABLE_RECORD_DELETED);
    }

    private ApiResponse deleteByEmail(String emailId) {
        if (emailId == null) return null;
        TemporaryTable table = temporaryTableRepo.getByEmail(emailId);
        if (table == null) return exceptionHandlerUtil.createErrorResponse(
                "api.error.there.is.no.record.with.this.email.id");

        int deleted = temporaryTableRepo.deleteRecord(null, emailId, null);
        if (deleted != 1) {
            return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_RECORD_CANNOT_DELETED_USING_DEVICEID);
        }
        return exceptionHandlerUtil.successResponse(TEMP_TABLE_RECORD_DELETED);
    }

    private ApiResponse deleteByDeviceId(String deviceId) {
        if (deviceId == null) return null;
        TemporaryTable table = temporaryTableRepo.getByDevice(deviceId);
        if (table == null) return exceptionHandlerUtil.createErrorResponse(
                "api.error.There.is.no.record.with.this.device.id");

        int deleted = temporaryTableRepo.deleteRecord(null, null, deviceId);
        if (deleted != 1) {
            return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_RECORD_CANNOT_DELETED_USING_DEVICEID);
        }
        return exceptionHandlerUtil.successResponse(TEMP_TABLE_RECORD_DELETED);
    }


    @Override
    public ApiResponse saveStep2Details(TemporaryTableDTO temporaryTableDTO, MultipartFile livelinessVideo,
                                        String selfie) {
        try {
            if (Objects.isNull(temporaryTableDTO)) {
                return exceptionHandlerUtil.createErrorResponse(TEMP_TABLE_DTO_CANNOT_NULL);
            }
            if (temporaryTableDTO.getIdDocNumber() == null || temporaryTableDTO.getIdDocNumber().isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse(ID_DOC_CANNOT_NULL);
            }

            // Single method invocation replaces if-else
            return processStep2(temporaryTableDTO, livelinessVideo, selfie);

        } catch (Exception e) {
            logger.error("{}{} - saveStep2Details Exception: {}", CLASS, Utility.getMethodName(), e.getMessage());
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private ApiResponse processStep2(TemporaryTableDTO temporaryTableDTO, MultipartFile livelinessVideo, String selfie) {
        if (temporaryTableDTO.getStep() != 2) {
            return exceptionHandlerUtil.createErrorResponse("api.error.step.not.found");
        }

        ApiResponse response = flag2method(temporaryTableDTO, livelinessVideo, selfie);
        return AppUtil.createApiResponse(response.isSuccess(), response.getMessage(), response.getResult());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void deleteOldRecords() {
        try {
            logger.info("{}{} - deleteOldRecords corn job Started: {}", CLASS, Utility.getMethodName());
            List<TemporaryTable> records = temporaryTableRepo.findAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            for (TemporaryTable tempRecord : records) {
                if (tempRecord.getUpdatedOn() != null) {
                    LocalDateTime updatedOn = LocalDateTime.parse(tempRecord.getUpdatedOn(), formatter);
                    LocalDateTime threshold = now.minusHours(24);
                    if (updatedOn.isBefore(threshold) || updatedOn.isEqual(threshold)) {
                        temporaryTableRepo.deleteRecordByIdDocumentNumber(tempRecord.getIdDocNumber());
                    }
                }

            }
            logger.info("{}{} - deleteOldRecords record deleted: {}", CLASS, Utility.getMethodName());
        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            logger.error("{}{} - deleteOldRecords Exception: {}", CLASS, Utility.getMethodName(), e.getMessage());
        }
    }

    // Api to save face features into database in photo_features table
    @Override
    public ApiResponse getAllSubscriberExtractFeatures() {
        try {
            List<SubscriberOnboardingData> subscriberOnboardingDataList = subscriberOnboardingDataRepoIface
                    .getAllSelfies();

            for (SubscriberOnboardingData subscriberOnboardingData : subscriberOnboardingDataList) {
                ApiResponse result = processSubscriberFeatures(subscriberOnboardingData);
                if (!result.isSuccess()) {
                    return result;
                }
            }
            return exceptionHandlerUtil.createSuccessResponse("api.response.features.extracted", null);
        } catch (Exception e) {
            logger.error(CLASS + " getAllSubscriberExtractFeatures Exception {}", e.getMessage());
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private ApiResponse processSubscriberFeatures(SubscriberOnboardingData subscriberOnboardingData) {
        try {
            ApiResponse response = externalEdmsApi(subscriberOnboardingData.getSelfieUri());
            ApiResponse response1 = extractFeatchersPython(response.getResult().toString());

            if (!response1.isSuccess()) {
                return exceptionHandlerUtil
                        .createErrorResponse("api.error.response.from.facefeature.python.api.is.negative");
            }

            String featuresBase64 = response1.getResult().toString();
            byte[] decodedData = Base64.getDecoder().decode(featuresBase64);
            Blob blob = new SerialBlob(decodedData);

            PhotoFeaturesModel photoFeatures = new PhotoFeaturesModel();
            photoFeatures.setSuid(subscriberOnboardingData.getSubscriberUid());
            photoFeatures.setPhotoFeatures(blob);
            photoFeatures.setCreatedOn(AppUtil.getDate());
            photoFeatures.setUpdatedOn(AppUtil.getDate());
            photoFeaturesRepo.save(photoFeatures);

            return exceptionHandlerUtil.successResponse(null);
        } catch (Exception e) {
            logger.error(CLASS + " processSubscriberFeatures Exception {}", e.getMessage());
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    // Python api called to fetch face featues
    public ApiResponse extractFeatchersPython(String subscriberPhoto) {
        try {
            HttpHeaders headers = new HttpHeaders();

            ExtractFeatureInputDto extractFeatureInputDto = new ExtractFeatureInputDto();
            extractFeatureInputDto.setSubscriberPhoto(subscriberPhoto);
            HttpEntity<Object> request = new HttpEntity<>(extractFeatureInputDto, headers);

            ResponseEntity<ApiResponse> response = restTemplate.exchange(exractFeatures, HttpMethod.POST, request,
                    ApiResponse.class);

            if (!response.getBody().isSuccess()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.Extract.feature.python.api.failed");
            }
            return exceptionHandlerUtil.createSuccessResponse("api.response.features.extracted.successfully",
                    response.getBody().getResult());

        } catch (Exception e) {
            logger.error(CLASS + " extractFeatchersPython Exception {}", e.getMessage());
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleHttpException(e);
        }

    }

    // api to fetch subscriber selfie base 64 from edms
    public ApiResponse externalEdmsApi(String edmsUrl) {
        try {
            return fetchSelfieFromEdms(edmsUrl);
        } catch (Exception e) {
            logger.error("{}{} - externalEdmsApi Exception: {}", CLASS, Utility.getMethodName(), e.getMessage());
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private ApiResponse fetchSelfieFromEdms(String edmsUrl) {
        try {
            logger.info("{}{} - externalEdmsApi request: {}", CLASS, Utility.getMethodName(), edmsUrl);
            validateUrl(edmsUrl);

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Object> request = new HttpEntity<>(headers);
            ResponseEntity<byte[]> resp = restTemplate.exchange(edmsUrl, HttpMethod.GET, request, byte[].class);

            if (resp.getStatusCode() == HttpStatus.OK) {
                String selfieBase64 = AppUtil.getBase64FromByteArr(resp.getBody());
                return exceptionHandlerUtil.createSuccessResponse("api.response.edms.selfie", selfieBase64);
            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.edms.selfie.fetchednot.fetched");
            }
        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleHttpException(e);
        }
    }

    public ApiResponse verifyFaceFeatures(String selfieBase64) {
        try {
            ApiResponse response1 = findDetails(selfieBase64);
            if (!response1.isSuccess()) {
                return exceptionHandlerUtil.createFailedResponseWithCustomMessage(response1.getMessage(),
                        response1.getResult());
            }
            return exceptionHandlerUtil.createSuccessResponseWithCustomMessage(response1.getMessage(),
                    response1.getResult());

        } catch (Exception e) {
            logger.error("{}{} - Exception occurred in verifyFaceFeatures: {}", CLASS, Utility.getMethodName(),
                    e.getMessage(), e);
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    public ApiResponse findDetails(String subscriberPhoto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            ExtractFeatureInputDto extractFeatureInputDto = new ExtractFeatureInputDto();
            extractFeatureInputDto.setImage(subscriberPhoto);
            HttpEntity<Object> request = new HttpEntity<>(extractFeatureInputDto, headers);

            ResponseEntity<ApiResponse> response = restTemplate.exchange(findDetails, HttpMethod.POST, request,
                    ApiResponse.class);

            if (!response.getBody().isSuccess()) {
                return exceptionHandlerUtil.createFailedResponseWithCustomMessage(response.getBody().getMessage(),
                        response.getBody().getResult());
            }
            return exceptionHandlerUtil.createSuccessResponseWithCustomMessage(response.getBody().getMessage(),
                    response.getBody().getResult());
        } catch (Exception e) {
            logger.error("{}{} - Exception occurred in extractFeatchersPython: {}", CLASS, Utility.getMethodName(),
                    e.getMessage(), e);
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleHttpException(e);
        }

    }

    @Override
    public ApiResponse encriptedString(TemporaryTableDTO temporaryTableDTO) {
        try {
            Result r = DAESService.createSecureWireData(temporaryTableDTO.getNiraResponse().toString());
            String decryptedString = new String(r.getResponse());
            Result result = DAESService.decryptSecureWireData(decryptedString);
            String ss = new String(result.getResponse());
            return exceptionHandlerUtil.createSuccessResponseWithCustomMessage("Success", ss);

        } catch (Exception e) {
            logger.error(CLASS + "extractFeatchersPython Exception {}", e.getMessage());
            logger.error(UNEXPECTED_EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }


    private void validateUrl(String url) throws InvalidUrlException {
        try {
            if (url == null || url.trim().isEmpty()) {
                throw new InvalidUrlException("URL cannot be null or empty");
            }

            URI uri = new URI(url);

            // 1️⃣ Allow only HTTPS
            if (!"https".equalsIgnoreCase(uri.getScheme())) {
                throw new InvalidUrlException("Only HTTPS protocol is allowed");
            }

            // 2️⃣ Strict host validation
            String allowedHost = "internal-edms.company.com"; // replace with actual host
            if (!allowedHost.equalsIgnoreCase(uri.getHost())) {
                throw new InvalidUrlException("Unauthorized host detected: " + uri.getHost());
            }

            // 3️⃣ Block private/internal IP resolution
            InetAddress address = InetAddress.getByName(uri.getHost());
            if (address.isAnyLocalAddress() ||
                    address.isLoopbackAddress() ||
                    address.isSiteLocalAddress()) {
                throw new InvalidUrlException("Access to internal/private IPs is not allowed");
            }

        } catch (URISyntaxException | UnknownHostException e) {
            throw new InvalidUrlException("URL validation failed: " + url, e);
        }
    }
}
