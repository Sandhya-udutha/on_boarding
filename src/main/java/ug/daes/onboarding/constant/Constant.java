package ug.daes.onboarding.constant;

public class Constant {
    private Constant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String LOGIN = "/api/auth/login"; //ADMIN , CRO, CMR
    public static final String GET_ACCESS_TOKEN = "/api/auth/get/access-token"; //ADMIN , CRO, CMR
    public static final String RESET_PASSWORD = "/api/auth/password/reset"; //ADMIN , CRO, CMR

    //	Portal Activities
    public static final String GET_ACTIVITIES = "/api/auth/get/activities";
    public static final String SAVE_ACTIVITIES = "/api/auth/save/activities";

    //  Country
    public static final String GET_COUNTRIES = "/api/auth/get/countries";
    public static final String SAVE_COUNTRY = "/api/auth/save/country";

    //  Organisation
    public static final String GET_ORGANIZATIONS = "/api/auth/get/organization";
    public static final String GET_ORGANIZATIONS_BY_ID = "/api/auth/get/organization-by-id";
    public static final String SAVE_ORGANIZATIONS = "/api/auth/save/organization";

    //  Onboarding
    public static final String GET_ONBOARDING_ACTIVITIES = "/api/auth/get/onboarding-activities";
    public static final String SAVE_ONBOARDING_ACTIVITIES = "/api/auth/save/onboarding-activities";
    public static final String WORKFLOW_ONBOARDING = "/api/auth/save/workflow-onboarding";

    //  BussinessUsers
    public static final String GET_BUSINESS_USERS = "/api/auth/get/bussiness-users";
    public static final String SAVE_BUSINESS_USERS = "/api/auth/save/bussiness-users";

    //  Template
    public static final String GET_TEMPLATES = "/api/auth/get/templates";
    public static final String SAVE_TEMPLATES = "/api/auth/save/template";


//  Supporting Docs


    public static final String DEVICE_STATUS_ACTIVE = "ACTIVE";
    public static final String DEVICE_STATUS_DISABLED = "DISABLED";
    public static final String OTP_VERIFIED_STATUS = "VERIFIED";
    public static final String SUBSCRIBER_STATUS = "REGISTERED";
    public static final String SUBSCRIBER_STATUS_INACTIVE = "INACTIVE";
    public static final String SIGN = "SIGN";
    public static final String ACTIVE = "ACTIVE";
    public static final String BOTH = "BOTH";

    public static final String PENDING = "PENDING";

    public static final String PAYMENT_STATUS_PENDING = "Pending";
    public static final String PAYMENT_STATUS_INITIATED = "Initiated";
    public static final String PAYMENT_STATUS_SUCCESS = "Success";
    public static final String PAYMENT_STATUS_FAILED = "Failed";
    public static final String INITIATED = "Initiated";
    public static final String SUCCESS = "Success";

    public static final String FAILED = "FAILED";
    public static final String FAIL = "FAIL";
    public static final String REVOKED = "REVOKED";
    public static final String CERT_REVOKED = "revoked";
    public static final String EXPIRED = "EXPIRED";
    public static final String CERT_EXPIRED = "expired";

    public static final String RESIDENT = "Resident";
    public static final String LOA_UPDATED = "LAO UPDATED";
    public static final String PIN_SET_REQUIRED = "PIN_SET_REQUIRED";
    public static final String ONBOARDED = "ONBOARDED";
    public static final String ONBOARDED_SUCESSFULLY = "Onboarded Successfully";
    public static final String SUBSCRIBER_ONBOARDED = "SUBSCRIBER_ONBOARDED";
    public static final String LOA1 = "LOA1";
    public static final String LOA2 = "LOA2";
    public static final String LOA3 = "LOA3";
    public static final String UNID = "UNID";
    public static final String PASSPORT = "PASSPORT";

    public static final String DEVICE_STATUS_REGISTERED = "REGISTERED";

    public static final String NEW_DEVICE = "NEW";
}
