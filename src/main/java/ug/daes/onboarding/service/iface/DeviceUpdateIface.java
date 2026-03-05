package ug.daes.onboarding.service.iface;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.DeviceInfo;
import ug.daes.onboarding.dto.MobileOTPDto;
import ug.daes.onboarding.model.SubscriberDevice;

public interface DeviceUpdateIface {
    ApiResponse validateSubscriberAndDevice(DeviceInfo deviceInfo,MobileOTPDto mobileOTPDto);


    void updateSubscriberDeviceAndHistory(SubscriberDevice oldDevice,String newDeviceUid);

    ApiResponse activateNewDevice(DeviceInfo deviceInfo,MobileOTPDto mobileOTPDto);
}
