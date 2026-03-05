package ug.daes.onboarding.service.impl;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.EmailReqDto;

import ug.daes.onboarding.dto.NotificationContextDTO;
import ug.daes.onboarding.dto.NotificationDTO;
import ug.daes.onboarding.dto.NotificationDataDTO;
import ug.daes.onboarding.dto.SmsDTO;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.Subscriber;
import ug.daes.onboarding.model.SubscriberFcmToken;
import ug.daes.onboarding.repository.SubscriberFcmTokenRepoIface;
import ug.daes.onboarding.repository.SubscriberRepoIface;
import ug.daes.onboarding.service.iface.TestOTPServiceIface;
import ug.daes.onboarding.util.AppUtil;

@Service
public class TestOTPServiceImpl implements TestOTPServiceIface {
    private static final Logger logger = LoggerFactory.getLogger(TestOTPServiceImpl.class);

    private static final String ERROR = "api.error.something.went.wrong.please.try.after.sometime";
    private static final String EXCEPTION = "Unexpected exception";
    private final String mobileno;
    private final String email;
    private final String niraUserName;
    private final String niraPassword;
    private final String niraApiSMS;
    private final String niraApiToken;
    private final int timeToLive;
    private final String emailBaseUrl;
    private final String notificationUrl;


    private final MessageSource messageSource;

    private final RestTemplate restTemplate;
    private final SubscriberRepoIface subscriberRepoIface;
    private final SubscriberFcmTokenRepoIface subscriberFcmTokenRepoIface;
    private final ExceptionHandlerUtil exceptionHandlerUtil;


    public TestOTPServiceImpl(
            @Value("${test.mobile.otp}") String mobileno,
            @Value("${test.email.otp}") String email,
            @Value("${nira.username}") String niraUserName,
            @Value("${nira.password}") String niraPassword,
            @Value("${nira.api.sms}") String niraApiSMS,
            @Value("${nira.api.token}") String niraApiToken,
            @Value("${nira.api.timetolive}") int timeToLive,
            @Value("${email.url}") String emailBaseUrl,
            @Value("${onboarding.notificationurl}") String notificationUrl,
            MessageSource messageSource,

            RestTemplate restTemplate,
            SubscriberRepoIface subscriberRepoIface,
            SubscriberFcmTokenRepoIface subscriberFcmTokenRepoIface,
            ExceptionHandlerUtil exceptionHandlerUtil
    ) {
        this.mobileno = mobileno;
        this.email = email;
        this.niraUserName = niraUserName;
        this.niraPassword = niraPassword;
        this.niraApiSMS = niraApiSMS;
        this.niraApiToken = niraApiToken;
        this.timeToLive = timeToLive;
        this.emailBaseUrl = emailBaseUrl;
        this.notificationUrl = notificationUrl;

        this.messageSource = messageSource;

        this.restTemplate = restTemplate;
        this.subscriberRepoIface = subscriberRepoIface;
        this.subscriberFcmTokenRepoIface = subscriberFcmTokenRepoIface;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }

    @Override
    public ApiResponse testMobileOtpService() {
        try {
            String mobileOTP = generateOtp(6);
            if (mobileno.startsWith("+256")) {
                if (mobileno.length() == 13) {
                    ApiResponse response = sendSMSUGA(mobileOTP, mobileno, timeToLive);
                    if (response.isSuccess()) {
                        return exceptionHandlerUtil.createSuccessResponse("api.response.mobile.otp.send.successfully", response.getResult());

                    } else {

                        return exceptionHandlerUtil.createErrorResponse("api.error.otp.send.failed");

                    }

                } else {
                    return exceptionHandlerUtil.createErrorResponse("api.error.phone.number.is.invalid.please.enter.correct.phone.number");

                }
            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.invalid.country.code");

            }
        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.createErrorResponse(ERROR);

        }
    }

    @Override
    public ApiResponse testEmailOtpService() {
        try {
            String emailOTP = generateOtp(5);
            EmailReqDto dto = new EmailReqDto();
            dto.setEmailOtp(emailOTP);
            dto.setEmailId(email);


            ApiResponse res = sendEmailToSubscriber(dto);
            if (res.isSuccess()) {
                logger.info("Email Sent Successfully");
                return exceptionHandlerUtil.createSuccessResponse("api.response.test.email.otp.sent.successfully", null);

            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.otp.send.failed");
            }
        } catch (Exception e) {
            logger.error(EXCEPTION, e);

            return exceptionHandlerUtil.createErrorResponse(ERROR);

        }
    }

    public ApiResponse sendSMSUGA(String otp, String mobileNumber, int timeToLive) throws ParseException {
        String url = niraApiSMS;
        String basicAuth = getBasicAuth();
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setPhoneNumber(mobileNumber);


        smsDTO.setSmsText("Dear Customer, Test OTP for UgPass Registration is " + otp
                + "- UgPass System");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("daes-authorization", basicAuth);
        headers.set("access_token", getToken());
        HttpEntity<Object> requestEntity = new HttpEntity<>(smsDTO, headers);
        try {
            ResponseEntity<ApiResponse> res = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    ApiResponse.class);
            return res.getBody();
        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR, null, Locale.ENGLISH), null);
        }
    }

    public String getBasicAuth() {
        String userCredentials = niraUserName + ":" + niraPassword;
        return new String(Base64.getEncoder().encode(userCredentials.getBytes()));
    }

    public String getToken() {
        String url = niraApiToken;
        String basicAuth = getBasicAuth();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("daes-authorization", basicAuth);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        try {

            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            return res.getBody();
        } catch (Exception e) {

            logger.error(EXCEPTION, e);
            return e.getMessage();
        }

    }

    public ApiResponse sendEmailToSubscriber(EmailReqDto emailReqDto) {
        try {
            String url = emailBaseUrl;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> requestEntity = new HttpEntity<>(emailReqDto, headers);
            logger.info("requestEntity >> {}", requestEntity);
            ResponseEntity<ApiResponse> res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ApiResponse.class);
            logger.info("res >> {}", res);
            if (res.getStatusCodeValue() == 200) {
                return AppUtil.createApiResponse(true, res.getBody().getMessage(), res.getBody());
            } else if (res.getStatusCodeValue() == 400) {
                return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.bad.request", null, Locale.ENGLISH), null);
            } else if (res.getStatusCodeValue() == 500) {
                return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.internal.server.error", null, Locale.ENGLISH), null);
            }
            return AppUtil.createApiResponse(false, res.getBody().getMessage(), null);
        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR, null, Locale.ENGLISH), null);
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


    @Override
    public ApiResponse testSendNotification() {
        try {
            Map<String, String> paymentStatus = new HashMap<>();
            Map<String, String> paymentTransactionId = new HashMap<>();

            Subscriber subscribers = subscriberRepoIface.findFCMTokenByMobileEamil(mobileno, email);
            SubscriberFcmToken subscriberFcmToken = subscriberFcmTokenRepoIface.findBysubscriberUid(subscribers.getSubscriberUid());

            paymentStatus.put("PaymentStatus", "");
            paymentStatus.put("PaymentCategory", "");
            paymentTransactionId.put("PaymentTransactionId", "");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            NotificationDTO notificationBody = new NotificationDTO();
            NotificationDataDTO dataDTO = new NotificationDataDTO();
            NotificationContextDTO contextDTO = new NotificationContextDTO();
            notificationBody.setTo(subscriberFcmToken.getFcmToken());
            notificationBody.setPriority("high");
            dataDTO.setTitle("Hi ");
            dataDTO.setBody("Test");
            contextDTO.setPrefPaymentStatus(paymentStatus);
            contextDTO.setPrefTransactionId(paymentTransactionId);
            dataDTO.setNotificationContext(contextDTO);
            notificationBody.setData(dataDTO);

            HttpEntity<Object> requestEntity = new HttpEntity<>(notificationBody, headers);
            logger.info("RequestToken :{}", requestEntity);

            ResponseEntity<Object> res = restTemplate.exchange(notificationUrl, HttpMethod.POST, requestEntity,
                    Object.class);

            if (res.getStatusCode() == HttpStatus.OK) {
                return AppUtil.createApiResponse(
                        true,
                        messageSource.getMessage("api.response.notification.send.successfully", null, Locale.ENGLISH),
                        null
                );
            } else {
                return AppUtil.createApiResponse(false, messageSource.getMessage("api.error.notification.send.failed", null, Locale.ENGLISH), null);
            }


        } catch (JDBCConnectionException | ConstraintViolationException | DataException | LockAcquisitionException |
                 PessimisticLockException
                 | QueryTimeoutException | SQLGrammarException | GenericJDBCException e) {
            logger.error(EXCEPTION, e);
            return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR, null, Locale.ENGLISH), null);
        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            return AppUtil.createApiResponse(false, messageSource.getMessage(ERROR, null, Locale.ENGLISH), null);
        }
    }

}
