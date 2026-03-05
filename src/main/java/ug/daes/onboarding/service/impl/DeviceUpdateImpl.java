package ug.daes.onboarding.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.constant.Constant;
import ug.daes.onboarding.constant.DeviceUpdatePolicy;
import ug.daes.onboarding.dto.*;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.*;
import ug.daes.onboarding.repository.*;
import ug.daes.onboarding.service.iface.DeviceUpdateIface;
import ug.daes.onboarding.service.iface.PolicyIface;
import ug.daes.onboarding.service.iface.SubscriberServiceIface;
import ug.daes.onboarding.service.iface.TemplateServiceIface;
import ug.daes.onboarding.util.AppUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ug.daes.onboarding.service.impl.SubscriberServiceImpl.findLatestOnboardedSub;

@Service
public class DeviceUpdateImpl implements DeviceUpdateIface {

    private static Logger logger = LoggerFactory.getLogger(DeviceUpdateImpl.class);
    private static final String CLASS = "DeviceUpdateImpl";
    private static final String DEVICE_UPDATE_PREFIX = "DEVICE_UPDATE | ";
    private static final String OTHER = "OTHER";
    private static final String EXCEPTION = "Unexpected exception";
    private static final String UPDATE_SUBSCRIBER = "updateSubscriberDeviceAndHistory Exception {}";
    private static final String MOBILE_ALREDY_USED = "api.error.this.mobile.number.is.already.used.with.differenet.email.id";
    private static final String WELCOME_BACK = "api.response.services.now.accessible.on.this.device.welcome.back";
    private static final String DEVICE_CHANGE = " activateNewDevice device change mongo {} ";
    private static final String NEW_DEVICE_ALREADY_USED = "api.response.new.device.is.ready.to.be.used";


    private final SubscriberRepoIface subscriberRepoIface;
    private final SubscriberDeviceRepoIface deviceRepoIface;

    private final LogModelServiceImpl logModelServiceImpl;
    private final SubscriberOnboardingDataRepoIface onboardingDataRepoIface;
    private final TemplateServiceIface templateServiceIface;
    private final SubscriberStatusRepoIface statusRepoIface;
    private final SubscriberCertificatesRepoIface subscriberCertificatesRepoIface;
    private final SubscriberCertPinHistoryRepoIface subscriberCertPinHistoryRepoIface;
    private final SubscriberFcmTokenRepoIface fcmTokenRepoIface;
    private final SubscriberDeviceHistoryRepoIface subscriberDeviceHistoryRepoIface;
    private final PolicyIface policyIface;
    private final SubscriberServiceIface subscriberServiceIface;
    private final DevicePolicyRepository devicePolicyRepository;
    private final ExceptionHandlerUtil exceptionHandlerUtil;

    @Autowired
    public DeviceUpdateImpl(
            @Lazy SubscriberServiceIface subscriberServiceIface,
            SubscriberRepoIface subscriberRepoIface,
            SubscriberDeviceRepoIface deviceRepoIface,

            LogModelServiceImpl logModelServiceImpl,
            SubscriberOnboardingDataRepoIface onboardingDataRepoIface,
            TemplateServiceIface templateServiceIface,
            SubscriberStatusRepoIface statusRepoIface,
            SubscriberCertificatesRepoIface subscriberCertificatesRepoIface,
            SubscriberCertPinHistoryRepoIface subscriberCertPinHistoryRepoIface,
            SubscriberFcmTokenRepoIface fcmTokenRepoIface,
            SubscriberDeviceHistoryRepoIface subscriberDeviceHistoryRepoIface,
            PolicyIface policyIface,
            DevicePolicyRepository devicePolicyRepository,
            ExceptionHandlerUtil exceptionHandlerUtil
    ) {
        this.subscriberServiceIface = subscriberServiceIface;
        this.subscriberRepoIface = subscriberRepoIface;
        this.deviceRepoIface = deviceRepoIface;

        this.logModelServiceImpl = logModelServiceImpl;
        this.onboardingDataRepoIface = onboardingDataRepoIface;
        this.templateServiceIface = templateServiceIface;
        this.statusRepoIface = statusRepoIface;
        this.subscriberCertificatesRepoIface = subscriberCertificatesRepoIface;
        this.subscriberCertPinHistoryRepoIface = subscriberCertPinHistoryRepoIface;
        this.fcmTokenRepoIface = fcmTokenRepoIface;
        this.subscriberDeviceHistoryRepoIface = subscriberDeviceHistoryRepoIface;
        this.policyIface = policyIface;
        this.devicePolicyRepository = devicePolicyRepository;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }

    @Value("${device.update.min.policy}")
    private long minhour;

    @Value("${is.onboarding.fee}")
    private boolean selfieRequired;

    @Value("${device.update.max.policy}")
    private long maxhour;


    @Override
    public void updateSubscriberDeviceAndHistory(SubscriberDevice oldDevice, String newDeviceUid) {
        // save to subscriber device history
        try {
            logger.info(CLASS + "updateSubscriberDeviceAndHistory oldDevice and newDeviceUid {}, {}", oldDevice,
                    newDeviceUid);

            SubscriberDeviceHistory subscriberDeviceHistory = new SubscriberDeviceHistory();
            subscriberDeviceHistory.setDeviceUid(oldDevice.getDeviceUid());
            subscriberDeviceHistory.setDeviceStatus(Constant.DEVICE_STATUS_DISABLED);
            subscriberDeviceHistory.setSubscriberUid(oldDevice.getSubscriberUid());
            subscriberDeviceHistory.setCreatedDate(AppUtil.getDate());
            subscriberDeviceHistory.setUpdatedDate(AppUtil.getDate());
            subscriberDeviceHistoryRepoIface.save(subscriberDeviceHistory);

            oldDevice.setDeviceUid(newDeviceUid);
            oldDevice.setDeviceStatus(Constant.DEVICE_STATUS_ACTIVE);

            oldDevice.setUpdatedDate(AppUtil.getDate());
            deviceRepoIface.save(oldDevice);

        } catch (JDBCConnectionException | ConstraintViolationException | DataException | LockAcquisitionException
                 | PessimisticLockException | QueryTimeoutException | SQLGrammarException | GenericJDBCException ex) {
            logger.error(EXCEPTION, ex);
            logger.error(CLASS + UPDATE_SUBSCRIBER, ex.getMessage());
        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            logger.error(CLASS + UPDATE_SUBSCRIBER, e.getMessage());
        }

    }

    @Override
    public ApiResponse activateNewDevice(DeviceInfo deviceInfo, MobileOTPDto mobileOTPDto) {
        try {
            if (Objects.isNull(deviceInfo) && Objects.isNull(mobileOTPDto)) {
                return exceptionHandlerUtil.createErrorResponse(
                        "api.error.device.info.and.mobile.otp.dtos.cant.null");
            }

            int countDevice = subscriberRepoIface.countSubscriberDevice(deviceInfo.getDeviceId());
            int countMobile = subscriberRepoIface.countSubscriberMobile(mobileOTPDto.getSubscriberMobileNumber());
            int countEmail = subscriberRepoIface.countSubscriberEmailId(mobileOTPDto.getSubscriberEmail().toLowerCase());

            logger.info(CLASS + "Device/Mobile/Email counts: {}, {}, {}", countDevice, countMobile, countEmail);

            Subscriber subscriber = subscriberRepoIface.getSubscriberUidByEmailAndMobile(
                    mobileOTPDto.getSubscriberEmail(), mobileOTPDto.getSubscriberMobileNumber());

            Date startTime = new Date();

            // Only proceed if counts are valid
            if (countEmail == 1 && countMobile == 1 && subscriber != null) {
                SubscriberDevice device = deviceRepoIface.getSubscriber(subscriber.getSubscriberUid());

                if (device.getDeviceStatus().equals(Constant.DEVICE_STATUS_ACTIVE) ||
                        device.getDeviceStatus().equals(Constant.DEVICE_STATUS_DISABLED)) {

                    return handleExistingDevice(deviceInfo, mobileOTPDto, device, subscriber, startTime);
                }
            }

            return exceptionHandlerUtil.createErrorResponse(
                    "api.error.the.device.is.already.registered.with.either.same.or.different.email.id.and.mobile.number");

        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            logger.error(CLASS + UPDATE_SUBSCRIBER, e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private ApiResponse handleExistingDevice(DeviceInfo deviceInfo,
                                             MobileOTPDto mobileOTPDto,
                                             SubscriberDevice device,
                                             Subscriber subscriber,
                                             Date startTime) throws ParseException {

        // Update device history
        updateSubscriberDeviceAndHistory(device, deviceInfo.getDeviceId());

        // Update subscriber info
        Subscriber subscriberToUpdate = subscriberRepoIface.findBysubscriberUid(subscriber.getSubscriberUid());
        if (subscriberToUpdate != null) {
            subscriberToUpdate.setAppVersion(deviceInfo.getAppVersion());
            subscriberToUpdate.setOsVersion(deviceInfo.getOsVersion());
            subscriberToUpdate.setOsName(mobileOTPDto.getOsName());
            subscriberToUpdate.setDeviceInfo(mobileOTPDto.getDeviceInfo());
            subscriberRepoIface.save(subscriberToUpdate);
        }

        // Update FCM token
        updateFcmToken(subscriber.getSubscriberUid(), mobileOTPDto.getFcmToken());

        // Logging
        Date endTime = new Date();
        String deviceInfoStr = mobileOTPDto.getOsName() + " | " + deviceInfo.getOsVersion() + " | "
                + deviceInfo.getAppVersion() + " | " + mobileOTPDto.getDeviceInfo();
        String message = DEVICE_UPDATE_PREFIX + deviceInfo.getDeviceId() + "|" + device.getDeviceUid() + "|"
                + AppUtil.getDate() + "|" + deviceInfoStr;

        logger.info(CLASS + DEVICE_CHANGE, message);

        logModelServiceImpl.setLogModelDTO(true, device.getSubscriberUid(), null, OTHER, null,
                message, startTime, endTime, null);

        return exceptionHandlerUtil.successResponse(WELCOME_BACK);
    }

    private void updateFcmToken(String suid, String fcmToken) {
        try {
            logger.info(CLASS + "updateFcmToken suid and fcmToken {}, {}", suid, fcmToken);
            SubscriberFcmToken subscriberFcmToken = fcmTokenRepoIface.findBysubscriberUid(suid);
            subscriberFcmToken.setFcmToken(fcmToken);
            subscriberFcmToken.setCreatedDate(AppUtil.getDate());
            fcmTokenRepoIface.save(subscriberFcmToken);
        } catch (JDBCConnectionException | ConstraintViolationException | DataException | LockAcquisitionException
                 | PessimisticLockException | QueryTimeoutException | SQLGrammarException | GenericJDBCException ex) {
            logger.error(EXCEPTION, ex);
            logger.error(CLASS + "updateFcmToken Exception {}", ex.getMessage());
        } catch (Exception e) {
            logger.error(EXCEPTION, e);

            logger.error(CLASS + "updateFcmToken Exception {}", e.getMessage());
        }

    }

    private NewDeviceDTO setNewDeviceResponse(Subscriber subscriber) {
        NewDeviceDTO newDeviceDTO = new NewDeviceDTO();
        newDeviceDTO.setNewDevice(true);

        if (subscriber == null) return newDeviceDTO;

        try {
            newDeviceDTO.setEmail(subscriber.getEmailId());
            newDeviceDTO.setMobileNumber(subscriber.getMobileNumber());

            SubscriberStatusModel status = statusRepoIface.findBysubscriberUid(subscriber.getSubscriberUid());
            SubscriberDetailsReponseDTO responseDTO = new SubscriberDetailsReponseDTO();
            responseDTO.setSuID(subscriber.getSubscriberUid());
            responseDTO.setSubscriberStatus(status != null ? status.getSubscriberStatus() : null);

            List<SubscriberOnboardingData> onboardingDataList = onboardingDataRepoIface
                    .getBySubUid(subscriber.getSubscriberUid());
            SubscriberOnboardingData onboardingData = getLatestOnboardingData(onboardingDataList);

            if (onboardingData != null) {
                newDeviceDTO.setIdDocNumber(onboardingData.getIdDocNumber());
                newDeviceDTO.setSelfieUri(resolveSelfieUri(onboardingData));
                responseDTO.setSubscriberDetails(buildSubscriberDetails(subscriber, onboardingData));
            } else {
                newDeviceDTO.setIdDocNumber(null);
                newDeviceDTO.setSelfieUri(null);
                responseDTO.setSubscriberDetails(null);
            }

            newDeviceDTO.setSubscriberStatusDetails(responseDTO);
            return newDeviceDTO;

        } catch (Exception e) {
            logger.error(CLASS + " setNewDeviceResponse Exception {}", e.getMessage(), e);
            return newDeviceDTO;
        }
    }

    private String resolveSelfieUri(SubscriberOnboardingData onboardingData) {
        if (selfieRequired) {
            ApiResponse response = subscriberServiceIface.getSubscriberSelfie(onboardingData.getSelfieUri());
            return response.isSuccess() ? (String) response.getResult() : "";
        } else {
            return onboardingData.getSelfie();
        }
    }

    private SubscriberDetails buildSubscriberDetails(Subscriber subscriber, SubscriberOnboardingData onboardingData) {
        SubscriberDetails subscriberDetails = new SubscriberDetails();
        subscriberDetails.setSubscriberName(subscriber.getFullName());
        subscriberDetails.setOnboardingMethod(onboardingData.getOnboardingMethod());

        ApiResponse editTemplateDTORes = templateServiceIface.getTemplateLatestById(onboardingData.getTemplateId());
        if (editTemplateDTORes.isSuccess()) {
            EditTemplateDTO editTemplateDTO = (EditTemplateDTO) editTemplateDTORes.getResult();
            subscriberDetails.setTemplateDetails(editTemplateDTO);

            List<String> statuses = subscriberCertificatesRepoIface.getSubscriberCertificateStatus(
                    subscriber.getSubscriberUid(), Constant.SIGN, Constant.ACTIVE);
            String certStatus = statuses.isEmpty() ? null : statuses.get(0);
            subscriberDetails.setCertificateStatus(certStatus != null ? certStatus : Constant.PENDING);

            PinStatus pinStatus = new PinStatus();
            if (Constant.ACTIVE.equals(certStatus)) {
                SubscriberCertificatePinHistory certificatePinHistory =
                        subscriberCertPinHistoryRepoIface.findBysubscriberUid(subscriber.getSubscriberUid());
                if (certificatePinHistory != null) {
                    pinStatus.setAuthPinSet(certificatePinHistory.getAuthPinList() != null);
                    pinStatus.setSignPinSet(certificatePinHistory.getSignPinList() != null);
                }
            }
            subscriberDetails.setPinStatus(pinStatus);
        } else {
            return null;
        }
        return subscriberDetails;
    }

    private SubscriberOnboardingData getLatestOnboardingData(List<SubscriberOnboardingData> onboardingDataList) {
        if (onboardingDataList == null || onboardingDataList.isEmpty()) return null;
        return onboardingDataList.size() == 1 ? onboardingDataList.get(0) : findLatestOnboardedSub(onboardingDataList);
    }

    @Override
    public ApiResponse validateSubscriberAndDevice(DeviceInfo deviceInfo, MobileOTPDto mobileOTPDto) {
        try {
            int countDevice = subscriberRepoIface.countSubscriberDevice(deviceInfo.getDeviceId());
            int countMobile = subscriberRepoIface.countSubscriberMobile(mobileOTPDto.getSubscriberMobileNumber());
            int countEmail = subscriberRepoIface.countSubscriberEmailId(mobileOTPDto.getSubscriberEmail().toLowerCase());

            logger.info("{} validateSubscriberAndDeviceNew ::: {}  mobileOTPDto :: {}", CLASS, deviceInfo, mobileOTPDto);

            Subscriber subscriber = subscriberRepoIface.getSubscriberDetailsByEmailAndMobile(
                    mobileOTPDto.getSubscriberEmail(), mobileOTPDto.getSubscriberMobileNumber());

            NewDeviceDTO newDeviceDTO = setNewDeviceResponse(subscriber);

            // ---------- Early returns for simple cases ----------
            if (isFirstTimeRegistration(countDevice, countEmail, countMobile)) {
                newDeviceDTO.setNewDevice(false);
                return buildNewDeviceResponse(newDeviceDTO, "api.response.first.time.registering.onboarding");
            }

            ApiResponse conflictResponse = checkConflictScenarios(countDevice, countEmail, countMobile, subscriber, newDeviceDTO);
            if (conflictResponse != null) return conflictResponse;

            // ---------- Existing device scenario ----------
            if (isExistingDeviceScenario(countDevice, countEmail, countMobile)) {
                return handleExistingActiveDevice(subscriber, newDeviceDTO);
            }

            // ---------- Other existing device scenarios ----------
            return handleExistingDeviceValidation(deviceInfo, subscriber, newDeviceDTO);

        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            logger.error(CLASS + " validateSubscriberAndDevice Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    private ApiResponse checkConflictScenarios(int countDevice, int countEmail, int countMobile, Subscriber subscriber, NewDeviceDTO newDeviceDTO) {
        if (countEmail >= 1 && countMobile >= 1 && subscriber == null) {
            return buildErrorResponse(newDeviceDTO, "api.error.this.mobile.no.is.already.register.with.different.email.id");
        }
        if (countDevice == 0 && countEmail == 1 && countMobile == 0) {
            return buildErrorResponse(newDeviceDTO, "api.error.this.email.id.is.already.used.with.differenet.mobile.no");
        }
        if (countDevice == 0 && countEmail == 0 && countMobile == 1) {
            return buildErrorResponse(newDeviceDTO, MOBILE_ALREDY_USED);
        }
        return null;
    }

    private boolean isExistingDeviceScenario(int countDevice, int countEmail, int countMobile) {
        return countDevice == 0 && countEmail == 1 && countMobile == 1;
    }

    private boolean isFirstTimeRegistration(int countDevice, int countEmail, int countMobile) {
        return countDevice == 0 && countEmail == 0 && countMobile == 0;
    }

    private ApiResponse handleExistingActiveDevice(Subscriber subscriber, NewDeviceDTO newDeviceDTO) {
        SubscriberDevice subDevice = deviceRepoIface.getSubscriber(subscriber.getSubscriberUid());
        if (Constant.DEVICE_STATUS_ACTIVE.equals(subDevice.getDeviceStatus())) {
            String date = Objects.equals(subDevice.getCreatedDate(), subDevice.getUpdatedDate())
                    ? subDevice.getCreatedDate() : subDevice.getUpdatedDate();

            ApiResponse policyCheck = checkDevicePolicy(date);
            if (policyCheck != null) return policyCheck;
        }
        return buildNewDeviceResponse(newDeviceDTO, NEW_DEVICE_ALREADY_USED);
    }

    private ApiResponse handleExistingDeviceValidation(DeviceInfo deviceInfo, Subscriber subscriber, NewDeviceDTO newDeviceDTO) {
        SubscriberDevice subDevice = deviceRepoIface.findBydeviceUidAndStatus(deviceInfo.getDeviceId(), Constant.DEVICE_STATUS_ACTIVE);

        if (subDevice == null && subscriber == null) {
            newDeviceDTO.setNewDevice(false);
            return exceptionHandlerUtil.createSuccessResponse("api.response.first.time.registering.onboarding", newDeviceDTO);
        }

        if (subDevice != null && subscriber == null) {
            return exceptionHandlerUtil.createErrorResponseWithResult(MOBILE_ALREDY_USED, newDeviceDTO);
        }

        SubscriberDevice subscriberDevice = deviceRepoIface.getSubscriber(subscriber.getSubscriberUid());
        if (Constant.DEVICE_STATUS_ACTIVE.equals(subscriberDevice.getDeviceStatus())) {
            String date = Objects.equals(subscriberDevice.getCreatedDate(), subscriberDevice.getUpdatedDate())
                    ? subscriberDevice.getCreatedDate() : subscriberDevice.getUpdatedDate();
            ApiResponse policyCheck = checkDevicePolicy(date);
            if (policyCheck != null) return policyCheck;

            return buildNewDeviceResponse(newDeviceDTO, NEW_DEVICE_ALREADY_USED);
        }

        if (subscriberDevice.getDeviceUid().equalsIgnoreCase(deviceInfo.getDeviceId()) &&
                Constant.DEVICE_STATUS_DISABLED.equalsIgnoreCase(subscriberDevice.getDeviceStatus()) &&
                subscriberDevice.getSubscriberUid().equalsIgnoreCase(subscriber.getSubscriberUid())) {

            newDeviceDTO.setNewDevice(false);
            return buildNewDeviceResponse(newDeviceDTO, NEW_DEVICE_ALREADY_USED);
        }

        return buildNewDeviceResponse(newDeviceDTO, NEW_DEVICE_ALREADY_USED);
    }

    private ApiResponse buildNewDeviceResponse(NewDeviceDTO dto, String successMessage) {
        return exceptionHandlerUtil.createSuccessResponse(successMessage, dto);
    }

    private ApiResponse buildErrorResponse(NewDeviceDTO dto, String errorMessage) {
        return exceptionHandlerUtil.createErrorResponseWithResult(errorMessage, dto);
    }

    private ApiResponse checkDevicePolicy(String date) {
        long devicePolicyHour = resolveDevicePolicyHour();
        ApiResponse policyResponse = policyIface.checkPolicyRange(date, DeviceUpdatePolicy.PATTERN, devicePolicyHour);
        if (!policyResponse.isSuccess()) {
            return exceptionHandlerUtil.createFailedResponseWithCustomMessage("api.error.device.access.blocked",devicePolicyHour);
        }
        return null; // success
    }

    private long resolveDevicePolicyHour() {
        Optional<DevicePolicyModel> policyModelOpt = Optional.ofNullable(devicePolicyRepository.getDevicePolicyHour());
        long devicePolicyHour = policyModelOpt.map(DevicePolicyModel::getDevicePolicyHour).orElse((int) minhour);
        if (devicePolicyHour < minhour) devicePolicyHour = minhour;
        if (devicePolicyHour > maxhour) devicePolicyHour = maxhour;
        return devicePolicyHour;
    }
}