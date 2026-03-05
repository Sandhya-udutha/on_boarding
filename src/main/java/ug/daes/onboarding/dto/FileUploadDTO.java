package ug.daes.onboarding.dto;


import org.springframework.web.multipart.MultipartFile;

import ug.daes.onboarding.enums.TypeOfServiceEnum;
import ug.daes.onboarding.enums.VerificationEnum;

public class FileUploadDTO {


    private String subscriberUid;
    private MultipartFile file;
    private String recordedTime;
    private String recordedGeoLocation;
    private VerificationEnum verificationFirst;
    private VerificationEnum verificationSecond;
    private VerificationEnum verificationThird;
    private TypeOfServiceEnum typeOfService;


    public String getSubscriberUid() {
        return subscriberUid;
    }

    public void setSubscriberUid(String subscriberUid) {
        this.subscriberUid = subscriberUid;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(String recordedTime) {
        this.recordedTime = recordedTime;
    }

    public String getRecordedGeoLocation() {
        return recordedGeoLocation;
    }

    public void setRecordedGeoLocation(String recordedGeoLocation) {
        this.recordedGeoLocation = recordedGeoLocation;
    }

    public VerificationEnum getVerificationFirst() {
        return verificationFirst;
    }

    public void setVerificationFirst(VerificationEnum verificationFirst) {
        this.verificationFirst = verificationFirst;
    }

    public VerificationEnum getVerificationSecond() {
        return verificationSecond;
    }

    public void setVerificationSecond(VerificationEnum verificationSecond) {
        this.verificationSecond = verificationSecond;
    }

    public VerificationEnum getVerificationThird() {
        return verificationThird;
    }

    public void setVerificationThird(VerificationEnum verificationThird) {
        this.verificationThird = verificationThird;
    }

    public TypeOfServiceEnum getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(TypeOfServiceEnum typeOfService) {
        this.typeOfService = typeOfService;
    }

}
