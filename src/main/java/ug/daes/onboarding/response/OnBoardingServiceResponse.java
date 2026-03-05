package ug.daes.onboarding.response;


import org.springframework.context.MessageSource;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.util.AppUtil;

import java.util.Locale;

public class OnBoardingServiceResponse {
    private final MessageSource messageSource;


    public OnBoardingServiceResponse(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ApiResponse createApiResponse(boolean success, String message, Object result) {
        return AppUtil.createApiResponse(success, messageSource.getMessage(message, null, Locale.ENGLISH), result);
    }
}
