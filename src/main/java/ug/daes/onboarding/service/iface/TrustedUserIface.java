package ug.daes.onboarding.service.iface;

import java.util.List;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.model.TrustedUser;

public interface TrustedUserIface {

    ApiResponse getTrustedUserByEmail(String email);

    ApiResponse updateTrustedUser(TrustedUser trustedUser);

    ApiResponse deleteTrustedUser(String email);

    ApiResponse getAllTrustedUser();

    ApiResponse addTrustedUser(List<TrustedUser> trustedUser);

}
