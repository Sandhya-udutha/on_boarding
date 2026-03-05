package ug.daes.onboarding.service.iface;

import ug.daes.onboarding.constant.ApiResponse;

public interface PolicyIface {

    boolean checkPolicy(String date, String pattern, long policy);

    String matchDeviceUid(String suid, String deviceUid);

    ApiResponse checkPolicyRange(String date, String pattern, long minLimit);
    
}
