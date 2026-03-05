package ug.daes.onboarding.model;


import jakarta.persistence.*;

import java.util.Date;

@Table(name = "consent_history")
@Entity
public class ConsentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "consent_id")
    private int consentId;

    @Column(name = "consent")
    private String consent;

    @Column(name = "created_On")
    private Date createdOn;

    @Column(name = "optional_terms_and_conditions")
    private String termsAndConditionsBase64;

    @Column(name = "optional_data_and_privacy")
    private String dataPrivacyBase64;

    @Column(name = "consent_type")
    private String consentType;

    @Column(name = "consent_required")
    private boolean consentRequired;

    @Column(name = "privacy_consent")
    private String privacyConsent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConsentId() {
        return consentId;
    }

    public void setConsentId(int consentId) {
        this.consentId = consentId;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getTermsAndConditionsBase64() {
        return termsAndConditionsBase64;
    }

    public void setTermsAndConditionsBase64(String termsAndConditionsBase64) {
        this.termsAndConditionsBase64 = termsAndConditionsBase64;
    }

    public String getDataPrivacyBase64() {
        return dataPrivacyBase64;
    }

    public void setDataPrivacyBase64(String dataPrivacyBase64) {
        this.dataPrivacyBase64 = dataPrivacyBase64;
    }

    public String getConsentType() {
        return consentType;
    }

    public void setConsentType(String consentType) {
        this.consentType = consentType;
    }

    public boolean isConsentRequired() {
        return consentRequired;
    }

    public void setConsentRequired(boolean consentRequired) {
        this.consentRequired = consentRequired;
    }

    public String getPrivacyConsent() {
        return privacyConsent;
    }

    public void setPrivacyConsent(String privacyConsent) {
        this.privacyConsent = privacyConsent;
    }

    @Override
    public String toString() {
        return "ConsentHistory{" +
                "id=" + id +
                ", consentId=" + consentId +
                ", consent='" + consent + '\'' +
                ", createdOn=" + createdOn +
                ", termsAndConditionsBase64='" + termsAndConditionsBase64 + '\'' +
                ", dataPrivacyBase64='" + dataPrivacyBase64 + '\'' +
                ", consentType='" + consentType + '\'' +
                ", consentRequired=" + consentRequired +
                ", privacyConsent='" + privacyConsent + '\'' +
                '}';
    }
}
