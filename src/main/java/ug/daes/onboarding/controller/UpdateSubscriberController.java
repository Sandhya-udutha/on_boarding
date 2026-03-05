package ug.daes.onboarding.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ug.daes.onboarding.constant.ApiResponse;

import ug.daes.onboarding.dto.UpdateDto;
import ug.daes.onboarding.dto.UpdateOtpDto;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.service.iface.SubscriberServiceIface;

import jakarta.servlet.http.HttpServletRequest;


@RestController
public class UpdateSubscriberController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateSubscriberController.class);


    private static final String CLASS = "UpdateSubscriberController";

    private final SubscriberServiceIface subscriberServiceIface;
    private final ExceptionHandlerUtil exceptionHandlerUtil;


    public UpdateSubscriberController(SubscriberServiceIface subscriberServiceIface,
                                      ExceptionHandlerUtil exceptionHandlerUtil) {
        this.subscriberServiceIface = subscriberServiceIface;
        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }

    @PostMapping("/api/post/updateSubscriberDetails")
    public ApiResponse updatePhoneNumber(HttpServletRequest request, @RequestBody UpdateDto updateDto) {
        logger.info(CLASS + " updatePhoneNumber req  {}", updateDto);
        if (updateDto.isUpdateMobile() && updateDto.isUpdateEmail()) {
            return exceptionHandlerUtil.createErrorResponse("api.error.cant.update.mail.and.mobile.number.ata.time");
        } else if (updateDto.isUpdateMobile()) {
            return subscriberServiceIface.updatePhoneNumber(updateDto);
        } else if (updateDto.isUpdateEmail()) {
            return subscriberServiceIface.updateEmail(updateDto);
        }
        return exceptionHandlerUtil.createErrorResponse("api.error.did.not.select.anything.to.update");
    }

    @PostMapping("/api/post/updateSubscriberOtp")
    public ApiResponse sendOtp(HttpServletRequest request, @RequestBody UpdateOtpDto otpDto) {
        logger.info(CLASS + "sendOtpMobile req {}", otpDto);
        if (otpDto.isOtpEmail() && otpDto.isOtpMobile()) {
            return exceptionHandlerUtil.createErrorResponse("api.error.cant.send.otp.for.email.and.mobile.at.same.time");
        } else if (otpDto.isOtpMobile()) {
            return subscriberServiceIface.sendOtpMobile(otpDto);
        } else if (otpDto.isOtpEmail()) {
            return subscriberServiceIface.sendOtpEmail(otpDto);
        }
        return exceptionHandlerUtil.createErrorResponse("api.error.did.not.select.anything.to.update");
    }

}
