package ug.daes.onboarding.service.iface;

import ug.daes.onboarding.constant.ApiResponse;

public interface TestOTPServiceIface {

    ApiResponse testMobileOtpService();

    ApiResponse testEmailOtpService();

    ApiResponse testSendNotification();

}
