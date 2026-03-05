package ug.daes.onboarding.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.service.iface.TestOTPServiceIface;

@RestController
@CrossOrigin
public class TestController {
    private final TestOTPServiceIface testOTPServiceIface;


    public TestController(TestOTPServiceIface testOTPServiceIface) {
        this.testOTPServiceIface = testOTPServiceIface;
    }

    @GetMapping("/api/get/mobileotp")
    public ApiResponse getOTPMobile() {
        return testOTPServiceIface.testMobileOtpService();
    }

    @GetMapping("/api/get/emailotp")
    public ApiResponse getOTPEmail() {
        return testOTPServiceIface.testEmailOtpService();
    }

    @GetMapping("/send/notification")
    public ApiResponse getSendNotification() {
        return testOTPServiceIface.testSendNotification();
    }


}
