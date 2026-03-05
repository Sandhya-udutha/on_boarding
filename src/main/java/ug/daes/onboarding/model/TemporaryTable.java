package ug.daes.onboarding.model;

import jakarta.persistence.*;

import java.util.Arrays;

@Entity
@Table(name = "temporary_table")
public class TemporaryTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "id_doc_number")
    private String idDocNumber;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "optional_data1")
    private String optionalData1;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "step_1_status")
    private String step1Status;

    @Column(name = "step_1_data")
    private String step1Data;


    @Column(name = "step_2_status")
    private String step2Status;

    @Column(name = "step_2_data")
    private String step2Data;


    @Column(name = "step_3_status")
    private String step3Status;

    @Column(name = "step_3_data")
    private String step3Data;

    @Column(name = "step_4_status")
    private String step4Status;

    @Column(name = "step_4_data")
    private String step4Data;

    @Column(name = "step_5_status")
    private String step5Status;

    @Column(name = "step_5_data")
    private String step5Data;

    @Column(name = "step_completed")
    private int stepCompleted;

    @Column(name = "next_step")
    private int nextStep;

    @Column(name = "nira_response")
    private String niraResponse;

    @Lob
    @Column(name = "liveliness_video")
    private byte[] livelinessVideo;


    @Column(name = "selfie")
    private String selfie;


    @Column(name = "created_on")
    private String createdOn;

    @Column(name = "updated_on")
    private String updatedOn;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getStep1Status() {
        return step1Status;
    }

    public void setStep1Status(String step1Status) {
        this.step1Status = step1Status;
    }

    public String getStep1Data() {
        return step1Data;
    }

    public void setStep1Data(String step1Data) {
        this.step1Data = step1Data;
    }

    public String getStep2Status() {
        return step2Status;
    }

    public void setStep2Status(String step2Status) {
        this.step2Status = step2Status;
    }

    public String getStep2Data() {
        return step2Data;
    }

    public void setStep2Data(String step2Data) {
        this.step2Data = step2Data;
    }

    public String getStep3Status() {
        return step3Status;
    }

    public void setStep3Status(String step3Status) {
        this.step3Status = step3Status;
    }

    public String getStep3Data() {
        return step3Data;
    }

    public void setStep3Data(String step3Data) {
        this.step3Data = step3Data;
    }

    public String getStep4Status() {
        return step4Status;
    }

    public void setStep4Status(String step4Status) {
        this.step4Status = step4Status;
    }

    public String getStep4Data() {
        return step4Data;
    }

    public void setStep4Data(String step4Data) {
        this.step4Data = step4Data;
    }

    public String getStep5Status() {
        return step5Status;
    }

    public void setStep5Status(String step5Status) {
        this.step5Status = step5Status;
    }

    public String getStep5Data() {
        return step5Data;
    }

    public void setStep5Data(String step5Data) {
        this.step5Data = step5Data;
    }

    public int getStepCompleted() {
        return stepCompleted;
    }

    public void setStepCompleted(int stepCompleted) {
        this.stepCompleted = stepCompleted;
    }

    public int getNextStep() {
        return nextStep;
    }

    public void setNextStep(int nextStep) {
        this.nextStep = nextStep;
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


    public byte[] getLivelinessVideo() {
        return livelinessVideo;
    }

    public void setLivelinessVideo(byte[] livelinessVideo) {
        this.livelinessVideo = livelinessVideo;
    }

    public String getSelfie() {
        return selfie;
    }

    public void setSelfie(String selfie) {
        this.selfie = selfie;
    }


    public String getNiraResponse() {
        return niraResponse;
    }

    public void setNiraResponse(String niraResponse) {
        this.niraResponse = niraResponse;
    }

    @Override
    public String toString() {
        return "TemporaryTable [id=" + id + ", idDocNumber=" + idDocNumber + ", deviceId=" + deviceId
                + ", optionalData1=" + optionalData1 + ", deviceInfo=" + deviceInfo + ", step1Status=" + step1Status
                + ", step1Data=" + step1Data + ", step2Status=" + step2Status + ", step2Data=" + step2Data
                + ", step3Status=" + step3Status + ", step3Data=" + step3Data + ", step4Status=" + step4Status
                + ", step4Data=" + step4Data + ", step5Status=" + step5Status + ", step5Data=" + step5Data
                + ", stepCompleted=" + stepCompleted + ", nextStep=" + nextStep + ", niraResponse=" + niraResponse
                + ", livelinessVideo=" + Arrays.toString(livelinessVideo) + ", selfie=" + selfie + ", createdOn="
                + createdOn + ", updatedOn=" + updatedOn + "]";
    }
}
