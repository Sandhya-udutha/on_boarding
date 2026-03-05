package ug.daes.onboarding.dto;

import ug.daes.onboarding.model.Subscriber;
import ug.daes.onboarding.model.SubscriberDevice;
import ug.daes.onboarding.model.SubscriberFcmToken;

import ug.daes.onboarding.model.SubscriberStatusModel;

public class SubscriberSetupContext {
    private MobileOTPDto dto;
    private String suid;
    private Subscriber previousSubscriber;

    private Subscriber subscriber;
    private SubscriberDevice device;
    private SubscriberFcmToken fcmToken;
    private SubscriberStatusModel status;
    private SubscriberRegisterResponseDTO responseDTO;


    public MobileOTPDto getDto() {
        return dto;
    }

    public void setDto(MobileOTPDto dto) {
        this.dto = dto;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public Subscriber getPreviousSubscriber() {
        return previousSubscriber;
    }

    public void setPreviousSubscriber(Subscriber previousSubscriber) {
        this.previousSubscriber = previousSubscriber;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public SubscriberDevice getDevice() {
        return device;
    }

    public void setDevice(SubscriberDevice device) {
        this.device = device;
    }

    public SubscriberFcmToken getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(SubscriberFcmToken fcmToken) {
        this.fcmToken = fcmToken;
    }

    public SubscriberStatusModel getStatus() {
        return status;
    }

    public void setStatus(SubscriberStatusModel status) {
        this.status = status;
    }

    public SubscriberRegisterResponseDTO getResponseDTO() {
        return responseDTO;
    }

    public void setResponseDTO(SubscriberRegisterResponseDTO responseDTO) {
        this.responseDTO = responseDTO;
    }
}