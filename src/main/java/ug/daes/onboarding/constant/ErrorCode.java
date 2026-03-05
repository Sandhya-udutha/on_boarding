package ug.daes.onboarding.constant;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {

    protected static final Map<String, String> map = new HashMap<>();
    private static final String ERROR_CODE_104052 = "104052";

    private ErrorCode() {
        throw new IllegalStateException("Utility class");
    }

    static {
        map.put("104040", "Bad Request");
        map.put("104041", "Unauthorized");
        map.put("104043", "Forbidden");
        map.put("104044", "Not Found");
        map.put("104045", "Method Not Allowed");
        map.put("104046", "Unsupported Media Type");
        map.put("104048", "Request Timeout");
        map.put("104050", "Internal Server Error");
        map.put(ERROR_CODE_104052, "Bad Gateway");
        map.put("104053", "Service Unavailable");
        map.put("104054", "Gateway Timeout");

        map.put("Illegal State Exception_consent-service", "104061");
        map.put("Illegal Argument Exception_consent-service", "104062");
        map.put("JsonMapping Exception_consent-service", "104063");
        map.put("Null pointer Exception_consent-service", "104064");
        map.put("Illegal State Exception_Delete-service", "104071");
        map.put("Illegal Argument Exception_Delete-service", "104072");
        map.put("JsonMapping Exception_Delete-service", "104073");
        map.put("Null pointer Exception_Delete-service", "104074");
        map.put("Illegal State Exception_OTP-service", "104081");
        map.put("Illegal Argument Exception_OTP-service", "104082");
        map.put("JsonMapping Exception_OTP-service", "104083");
        map.put("Null pointer Exception_OTP-service", "104084");
        map.put("Illegal State Exception_Ra-service", "104091");
        map.put("Illegal Argument Exception_Ra-service", "104092");
        map.put("JsonMapping Exception_Ra-service", "104093");
        map.put("Null pointer Exception_Ra-service", "104094");
        map.put("Illegal State Exception_SendNotification-service", "104101");
        map.put("Illegal Argument Exception_SendNotification-service", "104102");
        map.put("JsonMapping Exception_SendNotification-service", "104103");
        map.put("Null pointer Exception_SendNotification-service", "104104");
        map.put("Illegal State Exception_Template-service", "104111");
        map.put("Illegal Argument Exception_Template-service", "104112");
        map.put("JsonMapping Exception_Template-service", "104113");
        map.put("Null pointer Exception_Template-service", "104114");
        map.put("Illegal State Exception_AppConfig-service", "104115");
        map.put("Illegal Argument Exception_AppConfig-service", "104116");
        map.put("JsonMapping Exception_AppConfig-service", "104117");
        map.put("Null pointer Exception_AppConfig-service", "104118");
        map.put("404", "104044");
        map.put("400", "104040");
        map.put("401", "104041");
        map.put("403", "104043");
        map.put("405", "104045");
        map.put("408", "104048");
        map.put("415", "104046");
        map.put("500", "104050");

        map.put("503", "104053");
        map.put("EXCEPTION_ConsentService", "104060");
        map.put("EXCEPTION_DeleteService", "104070");
        map.put("EXCEPTION_OTPService", "104080");
        map.put("EXCEPTION_RaService", "104090");
        map.put("EXCEPTION_SendNotification", "104100");
        map.put("EXCEPTION_TemplateService", "104110");
        map.put("EXCEPTION_AppConfigService", "104120");
    }
}
