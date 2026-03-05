package ug.daes.onboarding.service.impl;

import java.util.Base64;
import java.util.List;

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


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.SignedDataDto;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.ConsentHistory;
import ug.daes.onboarding.model.Subscriber;
import ug.daes.onboarding.model.SubscriberConsents;
import ug.daes.onboarding.repository.ConsentHistoryRepo;

import ug.daes.onboarding.repository.SubscriberConsentsRepo;
import ug.daes.onboarding.repository.SubscriberRepoIface;
import ug.daes.onboarding.service.iface.ConsentIface;
import ug.daes.onboarding.util.AppUtil;

@Service
public class ConsentImpl implements ConsentIface {

    private static Logger logger = LoggerFactory.getLogger(ConsentImpl.class);

    private final RestTemplate restTemplate;
    private final SubscriberRepoIface subscriberRepoIface;
    private final ConsentHistoryRepo consentHistoryRepo;
    private final SubscriberConsentsRepo subscriberConsentsRepo;

    private final ExceptionHandlerUtil exceptionHandlerUtil;
    private final String signedURL;
    private final boolean signRequired;

    public ConsentImpl(

            RestTemplate restTemplate,
            SubscriberRepoIface subscriberRepoIface,
            ConsentHistoryRepo consentHistoryRepo,
            SubscriberConsentsRepo subscriberConsentsRepo,

            ExceptionHandlerUtil exceptionHandlerUtil,
            @Value("${signed.data.url}") String signedURL,
            @Value("${signed.required.by.user}") boolean signRequired
    ) {

        this.restTemplate = restTemplate;
        this.subscriberRepoIface = subscriberRepoIface;
        this.consentHistoryRepo = consentHistoryRepo;
        this.subscriberConsentsRepo = subscriberConsentsRepo;

        this.exceptionHandlerUtil = exceptionHandlerUtil;
        this.signedURL = signedURL;
        this.signRequired = signRequired;
    }

    @Override
    public ApiResponse signData(HttpHeaders httpHeaders) {
        try {
            String subscriberMail = httpHeaders.getFirst("adminugpassemail");
            if (subscriberMail == null) return createEmailError();

            Subscriber subscriber = getSubscriber(subscriberMail);
            if (subscriber == null) return subscriberNotFoundError();

            ConsentHistory latestConsent = getLatestConsent();

            if (signRequired) {
                return signConsentFlow(subscriberMail, subscriber, latestConsent);
            } else {
                return saveConsentWithoutSign(subscriber, latestConsent);
            }

        } catch (JDBCConnectionException | ConstraintViolationException | DataException |
                 LockAcquisitionException | PessimisticLockException | QueryTimeoutException |
                 SQLGrammarException | GenericJDBCException ex) {
            logger.error("Unexpected database exception", ex);
            return exceptionHandlerUtil.createErrorResponse("api.error.database");
        } catch (Exception e) {
            logger.error("Unexpected exception occurred", e);
            return exceptionHandlerUtil.createErrorResponse("api.error.generic");
        }
    }


    private ApiResponse createEmailError() {
        return exceptionHandlerUtil.createErrorResponse("api.error.email.cant.should.be.null.or.empty");
    }

    private ApiResponse subscriberNotFoundError() {
        return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.not.found.with.given.email");
    }

    private Subscriber getSubscriber(String email) {
        return subscriberRepoIface.findByemailId(email);
    }

    private ConsentHistory getLatestConsent() {
        List<ConsentHistory> latestConsentList = consentHistoryRepo.findLatestConsent();
        return latestConsentList.isEmpty() ? null : latestConsentList.get(0);
    }

    private ApiResponse signConsentFlow(String subscriberMail, Subscriber subscriber, ConsentHistory consentHistory) throws Exception {
        String consentData = "I agreed to above Terms and conditions and Data privacy terms";

        String url = signedURL;
        SignedDataDto signedDataDto = new SignedDataDto();
        String base64 = Base64.getEncoder().encodeToString(consentData.getBytes());

        signedDataDto.setDocumentType("CADES");
        signedDataDto.setSubscriberUniqueId(subscriberMail);
        signedDataDto.setDocData(base64);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> reqEntity = new HttpEntity<>(signedDataDto, headers);

        ResponseEntity<ApiResponse> res = restTemplate.exchange(url, HttpMethod.POST, reqEntity, ApiResponse.class);
        if (!res.getBody().isSuccess()) {
            return exceptionHandlerUtil.createErrorResponseWithResult("api.error.generic", res.getBody().getMessage());
        }

        saveSubscriberConsent(subscriber, consentHistory, consentData, res.getBody().getResult().toString());
        return exceptionHandlerUtil.successResponse("api.response.consent.signed");
    }

    private ApiResponse saveConsentWithoutSign(Subscriber subscriber, ConsentHistory consentHistory) {
        String consentData = "I agreed to above Terms and conditions and Data privacy terms";

        SubscriberConsents existing = subscriberConsentsRepo.findSubscriberConsentBySuidAndConsentId(
                subscriber.getSubscriberUid(), consentHistory.getId());
        if (existing != null) return exceptionHandlerUtil.successResponse("api.response.consent.saved");

        saveSubscriberConsent(subscriber, consentHistory, consentData, null);
        return exceptionHandlerUtil.successResponse("api.response.consent.saved");
    }

    private void saveSubscriberConsent(Subscriber subscriber, ConsentHistory consentHistory, String consentData, String signedData) {
        SubscriberConsents subscriberConsents = new SubscriberConsents();
        subscriberConsents.setConsentData(consentData);
        subscriberConsents.setSignedConsentData(signedData);
        subscriberConsents.setConsentId(consentHistory.getId());
        subscriberConsents.setSuid(subscriber.getSubscriberUid());
        subscriberConsents.setCreatedOn(AppUtil.getDate());
        subscriberConsentsRepo.save(subscriberConsents);
    }
}