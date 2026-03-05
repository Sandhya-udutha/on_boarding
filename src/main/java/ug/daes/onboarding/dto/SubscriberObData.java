
package ug.daes.onboarding.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriberObData implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Date of Birth is required and cannot be left blank.")
    private String dateOfBirth;

    @NotBlank(message = "Date of Expiry is required and cannot be left blank.")
    private String dateOfExpiry;

    @NotBlank(message = "Nationality is required and cannot be left blank.")
    private String nationality;

    @NotBlank(message = "Gender is required and cannot be left blank.")
    private String gender;

    private String primaryIdentifier;

    private String secondaryIdentifier;

    @NotBlank(message = "Document type is required and cannot be empty.")
    private String documentType;

    @NotBlank(message = "Document code is required and cannot be empty.")
    private String documentCode;

    private String optionalData1;

    private String optionalData2;

    @NotBlank(message = "Document number is required and cannot be empty.")
    private String documentNumber;

    private String issuingState;

    private String photoVerificationPerc;

    private String subscriberSelfie;

    private String subscriberUniqueId;

    @NotBlank(message = "Geo location is required and cannot be empty.")
    private String geoLocation;

    private String remarks;

    private String niraResponse;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPrimaryIdentifier() {
        return primaryIdentifier;
    }

    public void setPrimaryIdentifier(String primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
    }

    public String getSecondaryIdentifier() {
        return secondaryIdentifier;
    }

    public void setSecondaryIdentifier(String secondaryIdentifier) {
        this.secondaryIdentifier = secondaryIdentifier;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getOptionalData1() {
        return optionalData1;
    }

    public void setOptionalData1(String optionalData1) {
        this.optionalData1 = optionalData1;
    }

    public String getOptionalData2() {
        return optionalData2;
    }

    public void setOptionalData2(String optionalData2) {
        this.optionalData2 = optionalData2;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getIssuingState() {
        return issuingState;
    }

    public void setIssuingState(String issuingState) {
        this.issuingState = issuingState;
    }

    public String getPhotoVerificationPerc() {
        return photoVerificationPerc;
    }

    public void setPhotoVerificationPerc(String photoVerificationPerc) {
        this.photoVerificationPerc = photoVerificationPerc;
    }

    public String getSubscriberSelfie() {
        return subscriberSelfie;
    }

    public void setSubscriberSelfie(String subscriberSelfie) {
        this.subscriberSelfie = subscriberSelfie;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getSubscriberUniqueId() {
        return subscriberUniqueId;
    }

    public void setSubscriberUniqueId(String subscriberUniqueId) {
        this.subscriberUniqueId = subscriberUniqueId;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNiraResponse() {
        return niraResponse;
    }

    public void setNiraResponse(String niraResponse) {
        this.niraResponse = niraResponse;
    }

    @Override
    public String toString() {
        return "SubscriberObData [dateOfBirth=" + dateOfBirth + ", dateOfExpiry=" + dateOfExpiry + ", nationality="
                + nationality + ", gender=" + gender + ", primaryIdentifier=" + primaryIdentifier
                + ", secondaryIdentifier=" + secondaryIdentifier + ", documentType=" + documentType + ", documentCode="
                + documentCode + ", optionalData1=" + optionalData1 + ", optionalData2=" + optionalData2
                + ", documentNumber=" + documentNumber + ", issuingState=" + issuingState + ", photoVerificationPerc="
                + photoVerificationPerc + ", subscriberSelfie=" + subscriberSelfie + ", subscriberUniqueId="
                + subscriberUniqueId + ", geoLocation=" + geoLocation + ", remarks=" + remarks + ", niraResponse="
                + niraResponse + "]";
    }

}
