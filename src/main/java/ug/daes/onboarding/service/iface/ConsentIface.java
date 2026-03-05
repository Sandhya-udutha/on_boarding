package ug.daes.onboarding.service.iface;

import org.springframework.http.HttpHeaders;

import ug.daes.onboarding.constant.ApiResponse;


public interface ConsentIface {



    ApiResponse signData(HttpHeaders httpHeaders);
}
