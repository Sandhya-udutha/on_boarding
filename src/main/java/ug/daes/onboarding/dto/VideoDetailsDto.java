package ug.daes.onboarding.dto;


public class VideoDetailsDto {

    private String recordedTime;
    private String recordedGeoLocation;
    private String verificationFirst;
    private String verificationSecond;
    private String verificationThird;
    private String typeOfService;

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

    public String getVerificationFirst() {
        return verificationFirst;
    }

    public void setVerificationFirst(String verificationFirst) {
        this.verificationFirst = verificationFirst;
    }

    public String getVerificationSecond() {
        return verificationSecond;
    }

    public void setVerificationSecond(String verificationSecond) {
        this.verificationSecond = verificationSecond;
    }

    public String getVerificationThird() {
        return verificationThird;
    }

    public void setVerificationThird(String verificationThird) {
        this.verificationThird = verificationThird;
    }

    public String getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(String typeOfService) {
        this.typeOfService = typeOfService;
    }

    @Override
    public String toString() {
        return "VideoDetailsDto{" +
                "recordedTime='" + recordedTime + '\'' +
                ", recordedGeoLocation='" + recordedGeoLocation + '\'' +
                ", verificationFirst='" + verificationFirst + '\'' +
                ", verificationSecond='" + verificationSecond + '\'' +
                ", verificationThird='" + verificationThird + '\'' +
                ", typeOfService='" + typeOfService + '\'' +
                '}';
    }
}

