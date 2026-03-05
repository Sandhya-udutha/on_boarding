package ug.daes.onboarding.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriberObDetails {


        private String subscriberName;
        private String subscriberType;
        private String documentNumber;
        private String onboardingMethod;
        private int templateID;
        private String dateOfBirth;
        private String dateOfExpiry;
        private String nationality;
        private String gender;
        private String primaryIdentifier;
        private String optionalData1;
        private String optionalData2;
        private String secondaryIdentifier;
        private String documentType;
        private String documentCode;
        private String issuingState;
        private String geoLocation;
        private String remarks;

        // Getters and setters
        public String getSubscriberName() {
            return subscriberName;
        }

        public void setSubscriberName(String subscriberName) {
            this.subscriberName = subscriberName;
        }

        public String getSubscriberType() {
            return subscriberType;
        }

        public void setSubscriberType(String subscriberType) {
            this.subscriberType = subscriberType;
        }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getOnboardingMethod() {
            return onboardingMethod;
        }

        public void setOnboardingMethod(String onboardingMethod) {
            this.onboardingMethod = onboardingMethod;
        }

        public int getTemplateID() {
            return templateID;
        }

        public void setTemplateID(int templateID) {
            this.templateID = templateID;
        }

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

        public String getDocumentCode() {
            return documentCode;
        }

        public void setDocumentCode(String documentCode) {
            this.documentCode = documentCode;
        }

        public String getIssuingState() {
            return issuingState;
        }

        public void setIssuingState(String issuingState) {
            this.issuingState = issuingState;
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

    @Override
    public String toString() {
        return "SubscriberObDetails{" +
                "subscriberName='" + subscriberName + '\'' +
                ", subscriberType='" + subscriberType + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", onboardingMethod='" + onboardingMethod + '\'' +
                ", templateID=" + templateID +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", dateOfExpiry='" + dateOfExpiry + '\'' +
                ", nationality='" + nationality + '\'' +
                ", gender='" + gender + '\'' +
                ", primaryIdentifier='" + primaryIdentifier + '\'' +
                ", optionalData1='" + optionalData1 + '\'' +
                ", optionalData2='" + optionalData2 + '\'' +
                ", secondaryIdentifier='" + secondaryIdentifier + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentCode='" + documentCode + '\'' +
                ", issuingState='" + issuingState + '\'' +
                ", geoLocation='" + geoLocation + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}



