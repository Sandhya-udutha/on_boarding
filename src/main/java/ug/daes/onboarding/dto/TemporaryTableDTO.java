package ug.daes.onboarding.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TemporaryTableDTO {


    private String idDocNumber;
    private String deviceId;

    private String optionalData1;
    private String niraResponse;

    private SubscriberObDetails subscriberObDataDTO;
    private SubscriberDeviceInfoDto subscriberDeviceInfoDto;

    private VideoDetailsDto videoDetailsDto;
    private String mobileNumber;
    private String emailId;
    private String step5Details;
    private int step;
    private Date createdOn;
    private Date updatedOn;

    public String getIdDocNumber() {
        return idDocNumber;
    }

    public void setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getOptionalData1() {
        return optionalData1;
    }

    public void setOptionalData1(String optionalData1) {
        this.optionalData1 = optionalData1;
    }


    public SubscriberObDetails getSubscriberObDataDTO() {
        return subscriberObDataDTO;
    }

    public void setSubscriberObDataDTO(SubscriberObDetails subscriberObDataDTO) {
        this.subscriberObDataDTO = subscriberObDataDTO;
    }

    public SubscriberDeviceInfoDto getSubscriberDeviceInfoDto() {
        return subscriberDeviceInfoDto;
    }

    public void setSubscriberDeviceInfoDto(SubscriberDeviceInfoDto subscriberDeviceInfoDto) {
        this.subscriberDeviceInfoDto = subscriberDeviceInfoDto;
    }

    public VideoDetailsDto getVideoDetailsDto() {
        return videoDetailsDto;
    }

    public void setVideoDetailsDto(VideoDetailsDto videoDetailsDto) {
        this.videoDetailsDto = videoDetailsDto;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStep5Details() {
        return step5Details;
    }

    public void setStep5Details(String step5Details) {
        this.step5Details = step5Details;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getNiraResponse() {
        return niraResponse;
    }

    public void setNiraResponse(String niraResponse) {
        this.niraResponse = niraResponse;
    }

    @Override
    public String toString() {
        return "TemporaryTableDTO [idDocNumber=" + idDocNumber + ", deviceId=" + deviceId + ", optionalData1="
                + optionalData1 + ", niraResponse=" + niraResponse + ", subscriberObDataDTO=" + subscriberObDataDTO
                + ", subscriberDeviceInfoDto=" + subscriberDeviceInfoDto + ", videoDetailsDto=" + videoDetailsDto
                + ", mobileNumber=" + mobileNumber + ", emailId=" + emailId + ", step5Details=" + step5Details
                + ", step=" + step + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + "]";
    }
}
