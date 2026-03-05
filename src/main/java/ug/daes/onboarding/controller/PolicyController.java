package ug.daes.onboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.PolicyDTO;
import ug.daes.onboarding.service.iface.PolicyIface;
import ug.daes.onboarding.util.AppUtil;

@RestController
public class PolicyController {


    private final PolicyIface policyIface;
    @Autowired
    public PolicyController(PolicyIface policyIface) {
        this.policyIface = policyIface;
    }

    @GetMapping("/api/get/verify-policy")
    public ApiResponse verifyPolicy(@RequestBody PolicyDTO policyDTO)
    {
        try{
            if(policyDTO.getDeviceUid()==null || policyDTO.getSuid()==null)
                return AppUtil.createApiResponse(false,"Bad Request",null);
            String deviceUid=policyIface.matchDeviceUid(policyDTO.getSuid(), policyDTO.getDeviceUid());
            if(deviceUid==null)
                return AppUtil.createApiResponse(false,"device does not exist",null);
            return AppUtil.createApiResponse(true,"",deviceUid);
        }catch (Exception e){
            return AppUtil.createApiResponse(false,e.getLocalizedMessage(),null);
        }



    }
}
