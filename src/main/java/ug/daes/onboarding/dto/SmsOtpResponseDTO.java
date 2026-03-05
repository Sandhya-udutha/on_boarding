package ug.daes.onboarding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class SmsOtpResponseDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    private String uuid;

    private String sender;

    private String receiver;

    private String text;

    private String url;

    private String operator;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("dlr_url")
    private String dlrUrl;
    @JsonProperty("dlr_status")

    private String dlrStatus;
    @JsonProperty("external_ref")
    private String externalRef;
    @JsonProperty("created_by")
    private String createdBy;

    private String organization;
    @JsonProperty("non_field_errors")
    private List<String> nonFieldErrors;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDlrUrl() {
        return dlrUrl;
    }

    public void setDlrUrl(String dlrUrl) {
        this.dlrUrl = dlrUrl;
    }

    public String getDlrStatus() {
        return dlrStatus;
    }

    public void setDlrStatus(String dlrStatus) {
        this.dlrStatus = dlrStatus;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public List<String> getNonFieldErrors() {
        return nonFieldErrors;
    }

    public void setNonFieldErrors(List<String> nonFieldErrors) {
        this.nonFieldErrors = nonFieldErrors;
    }

    @Override
    public String toString() {
        return "SmsOtpResponseDTO{" +
                "uuid='" + uuid + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                ", operator='" + operator + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", dlrUrl='" + dlrUrl + '\'' +
                ", dlrStatus='" + dlrStatus + '\'' +
                ", externalRef='" + externalRef + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", organization='" + organization + '\'' +
                ", nonFieldErrors=" + nonFieldErrors +
                '}';
    }
}
