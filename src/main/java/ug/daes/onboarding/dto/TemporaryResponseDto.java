package ug.daes.onboarding.dto;

import ug.daes.onboarding.model.OnboardingStepDetails;
import ug.daes.onboarding.model.Subscriber;

import java.util.List;

public class TemporaryResponseDto {

    private String idDocNumber;
    private String deviceId;

    private String optionalData1;

    private SubscriberObDetails subscriberObDetails;
    private SubscriberDeviceInfoDto subscriberDeviceInfoDto;

    private String step1Status;
    private VideoDetailsDto videoDetailsDto;
    private String step2Status;
    private String mobileNumber;
    private String step3Status;
    private String emailId;
    private String step4Status;
    private String step5Details;
    private String step5Status;
    private int nextStep;
    private int stepCompleted;
    private String createdOn;
    private String updatedOn;
    private List<OnboardingStepDetails> onboardingStepDetails;
    private Subscriber subscriber;
    private boolean usedDevice;
    private boolean newDevice;
    private boolean dataInTemporaryTable;
    private boolean existingSubscriber;
    private boolean existingSubscriberDevice;
    private boolean newMobileNumber;
    private boolean usedMobNumber;
    private boolean newEmailId;
    private boolean usedEmail;
    private String selfieImage;


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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public List<OnboardingStepDetails> getOnboardingStepDetails() {
        return onboardingStepDetails;
    }

    public void setOnboardingStepDetails(List<OnboardingStepDetails> onboardingStepDetails) {
        this.onboardingStepDetails = onboardingStepDetails;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public boolean isUsedDevice() {
        return usedDevice;
    }

    public void setUsedDevice(boolean usedDevice) {
        this.usedDevice = usedDevice;
    }

    public boolean isNewDevice() {
        return newDevice;
    }

    public void setNewDevice(boolean newDevice) {
        this.newDevice = newDevice;
    }

    public boolean isDataInTemporaryTable() {
        return dataInTemporaryTable;
    }

    public void setDataInTemporaryTable(boolean dataInTemporaryTable) {
        this.dataInTemporaryTable = dataInTemporaryTable;
    }

    public boolean isExistingSubscriber() {
        return existingSubscriber;
    }

    public void setExistingSubscriber(boolean existingSubscriber) {
        this.existingSubscriber = existingSubscriber;
    }

    public SubscriberObDetails getSubscriberObDetails() {
        return subscriberObDetails;
    }

    public void setSubscriberObDetails(SubscriberObDetails subscriberObDetails) {
        this.subscriberObDetails = subscriberObDetails;
    }

    public SubscriberDeviceInfoDto getSubscriberDeviceInfoDto() {
        return subscriberDeviceInfoDto;
    }

    public void setSubscriberDeviceInfoDto(SubscriberDeviceInfoDto subscriberDeviceInfoDto) {
        this.subscriberDeviceInfoDto = subscriberDeviceInfoDto;
    }

    public int getNextStep() {
        return nextStep;
    }

    public void setNextStep(int nextStep) {
        this.nextStep = nextStep;
    }

    public int getStepCompleted() {
        return stepCompleted;
    }

    public void setStepCompleted(int stepCompleted) {
        this.stepCompleted = stepCompleted;
    }

    public String getStep1Status() {
        return step1Status;
    }

    public void setStep1Status(String step1Status) {
        this.step1Status = step1Status;
    }

    public String getStep2Status() {
        return step2Status;
    }

    public void setStep2Status(String step2Status) {
        this.step2Status = step2Status;
    }

    public String getStep3Status() {
        return step3Status;
    }

    public void setStep3Status(String step3Status) {
        this.step3Status = step3Status;
    }

    public String getStep4Status() {
        return step4Status;
    }

    public void setStep4Status(String step4Status) {
        this.step4Status = step4Status;
    }

    public String getStep5Status() {
        return step5Status;
    }

    public void setStep5Status(String step5Status) {
        this.step5Status = step5Status;
    }

    public boolean isNewMobileNumber() {
        return newMobileNumber;
    }

    public void setNewMobileNumber(boolean newMobileNumber) {
        this.newMobileNumber = newMobileNumber;
    }

    public boolean isNewEmailId() {
        return newEmailId;
    }

    public void setNewEmailId(boolean newEmailId) {
        this.newEmailId = newEmailId;
    }

    public boolean isUsedMobNumber() {
        return usedMobNumber;
    }

    public void setUsedMobNumber(boolean usedMobNumber) {
        this.usedMobNumber = usedMobNumber;
    }

    public boolean isUsedEmail() {
        return usedEmail;
    }

    public void setUsedEmail(boolean usedEmail) {
        this.usedEmail = usedEmail;
    }

    public boolean isExistingSubscriberDevice() {
        return existingSubscriberDevice;
    }

    public void setExistingSubscriberDevice(boolean existingSubscriberDevice) {
        this.existingSubscriberDevice = existingSubscriberDevice;
    }

    public String getSelfieImage() {
        return selfieImage;
    }

    public void setSelfieImage(String selfieImage) {
        this.selfieImage = selfieImage;
    }

    @Override
    public String toString() {
        return "TemporaryResponseDto{" +
                "idDocNumber='" + idDocNumber + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", optionalData1='" + optionalData1 + '\'' +
                ", subscriberObDetails=" + subscriberObDetails +
                ", subscriberDeviceInfoDto=" + subscriberDeviceInfoDto +
                ", step1Status='" + step1Status + '\'' +
                ", videoDetailsDto=" + videoDetailsDto +
                ", step2Status='" + step2Status + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", step3Status='" + step3Status + '\'' +
                ", emailId='" + emailId + '\'' +
                ", step4Status='" + step4Status + '\'' +
                ", step5Details='" + step5Details + '\'' +
                ", step5Status='" + step5Status + '\'' +
                ", nextStep=" + nextStep +
                ", stepCompleted=" + stepCompleted +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", onboardingStepDetails=" + onboardingStepDetails +
                ", subscriber=" + subscriber +
                ", usedDevice=" + usedDevice +
                ", newDevice=" + newDevice +
                ", dataInTemporaryTable=" + dataInTemporaryTable +
                ", existingSubscriber=" + existingSubscriber +
                ", existingSubscriberDevice=" + existingSubscriberDevice +
                ", newMobileNumber=" + newMobileNumber +
                ", usedMobNumber=" + usedMobNumber +
                ", newEmailId=" + newEmailId +
                ", usedEmail=" + usedEmail +
                ", selfieImage='" + selfieImage + '\'' +
                '}';
    }
}
