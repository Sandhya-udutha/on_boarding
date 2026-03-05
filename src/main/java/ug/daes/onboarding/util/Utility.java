package ug.daes.onboarding.util;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ug.daes.onboarding.constant.ApiResponse;

public class Utility {

    private MessageSource messageSource;

    public Utility(MessageSource messageSource) {
        super();
        this.messageSource = messageSource;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }


    public static String convertToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public ApiResponse createSuccessResponse(String messageKey, Object data) {
        String message = messageSource.getMessage(messageKey, null, Locale.ENGLISH);
        return AppUtil.createApiResponse(true, message, data);
    }
}
