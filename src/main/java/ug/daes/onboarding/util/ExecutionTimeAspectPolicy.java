package ug.daes.onboarding.util;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import ug.daes.onboarding.model.SubscriberDevice;
import ug.daes.onboarding.model.SubscriberDeviceHistory;
import ug.daes.onboarding.repository.SubscriberDeviceHistoryRepoIface;
import ug.daes.onboarding.repository.SubscriberDeviceRepoIface;


@Aspect
@Component
public class ExecutionTimeAspectPolicy {
    private static Logger logger = LoggerFactory.getLogger(ExecutionTimeAspectPolicy.class);
    private static final String DISABLE = "DISABLED";
    private static final String ACTIVE = "ACTIVE";


    private final MessageSource messageSource;
    private final SubscriberDeviceRepoIface subscriberDeviceRepoIface;
    private final SubscriberDeviceHistoryRepoIface subscriberDeviceHistoryRepoIface;

    public ExecutionTimeAspectPolicy(MessageSource messageSource,
                                     SubscriberDeviceRepoIface subscriberDeviceRepoIface,
                                     SubscriberDeviceHistoryRepoIface subscriberDeviceHistoryRepoIface) {
        this.messageSource = messageSource;
        this.subscriberDeviceRepoIface = subscriberDeviceRepoIface;
        this.subscriberDeviceHistoryRepoIface = subscriberDeviceHistoryRepoIface;
    }

    @Pointcut("execution(* ug.daes.onboarding.controller.SubscriberController.saveSubscriberObData(..))")
    private void forsaveSubscriberObData() {
    }

    @Pointcut("execution(* ug.daes.onboarding.controller.SubscriberController.reOnboardAddSubscriberObData(..))")
    private void forreOnboardAddSubscriberObData() {
    }

    @Pointcut("execution(* ug.daes.onboarding.controller.SubscriberController.getSubscriberObData(..))")
    private void forgetSubscriberObData() {
    }

    @Pointcut("execution(* ug.daes.onboarding.controller.SubscriberController.resetPin(..))")
    private void forresetPin() {
    }


    @Pointcut("execution(* ug.daes.onboarding.controller.UpdateSubscriberController.updatePhoneNumber(..))")
    private void forUpdateSubscriberController() {
    }

    @Around("forsaveSubscriberObData() || forreOnboardAddSubscriberObData() || forgetSubscriberObData() || forresetPin() || forUpdateSubscriberController()")
    // methods
    public Object controllerPolicy(ProceedingJoinPoint joinPoint) throws Throwable {
        return checkPolicy(joinPoint);
    }

    private Object checkPolicy(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = extractHttpRequest(joinPoint.getArgs());
        String deviceUid = request != null ? request.getHeader("deviceId") : "";
        String appVersion = request != null ? request.getHeader("appVersion") : "";

        SubscriberDeviceHistory latestHistory = getLatestHistory(deviceUid);
        Optional<SubscriberDeviceHistory> historyOptional = Optional.ofNullable(latestHistory);

        SubscriberDevice subscriberDevices = getLatest(subscriberDeviceRepoIface.findBydeviceUid(deviceUid));

        boolean deviceEmpty = isAppVersionEmpty(appVersion, deviceUid);
        boolean checkPolicy = determinePolicy(deviceUid, historyOptional, subscriberDevices);

        Object result;
        if (deviceEmpty) {
            result = AppUtil.createApiResponse(false,
                    messageSource.getMessage("api.error.please.update.your.app", null, Locale.ENGLISH), null);
        } else if (checkPolicy) {
            result = joinPoint.proceed();
        } else {
            result = createDisabledDeviceResponse(subscriberDevices, historyOptional.orElse(null));
        }

        return result;
    }


    private HttpServletRequest extractHttpRequest(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest httpServletRequest) {
                return httpServletRequest;
            }
        }
        return null;
    }

    private SubscriberDeviceHistory getLatestHistory(String deviceUid) {
        List<SubscriberDeviceHistory> historyList = subscriberDeviceHistoryRepoIface.findBydeviceUid(deviceUid);
        logger.info(" historyList :: {}", historyList);
        return historyList.isEmpty() ? null : historyList.get(0);
    }

    private boolean isAppVersionEmpty(String appVersion, String deviceUid) {
        boolean empty = appVersion == null || appVersion.isEmpty();
        if (empty) {
            logger.info("appVersion is empty. appVersion: {} , deviceUid: {}", appVersion, deviceUid);
        }
        return empty;
    }

    private boolean determinePolicy(String deviceUid, Optional<SubscriberDeviceHistory> historyOptional,
                                    SubscriberDevice subscriberDevices) {

        return "WEB".equals(deviceUid)
                || (historyOptional.isPresent() && Optional.ofNullable(
                        subscriberDeviceRepoIface.findBydeviceUidAndStatus(deviceUid, ACTIVE))
                .map(device -> !isDeviceDisabled(device))
                .orElse(false))
                || (subscriberDevices != null && isDeviceActive(subscriberDevices));
    }

    private boolean isDeviceActive(SubscriberDevice device) {
        return ACTIVE.equalsIgnoreCase(device.getDeviceStatus());
    }

    private boolean isDeviceDisabled(SubscriberDevice device) {
        return DISABLE.equalsIgnoreCase(device.getDeviceStatus());
    }

    private Object createDisabledDeviceResponse(SubscriberDevice subscriberDevices, SubscriberDeviceHistory history) {
        if (subscriberDevices == null && history == null) {
            return AppUtil.createApiResponse(false,
                    messageSource.getMessage("api.error.subscriber.not.found", null, Locale.ENGLISH), null);
        } else {
            return AppUtil.createApiResponse(false,
                    messageSource.getMessage(
                            "api.error.account.registered.on.new.device.services.disabled.on.this.device",
                            null, Locale.ENGLISH), null);
        }
    }

    public SubscriberDevice getLatest(List<SubscriberDevice> list) {


        return list.stream()
                .sorted(Comparator.comparing(sd -> parseDate(sd.getUpdatedDate()), Comparator.reverseOrder()))
                .findFirst()
                .orElse(null);
    }

    private LocalDateTime parseDate(String date) {
        if (date.contains("T")) {
            return LocalDateTime.parse(date);
        } else {
            return LocalDateTime.parse(date.replace(" ", "T"));
        }
    }

}
