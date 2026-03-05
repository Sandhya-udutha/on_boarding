package ug.daes.onboarding.controller;

import java.net.UnknownHostException;

import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.MobileOTPDto;
import ug.daes.onboarding.service.iface.OtpServiceIface;

@RestController
@CrossOrigin
public class OTPController {

    private static final Logger logger = LoggerFactory.getLogger(OTPController.class);

    private static final String CLASS = "OTPController";

    private final OtpServiceIface otpServiceIface;


    public OTPController(OtpServiceIface otpServiceIface) {
        this.otpServiceIface = otpServiceIface;
    }

    @PostMapping("/api/post/register-subscriber")
    public ApiResponse sendOtpMobile(@RequestBody MobileOTPDto otpDto)
            throws  ParseException, UnknownHostException {
        logger.info(CLASS + "sendOtpMobile req {} ", otpDto);

        return otpServiceIface.sendOTPMobileSms(otpDto);
    }


}
