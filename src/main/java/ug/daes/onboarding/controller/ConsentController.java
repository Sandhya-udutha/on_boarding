package ug.daes.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;

import ug.daes.onboarding.model.ConsentHistory;
import ug.daes.onboarding.model.ConsentModel;
import ug.daes.onboarding.repository.ConsentHistoryRepo;
import ug.daes.onboarding.repository.ConsentRepoIface;
import ug.daes.onboarding.service.iface.ConsentIface;

import ug.daes.onboarding.util.AppUtil;

@RestController
@CrossOrigin
public class ConsentController {

	private static final Logger logger = LoggerFactory.getLogger(ConsentController.class);

	private static final String CLASS = "ConsentController";
	public static final String UNEXPECTED_EXCEPTION="Unexpected exception";
	public static final String API_RESPONSE_CONSENT="api.response.consent";

	private final ConsentRepoIface consentRepoIface;
	private final ConsentHistoryRepo consentHistoryRepo;
	private final ConsentIface consentIface;
	private final ExceptionHandlerUtil exceptionHandlerUtil;

	// ✅ Constructor Injection
	public ConsentController(ConsentRepoIface consentRepoIface,
							 ConsentHistoryRepo consentHistoryRepo,
							 ConsentIface consentIface,
							 ExceptionHandlerUtil exceptionHandlerUtil) {
		this.consentRepoIface = consentRepoIface;
		this.consentHistoryRepo = consentHistoryRepo;
		this.consentIface = consentIface;
		this.exceptionHandlerUtil = exceptionHandlerUtil;
	}

	@GetMapping(value = "/api/activte/consent")
	public ApiResponse getActivteConsent() {
		ConsentModel activeConsent = new ConsentModel();
		try {
			activeConsent = consentRepoIface.getActiveConsent();


			if (activeConsent != null) {
				return exceptionHandlerUtil.createSuccessResponse(API_RESPONSE_CONSENT, activeConsent);
			} else {
				return exceptionHandlerUtil.createErrorResponse("api.error.no.active.consent.found");
			}
		} catch (Exception e) {
			logger.error(CLASS + "Get Active Consent Exception {}", e.getMessage());
			logger.error(UNEXPECTED_EXCEPTION, e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@GetMapping(value = "/api/get/list/consent")
	public ApiResponse getConsentList() {
		List<ConsentModel> consent = new ArrayList<>();
		try {
			consent = consentRepoIface.findAll();
            return exceptionHandlerUtil.createSuccessResponse(API_RESPONSE_CONSENT, consent);
        } catch (Exception e) {
			logger.error(CLASS + "Get Consent List {} ", e.getMessage());
			logger.error(UNEXPECTED_EXCEPTION, e);
			return exceptionHandlerUtil.handleException(e);
		}
	}

	@GetMapping(value = "/api/get/consent/id")
	public ApiResponse getConsentById(@RequestParam int id) {

		ConsentHistory consent = new ConsentHistory();
		try {
			consent = consentHistoryRepo.findTopByConsentIdOrderByCreatedOnDesc(id);
			if (consent != null) {

				return exceptionHandlerUtil.createSuccessResponse(API_RESPONSE_CONSENT, consent);

			} else {
				return exceptionHandlerUtil.createErrorResponse("api.error.empty");

			}
		} catch (Exception e) {
			logger.error(CLASS + "Get Consent By-id {} ", e.getMessage());
			logger.error(UNEXPECTED_EXCEPTION, e);
			return exceptionHandlerUtil.handleException(e);

		}

	}

	@PostMapping(value = "/api/add/consent")
	public ApiResponse addConsent(@RequestBody ConsentModel consent) {
		logger.info(CLASS + "Add Consent :: {}", consent);
		try {
			if (consent.getConsent() == null || consent.getConsent().isEmpty()) {
				return AppUtil.createApiResponse(false, "api.error.consent.is.empty", null);
			}
			consent.setCreatedOn(AppUtil.getDate());
			consent.setUpdatedOn(AppUtil.getDate());
			consent.setStatus("INACTIVE");
			consentRepoIface.save(consent);
            return exceptionHandlerUtil.successResponse("api.response.consent.saved");

        } catch (Exception e) {
			logger.error(CLASS + "Add Consent Exception :: {}" , e.getMessage());
			return exceptionHandlerUtil.handleException(e);

		}
	}

	@GetMapping(value = "/api/update/cons/status")
	public ApiResponse updateConsentStatus(@RequestParam int consentId, @RequestParam String status) {
		logger.info(CLASS + "Update Consent Status :: consentId and status {},{}", consentId, status);
		try {
			if (status.equals("Active") || status.equals("ACTIVE")) {
				consentRepoIface.updateConsentStatusActive(consentId, status);
			} else {
				consentRepoIface.updateConsentStatusInactive(consentId, status);
			}
            return exceptionHandlerUtil.successResponse("api.response.consent.updated");

        } catch (Exception e) {
			logger.error(CLASS + "Update Consent Status Exception {}", e.getMessage());
			logger.error(UNEXPECTED_EXCEPTION, e);
			return exceptionHandlerUtil.handleException(e);

		}
	}

	@PostMapping("/sign-data/for/consent")
	public ApiResponse signData(@RequestHeader HttpHeaders httpHeaders) {
		return consentIface.signData(httpHeaders);
	}

}
