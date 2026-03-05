package ug.daes.onboarding.controller;

import java.text.ParseException;


import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.DeviceInfo;
import ug.daes.onboarding.dto.MobileOTPDto;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.service.iface.DeviceUpdateIface;

import ug.daes.onboarding.util.Utility;


@RestController
public class UpdateDeviceController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateDeviceController.class);


    private static final String CLASS = "UpdateDeviceController";
    private static final String APP_VERSION = "appVersion";
    private static final String OS_VERSION = "osVersion";
    private static final String DEVICE_ID = "deviceId";


    private final DeviceUpdateIface deviceUpdateIface;
    private final ExceptionHandlerUtil exceptionHandlerUtil;


    public UpdateDeviceController(DeviceUpdateIface deviceUpdateIface,
                                  ExceptionHandlerUtil exceptionHandlerUtil) {
        this.deviceUpdateIface = deviceUpdateIface;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }

    @PostMapping("/api/post/verify-new-device")
    public ApiResponse verifyNewDevice(HttpServletRequest request, @RequestBody MobileOTPDto subscriberDTO) throws ParseException {
        logger.info("{} - {} - verify new device req for {}", CLASS, Utility.getMethodName(), subscriberDTO);
        DeviceInfo deviceInfoObj = new DeviceInfo(request.getHeader(DEVICE_ID), request.getHeader(APP_VERSION), request.getHeader(OS_VERSION));
        if (deviceInfoObj.getDeviceId() == null || deviceInfoObj.getOsVersion() == null || deviceInfoObj.getAppVersion() == null) {
            return exceptionHandlerUtil.createErrorResponse("api.error.one.or.moredevice.info.is.missing");
        }
        subscriberDTO.setSubscriberEmail(subscriberDTO.getSubscriberEmail().toLowerCase());
        return deviceUpdateIface.validateSubscriberAndDevice(deviceInfoObj, subscriberDTO);
    }


    @PostMapping("/api/post/activate-new-device")
    public ApiResponse verifySubscriberDetails(HttpServletRequest request, @RequestBody MobileOTPDto mobileOTPDto) {
        try {
            DeviceInfo deviceInfoObj = new DeviceInfo(request.getHeader(DEVICE_ID), request.getHeader(APP_VERSION), request.getHeader(OS_VERSION));

            if (deviceInfoObj.getDeviceId() == null || deviceInfoObj.getOsVersion() == null || deviceInfoObj.getAppVersion() == null) {
                return exceptionHandlerUtil.createErrorResponse("api.error.one.or.moredevice.info.is.missing");
            }
            return deviceUpdateIface.activateNewDevice(deviceInfoObj, mobileOTPDto);
        } catch (Exception e) {
            logger.error("Unexpected exception", e);
            return exceptionHandlerUtil.handleException(e);
        }

    }
}
