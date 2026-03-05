package ug.daes.onboarding.service.impl;


import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.util.InvalidUrlException;
import ug.daes.DAESService;
import ug.daes.Result;
import ug.daes.onboarding.config.SentryClientExceptions;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.EmailReqDto;
import ug.daes.onboarding.dto.MobileOTPDto;
import ug.daes.onboarding.dto.OTPResponseDTO;
import ug.daes.onboarding.dto.SmsDTO;
import ug.daes.onboarding.dto.SmsOtpResponseDTO;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;

import ug.daes.onboarding.service.iface.OtpServiceIface;
import ug.daes.onboarding.util.AppUtil;

@Service
public class OtpServiceImpl implements OtpServiceIface {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
    private static final String MOBILE_NUMBER = "MobileNumber : ";
    private static final String EMAIL_ID = " | EmailId :";
    private static final String PHONE_NUMBER_IS_INVALID = "api.error.phone.number.is.invalid.please.enter.correct.phone.number";
    private static final String OTP_MOBILE_SMS = "sendOTPMobileSms";
    private static final String OTP_CONTROLLER = "OTPController";
    private static final String DEVICE_ID = " | DeviceId : ";
    private static final String OTP_STATUS = " | OtpStatus : ";
    private static final String EXCEPTION = "Unexpected exception";
    private static final String REGISTARTION_OTP_SENT = "REGISTRATION_OTP_SENT";


    private static final String CLASS = "OtpServiceImpl";

    private final String niraApiToken;
    private final String niraApiSMS;
    private final String niraUserName;
    private final String niraPassword;
    private final String indApiSMS;
    private final int timeToLive;

    private final String emailBaseUrl;

    private final RestTemplate restTemplate;
    private final LogModelServiceImpl logModelServiceImpl;
    private final SentryClientExceptions sentryClientExceptions;
    private final ExceptionHandlerUtil exceptionHandlerUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String uaeApiSMS;


    public OtpServiceImpl(
            @Value("${nira.api.token}") String niraApiToken,
            @Value("${nira.api.sms}") String niraApiSMS,
            @Value("${nira.username}") String niraUserName,
            @Value("${nira.password}") String niraPassword,
            @Value("${ind.api.sms}") String indApiSMS,
            @Value("${spring.mail.username}") String mailUserName,
            @Value("${nira.api.timetolive}") int timeToLive,
            @Value("${config.validation.allowTrustedUsersOnly}") int allowTrustedUsersOnly,
            @Value("${email.url}") String emailBaseUrl,
            RestTemplate restTemplate,
            LogModelServiceImpl logModelServiceImpl,
            SentryClientExceptions sentryClientExceptions,
            ExceptionHandlerUtil exceptionHandlerUtil
    ) {
        this.niraApiToken = niraApiToken;
        this.niraApiSMS = niraApiSMS;
        this.niraUserName = niraUserName;
        this.niraPassword = niraPassword;
        this.indApiSMS = indApiSMS;

        this.timeToLive = timeToLive;

        this.emailBaseUrl = emailBaseUrl;
        this.restTemplate = restTemplate;
        this.logModelServiceImpl = logModelServiceImpl;
        this.sentryClientExceptions = sentryClientExceptions;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }

    private void validateUrl(String url) throws InvalidUrlException, URISyntaxException, UnknownHostException {
        if (url == null || url.trim().isEmpty()) {
            throw new InvalidUrlException("URL cannot be null or empty");
        }

        URI uri = new URI(url);


        if (!"https".equalsIgnoreCase(uri.getScheme())) {
            throw new SecurityException("Only HTTPS protocol is allowed");
        }


        String allowedHost = "internal-edms.company.com";

        if (!allowedHost.equalsIgnoreCase(uri.getHost())) {
            throw new SecurityException("Unauthorized host detected: " + uri.getHost());
        }


        InetAddress address = InetAddress.getByName(uri.getHost());

        if (address.isAnyLocalAddress() ||
                address.isLoopbackAddress() ||
                address.isSiteLocalAddress()) {

            throw new SecurityException("Access to internal/private IPs is not allowed");
        }
    }

    public String generatecorrelationIdUniqueId() {
        UUID correlationID = UUID.randomUUID();
        return correlationID.toString();
    }


    @Override
    public ApiResponse sendOTPMobileSms(MobileOTPDto mobileOTPDto) throws ParseException, UnknownHostException {
        logger.info(CLASS + "sendOTPMobileSms() >> req {}", mobileOTPDto);

        // Whitelisted numbers
        if ("+256987654321".equals(mobileOTPDto.getSubscriberMobileNumber()) ||
                "+256123456789".equals(mobileOTPDto.getSubscriberMobileNumber())) {
            return verifyOtp(mobileOTPDto.getSubscriberMobileNumber());
        }

        Date startTime = new Date();
        String correlationId = generatecorrelationIdUniqueId();
        OTPResponseDTO otpResponse = new OTPResponseDTO();

        String mobileOTP = generateOtp(6);
        String emailOTP = generateOtp(5);


        String msisdn = mobileOTPDto.getSubscriberMobileNumber();

        ApiResponse apiResponse;
        try {
            if (msisdn.startsWith("+91")) {
                apiResponse = sendSmsToIND(mobileOTPDto, mobileOTP, correlationId);
                if (!apiResponse.isSuccess()) return apiResponse;

            } else if (msisdn.startsWith("+256")) {
                apiResponse = sendSmsToUGA(mobileOTPDto, mobileOTP, correlationId, startTime);
                if (!apiResponse.isSuccess()) return apiResponse;

            } else if (msisdn.startsWith("+971")) {
                apiResponse = sendSmsToUAE(mobileOTPDto, mobileOTP, correlationId);
                if (!apiResponse.isSuccess()) return apiResponse;

            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.invalid.country.code");
            }

            // Set OTP response
            otpResponse.setMobileOTP(null);
            otpResponse.setEmailOTP(null);
            otpResponse.setTtl(timeToLive);
            otpResponse.setMobileEncrptyOTP(encryptedString(mobileOTP));
            otpResponse.setEmailEncrptyOTP(encryptedString(emailOTP));

            return sendOtpEmail(mobileOTPDto, emailOTP, otpResponse, startTime, correlationId);

        } catch (Exception e) {
            logger.error(CLASS + "SendOTPMobileSms >> Exception {}", e.getMessage(), e);
            sentryClientExceptions.captureTags(mobileOTPDto.getSuID(), msisdn, OTP_MOBILE_SMS, OTP_CONTROLLER);
            sentryClientExceptions.captureExceptions(e);
            double totalTime = AppUtil.getDifferenceInSeconds(startTime, new Date());
            logModelServiceImpl.setLogModel(true, encryptedString(mobileOTPDto.getSubscriberEmail()), null,
                    REGISTARTION_OTP_SENT, correlationId, String.valueOf(totalTime), startTime, new Date(), e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }


    private ApiResponse sendSmsToIND(MobileOTPDto dto, String mobileOTP, String correlationId) throws UnknownHostException, ParseException {
        if (dto.getSubscriberMobileNumber().length() != 13) {
            return exceptionHandlerUtil.createErrorResponse(PHONE_NUMBER_IS_INVALID);
        }
        ApiResponse apiResponse = sendSMSIND(mobileOTP, dto.getSubscriberMobileNumber().substring(3));
        if (!apiResponse.isSuccess()) {
            logOtpFailure(dto, correlationId, apiResponse.getMessage());
        }
        return apiResponse;
    }

    private ApiResponse sendSmsToUGA(MobileOTPDto dto, String mobileOTP, String correlationId, Date startTime) throws Exception {
        if (dto.getSubscriberMobileNumber().length() != 13) {
            return exceptionHandlerUtil.createErrorResponse(PHONE_NUMBER_IS_INVALID);
        }
        ApiResponse response = sendSMSUGA(mobileOTP, dto.getSubscriberMobileNumber(), timeToLive);
        SmsOtpResponseDTO smsResp = objectMapper.readValue(response.getResult().toString(), SmsOtpResponseDTO.class);
        if (smsResp.getNonFieldErrors() != null) {
            logOtpFailure(dto, correlationId, smsResp.getNonFieldErrors().get(0));
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage(smsResp.getNonFieldErrors().get(0), null);
        }
        logOtpSuccess(dto, correlationId, startTime);
        return exceptionHandlerUtil.successResponse("UGA SMS Sent");
    }

    private ApiResponse sendSmsToUAE(MobileOTPDto dto, String mobileOTP, String correlationId) throws Exception {
        if (dto.getSubscriberMobileNumber().length() != 13) {
            return exceptionHandlerUtil.createErrorResponse(PHONE_NUMBER_IS_INVALID);
        }
        Object obj = sendSMSUAE(mobileOTP, dto.getSubscriberMobileNumber(), timeToLive);
        LinkedHashMap<String, String> smsResp = objectMapper.readValue(
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj), LinkedHashMap.class);
        if ("406".equals(smsResp.get("code"))) {
            logOtpFailure(dto, correlationId, "Invalid number: " + smsResp.get("code"));
            return exceptionHandlerUtil.createErrorResponse("api.error.invalid.number");
        }
        return exceptionHandlerUtil.successResponse("UAE SMS Sent");
    }

    private ApiResponse sendOtpEmail(MobileOTPDto dto, String emailOTP, OTPResponseDTO otpResponse, Date startTime, String correlationId) throws Exception {
        EmailReqDto emailDto = new EmailReqDto();
        emailDto.setEmailOtp(emailOTP);
        emailDto.setEmailId(dto.getSubscriberEmail());
        emailDto.setTtl(timeToLive);

        ApiResponse res = sendEmailToSubscriber(emailDto);
        if (res.isSuccess()) {
            double totalTime = AppUtil.getDifferenceInSeconds(startTime, new Date());
            logModelServiceImpl.setLogModel(true, encryptedString(dto.getSubscriberEmail()), null,
                    REGISTARTION_OTP_SENT, correlationId, String.valueOf(totalTime), startTime, new Date(),
                    "Email sent successfully");
            return exceptionHandlerUtil.createSuccessResponseWithCustomMessage("api.response.email.sent", otpResponse);
        } else {
            return exceptionHandlerUtil.createErrorResponse("api.error.something.went.wrong.please.try.after.sometime");
        }
    }

    private void logOtpFailure(MobileOTPDto dto, String correlationId, String message) throws ParseException {
        String logMsg = MOBILE_NUMBER + dto.getSubscriberMobileNumber() + OTP_STATUS + message
                + EMAIL_ID + dto.getSubscriberEmail() + DEVICE_ID + dto.getDeviceId();
        logModelServiceImpl.setLogModel(false, encryptedString(dto.getSubscriberEmail()), null,
                REGISTARTION_OTP_SENT, correlationId, null, null, null, logMsg);
    }

    private void logOtpSuccess(MobileOTPDto dto, String correlationId, Date startTime) throws ParseException {
        String logMsg = MOBILE_NUMBER + dto.getSubscriberMobileNumber() + " OtpStatus : done"
                + EMAIL_ID + dto.getSubscriberEmail() + DEVICE_ID + dto.getDeviceId();
        double totalTime = AppUtil.getDifferenceInSeconds(startTime, new Date());
        logModelServiceImpl.setLogModel(true, encryptedString(dto.getSubscriberEmail()), null,
                REGISTARTION_OTP_SENT, correlationId, String.valueOf(totalTime), startTime, new Date(), logMsg);
    }

    public ApiResponse sendEmailToSubscriber(EmailReqDto emailReqDto) throws UnknownHostException {
        try {
            String url = emailBaseUrl;
            validateUrl(url);
            logger.info(" emailReqDto {}", emailReqDto);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> requestEntity = new HttpEntity<>(emailReqDto, headers);
            logger.info("requestEntity >> {}", requestEntity);
            ResponseEntity<ApiResponse> res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ApiResponse.class);
            logger.info("res >> {}", res);
            if (res.getStatusCodeValue() == 200) {
                return exceptionHandlerUtil.createSuccessResponse("api.response.email.sent", res);

            } else if (res.getStatusCodeValue() == 400) {
                return exceptionHandlerUtil.createErrorResponse("api.error.bad.request");

            } else if (res.getStatusCodeValue() == 500) {
                return exceptionHandlerUtil.createErrorResponse("api.error.internal.server.error");

            }
            return exceptionHandlerUtil.createErrorResponse("api.error.something.went.wrong.please.try.after.sometime");
        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            sentryClientExceptions.captureTags(null, emailReqDto.getEmailId(), "sendEmailToSubscriber", OTP_CONTROLLER);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleHttpException(e);

        }

    }


    private ApiResponse sendSMSIND(String otp, String mobileNumber) throws UnknownHostException {
        logger.info(CLASS + "sendSMSIND >> req >> otp {} and  mobileNumber {}", otp, mobileNumber);
        String smsBody = "Dear Subscriber, " + otp + " is your DigitalTrust Mobile verification one-time code";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String smsUrlWithBody = indApiSMS
                + "?APIKey=E2X4Ixz65kKlawWUBVUKkA&senderid=DGTRST&channel=2&DCS=0&flashsms=0&number=" + mobileNumber
                + "&text=" + smsBody + "&route=1&dlttemplateid=1307162619898313468";

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        try {

            logger.info(CLASS + "sendSMSIND >> req for restTemplate >> smsUrlWithBody {} and requestEntity {}", smsUrlWithBody, requestEntity);

            ResponseEntity<Object> res = restTemplate.exchange(smsUrlWithBody, HttpMethod.GET, requestEntity,
                    Object.class);
            String smsResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(res.getBody());
            LinkedHashMap<String, String> indiaSmsOtpResponse = objectMapper.readValue(smsResponse,
                    LinkedHashMap.class);
            if ("000".equals(indiaSmsOtpResponse.get("ErrorCode"))) {
                logger.info("{} sendSMSIND >> res for restTemplate >> {}", CLASS, indiaSmsOtpResponse);
                return exceptionHandlerUtil.createSuccessResponseWithCustomMessage(indiaSmsOtpResponse.get("ErrorMessage"), null);

            } else {
                return exceptionHandlerUtil.createFailedResponseWithCustomMessage(indiaSmsOtpResponse.get("ErrorMessage"), null);

            }
        } catch (Exception e) {
            logger.error(CLASS + "sendSMSIND() >> Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            sentryClientExceptions.captureTags(null, mobileNumber, "sendSMSIND", OTP_CONTROLLER);
            sentryClientExceptions.captureExceptions(e);
            return exceptionHandlerUtil.handleHttpException(e);

        }
    }

    public ApiResponse sendSMSUGA(String otp, String mobileNumber, int timeToLive) throws ParseException {
        logger.info("sendSMSUGA() >> otp {} and mobileNumber {} and timeToLive {}", otp, mobileNumber, timeToLive);
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
            logger.info("sendSMSUGA() >> req for restTemplate >> url {} and requestEntity {}", url, requestEntity);
            ResponseEntity<ApiResponse> res = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    ApiResponse.class);
            ApiResponse api = res.getBody();
            logger.info("sendSMSUGA() >> res for restTemplate {}", res);
            return api;
        } catch (Exception e) {
            logger.error(CLASS + "sendSMSUGA() >> Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleHttpException(e);

        }
    }

    public Object sendSMSUAE(String otp, String mobileNumber, int timeToLive) throws ParseException {
        logger.info("sendSMSUAE() >> otp {} and mobileNumber {} and timeToLive{} ", otp, mobileNumber, timeToLive);
        String url = uaeApiSMS;
        String text = "Your ICA-Pass OTP Phone verification code  is " + otp + "The code is valid for " + timeToLive
                + " seconds. Don't share this code with anyone.";

        Map<String, String> uaeSmsBody = new HashMap<>();
        uaeSmsBody.put("mobileno", mobileNumber);
        uaeSmsBody.put("smstext", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.info("getToken() :: {}", getToken());
        headers.set("access_token", getToken());
        HttpEntity<Object> requestEntity = new HttpEntity<>(uaeSmsBody, headers);
        try {
            logger.info("sendSMSUAE() >> req for restTemplate >> url {} and requestEntity {}", url, requestEntity);
            ResponseEntity<Object> res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Object.class);
            ApiResponse api = new ApiResponse();
            api.setSuccess(true);
            api.setMessage("");
            api.setResult(res.getBody());
            logger.info("sendSMSUAE() >> res for restTemplate {}", res);
            return api.getResult();
        } catch (Exception e) {
            logger.error("sendSMSUAE >> Exception >> {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleHttpException(e);

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
            logger.error(EXCEPTION, e);
            return null;
        }
    }

    public String getBasicAuth() {
        String userCredentials = niraUserName + ":" + niraPassword;
        return new String(Base64.getEncoder().encode(userCredentials.getBytes()));
    }

    private String encryptedString(String s) {
        try {
            Result result = DAESService.encryptData(s);
            return new String(result.getResponse());
        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            return e.getMessage();
        }
    }

    public String getToken() {
        String url = niraApiToken;
        logger.info("getToken() >> req >> url {} ", url);
        String basicAuth = getBasicAuth();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("daes-authorization", basicAuth);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        try {
            logger.info("getToken() >> req for restTemplate {} ", requestEntity);
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            logger.info("getToken() >> res for restTemplate {}", res);
            return res.getBody();
        } catch (Exception e) {
            logger.error(CLASS + "getToken() >> Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return e.getMessage();
        }

    }

    public ApiResponse verifyOtp(String mobNo) {
        ApiResponse apiResponse = new ApiResponse();

        OTPResponseDTO otpResponse = new OTPResponseDTO();
        otpResponse.setEmailEncrptyOTP(AppUtil.encryptedString("12345"));
        otpResponse.setMobileEncrptyOTP(AppUtil.encryptedString("123456"));
        otpResponse.setTtl(180);

        apiResponse.setMessage("Otp verfication done");
        apiResponse.setSuccess(true);
        apiResponse.setResult(otpResponse);
        return apiResponse;

    }

    @Override
    public ApiResponse sendEmail(MobileOTPDto mobileOTPDto) {

        return null;
    }


}
