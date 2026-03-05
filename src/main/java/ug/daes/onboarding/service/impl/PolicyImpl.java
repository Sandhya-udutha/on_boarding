package ug.daes.onboarding.service.impl;


import org.springframework.stereotype.Service;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.exceptions.DeviceLookupException;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.exceptions.InvalidDateFormatException;
import ug.daes.onboarding.exceptions.InvalidDateValueException;
import ug.daes.onboarding.model.SubscriberDevice;
import ug.daes.onboarding.repository.SubscriberDeviceRepoIface;
import ug.daes.onboarding.service.iface.PolicyIface;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class PolicyImpl implements PolicyIface {

    private final SubscriberDeviceRepoIface subscriberDeviceRepoIface;
    private final ExceptionHandlerUtil exceptionHandlerUtil;

    public PolicyImpl(SubscriberDeviceRepoIface subscriberDeviceRepoIface,
                      ExceptionHandlerUtil exceptionHandlerUtil) {
        this.subscriberDeviceRepoIface = subscriberDeviceRepoIface;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }

    @Override
    public boolean checkPolicy(String date, String pattern, long policy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            LocalDateTime currTime = LocalDateTime.now();
            long noOfHours = dateTime.until(currTime, ChronoUnit.HOURS);
            return noOfHours >= policy;
        } catch (Exception e) {
            throw new InvalidDateFormatException(
                    "Error parsing date. Expected format: " + pattern, e);
        }
    }

    @Override
    public String matchDeviceUid(String suid, String deviceUid) {
        try {
            List<SubscriberDevice> devices = subscriberDeviceRepoIface.findBydeviceUid(deviceUid);
            SubscriberDevice subscriberDevice = devices.isEmpty() ? null : devices.get(0);
            return subscriberDevice == null ? null : subscriberDevice.getDeviceUid();
        } catch (Exception e) {
            throw new DeviceLookupException(
                    "Failed to fetch device for Subscriber UID: " + suid + ", Device UID: " + deviceUid, e);
        }
    }

    @Override
    public ApiResponse checkPolicyRange(String date, String pattern, long minLimit) throws InvalidDateValueException {

        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Date value is null or empty");
        }

        try {
            LocalDateTime dateTime;

            if (date.contains("T")) {
                dateTime = LocalDateTime.parse(date);
            } else {
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                dateTime = LocalDateTime.parse(date, formatter);
            }

            LocalDateTime currTime = LocalDateTime.now();
            long noOfHours = dateTime.until(currTime, ChronoUnit.HOURS);

            if (noOfHours < minLimit) {
                return exceptionHandlerUtil
                        .createErrorResponseWithResult("api.error.policy.limit", false);
            }

            return exceptionHandlerUtil
                    .createSuccessResponse("api.response.device.policy", noOfHours);

        } catch (Exception e) {
            throw new InvalidDateValueException("Invalid date value: " + date, e);
        }
    }
}
