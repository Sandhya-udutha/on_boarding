
package ug.daes.onboarding.dto;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriberObRequestDTO {


    @NotBlank(message = "Subscriber UID is required and cannot be blank.")
    private String suID;

    private String onboardingMethod;


    private String mobileNo;

    private String emailId;

    private SubscriberObData subscriberData;

    private int templateId;

    private int consentId;

    private String acknowledgement;

    @NotBlank(message = "Subscriber type is required and cannot be empty.")
    private String subscriberType;

    private String onboardingApprovalStatus;

    private String certStatus;

    private String onboardingPaymentStatus;

    private String levelOfAssurance;

    private String title;

    private TotpDtoResp totpResp;

    private List<SubscriberDocuments> subscriberDocuments;

    private CertificateDetailDto certificateDetailDto;

    private String boarderControlPhoto;

    private String niraResponse;

    private String emiratesIdNumber;
    private String passportNumber;
    private String emiratesIdDocumentNumber;

    private JsonNode residenceInfo;
    private JsonNode activePassport;

    public String getSuID() {
        return suID;
    }

    public void setSuID(String suID) {
        this.suID = suID;
    }

    public String getOnboardingMethod() {
        return onboardingMethod;
    }

    public void setOnboardingMethod(String onboardingMethod) {
        this.onboardingMethod = onboardingMethod;
    }

    public SubscriberObData getSubscriberData() {
        return subscriberData;
    }

    public void setSubscriberData(SubscriberObData subscriberData) {
        this.subscriberData = subscriberData;
    }

    public List<SubscriberDocuments> getSubscriberDocuments() {
        return subscriberDocuments;
    }

    public void setSubscriberDocuments(List<SubscriberDocuments> subscriberDocuments) {
        this.subscriberDocuments = subscriberDocuments;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getConsentId() {
        return consentId;
    }

    public void setConsentId(int consentId) {
        this.consentId = consentId;
    }

    public String getAcknowledgement() {
        return acknowledgement;
    }

    public void setAcknowledgement(String acknowledgement) {
        this.acknowledgement = acknowledgement;
    }

    public String getSubscriberType() {
        return subscriberType;
    }

    public void setSubscriberType(String subscriberType) {
        this.subscriberType = subscriberType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOnboardingApprovalStatus() {
        return onboardingApprovalStatus;
    }

    public void setOnboardingApprovalStatus(String onboardingApprovalStatus) {
        this.onboardingApprovalStatus = onboardingApprovalStatus;
    }

    public String getLevelOfAssurance() {
        return levelOfAssurance;
    }

    public void setLevelOfAssurance(String levelOfAssurance) {
        this.levelOfAssurance = levelOfAssurance;
    }

    public String getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(String certStatus) {
        this.certStatus = certStatus;
    }

    public String getOnboardingPaymentStatus() {
        return onboardingPaymentStatus;
    }

    public void setOnboardingPaymentStatus(String onboardingPaymentStatus) {
        this.onboardingPaymentStatus = onboardingPaymentStatus;
    }

    public CertificateDetailDto getCertificateDetailDto() {
        return certificateDetailDto;
    }

    public void setCertificateDetailDto(CertificateDetailDto certificateDetailDto) {
        this.certificateDetailDto = certificateDetailDto;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public TotpDtoResp getTotpResp() {
        return totpResp;
    }

    public void setTotpResp(TotpDtoResp totpResp) {
        this.totpResp = totpResp;
    }

    public String getBoarderControlPhoto() {
        return boarderControlPhoto;
    }

    public void setBoarderControlPhoto(String boarderControlPhoto) {
        this.boarderControlPhoto = boarderControlPhoto;
    }

    public String getNiraResponse() {
        return niraResponse;
    }

    public void setNiraResponse(String niraResponse) {
        this.niraResponse = niraResponse;
    }

    public String getEmiratesIdNumber() {
        return emiratesIdNumber;
    }

    public void setEmiratesIdNumber(String emiratesIdNumber) {
        this.emiratesIdNumber = emiratesIdNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getEmiratesIdDocumentNumber() {
        return emiratesIdDocumentNumber;
    }

    public void setEmiratesIdDocumentNumber(String emiratesIdDocumentNumber) {
        this.emiratesIdDocumentNumber = emiratesIdDocumentNumber;
    }

    public JsonNode getResidenceInfo() {
        return residenceInfo;
    }

    public void setResidenceInfo(JsonNode residenceInfo) {
        this.residenceInfo = residenceInfo;
    }

    public JsonNode getActivePassport() {
        return activePassport;
    }

    public void setActivePassport(JsonNode activePassport) {
        this.activePassport = activePassport;
    }

    @Override
    public String toString() {
        return "SubscriberObRequestDTO [suID=" + suID + ", onboardingMethod=" + onboardingMethod + ", mobileNo="
                + mobileNo + ", emailId=" + emailId + ", subscriberData=" + subscriberData + ", templateId="
                + templateId + ", consentId=" + consentId + ", acknowledgement=" + acknowledgement + ", subscriberType="
                + subscriberType + ", onboardingApprovalStatus=" + onboardingApprovalStatus + ", certStatus="
                + certStatus + ", onboardingPaymentStatus=" + onboardingPaymentStatus + ", levelOfAssurance="
                + levelOfAssurance + ", title=" + title + ", totpResp=" + totpResp + ", subscriberDocuments="
                + subscriberDocuments + ", certificateDetailDto=" + certificateDetailDto + ", boarderControlPhoto="
                + boarderControlPhoto + ", niraResponse=" + niraResponse + ", emiratesIdNumber=" + emiratesIdNumber
                + ", passportNumber=" + passportNumber + ", emiratesIdDocumentNumber=" + emiratesIdDocumentNumber
                + ", residenceInfo=" + residenceInfo + ", activePassport=" + activePassport + "]";
    }
}
