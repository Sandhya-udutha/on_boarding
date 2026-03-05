package ug.daes.onboarding.service.impl;


import org.springframework.stereotype.Service;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.SubscriberPreferenceRequestDTO;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.Subscriber;
import ug.daes.onboarding.model.SubscriberPreferences;
import ug.daes.onboarding.repository.SubscriberPreferencesRepo;
import ug.daes.onboarding.repository.SubscriberRepoIface;
import ug.daes.onboarding.service.iface.SubscriberPreferencesIface;
import ug.daes.onboarding.util.AppUtil;

@Service
public class SubscriberPreferenceImpl implements SubscriberPreferencesIface {

    private final ExceptionHandlerUtil exceptionHandlerUtil;
    private final SubscriberPreferencesRepo subscriberPreferencesRepo;
    private final SubscriberRepoIface subscriberRepoIface;

    public SubscriberPreferenceImpl(ExceptionHandlerUtil exceptionHandlerUtil,
                                    SubscriberPreferencesRepo subscriberPreferencesRepo,
                                    SubscriberRepoIface subscriberRepoIface) {
        this.exceptionHandlerUtil = exceptionHandlerUtil;
        this.subscriberPreferencesRepo = subscriberPreferencesRepo;
        this.subscriberRepoIface = subscriberRepoIface;
    }

    @Override
    public ApiResponse saveLanguagePreference(SubscriberPreferenceRequestDTO subscriberPreferenceRequestDTO) {
        if(subscriberPreferenceRequestDTO.getSuid()==null||subscriberPreferenceRequestDTO.getSuid().isEmpty()){
            return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.suid.cantbe.null.or.empty");
        }
        if (subscriberPreferenceRequestDTO.getLanguage() == null || subscriberPreferenceRequestDTO.getLanguage().isEmpty() ||
                (!subscriberPreferenceRequestDTO.getLanguage().equalsIgnoreCase("en") && !subscriberPreferenceRequestDTO.getLanguage().equalsIgnoreCase("ar"))) {
            return exceptionHandlerUtil.createErrorResponse("api.error.language");

        }

      Subscriber subscriber =  subscriberRepoIface.findBysubscriberUid(subscriberPreferenceRequestDTO.getSuid());
        if(subscriber == null){
            return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.not.found");
        }

        SubscriberPreferences subscriberPreferencesPresent = subscriberPreferencesRepo.getBySubUid(subscriberPreferenceRequestDTO.getSuid());

        if(subscriberPreferencesPresent!= null){
            return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.preference.already.found");
        }


        SubscriberPreferences subscriberPreferences = new SubscriberPreferences();
        subscriberPreferences.setSuid(subscriberPreferenceRequestDTO.getSuid());
        subscriberPreferences.setLanguagePreferred(subscriberPreferenceRequestDTO.getLanguage());
        subscriberPreferences.setCreatedOn(AppUtil.getDate());
        subscriberPreferences.setUpdatedOn(AppUtil.getDate());
        subscriberPreferencesRepo.save(subscriberPreferences);
        return exceptionHandlerUtil.createSuccessResponse("api.response.subscriber.preferences.saved",null);

    }

    @Override
    public ApiResponse getPreferenceBySuid(String suid) {
        if(suid==null||suid.isEmpty()){
            return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.suid.cantbe.null.or.empty");
        }

        SubscriberPreferences subscriberPreferences = subscriberPreferencesRepo.getBySubUid(suid);
        if(subscriberPreferences == null){
            return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.preference.empty");

        }
        return exceptionHandlerUtil.createSuccessResponse("api.response.subscriber.preference.found",subscriberPreferences);
    }
}
