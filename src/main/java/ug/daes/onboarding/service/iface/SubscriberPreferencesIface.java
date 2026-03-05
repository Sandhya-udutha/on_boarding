package ug.daes.onboarding.service.iface;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.SubscriberPreferenceRequestDTO;

public interface SubscriberPreferencesIface {

    ApiResponse saveLanguagePreference(SubscriberPreferenceRequestDTO subscriberPreferenceRequestDTO);
    ApiResponse getPreferenceBySuid(String suid);
}
