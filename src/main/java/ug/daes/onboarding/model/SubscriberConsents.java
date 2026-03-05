package ug.daes.onboarding.model;


import jakarta.persistence.*;

import java.io.Serializable;

@Table(name = "subscriber_consents")
@Entity
public class SubscriberConsents implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "subscriber_uid")
    private String suid;

    @Column(name = "consent_data")
    private String consentData;

    @Column(name = "signed_consent_data")
    private String signedConsentData;

    @Column(name = "consent_id")
    private int consentId;

    @Column(name = "created_on")
    private String createdOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getConsentData() {
        return consentData;
    }

    public void setConsentData(String consentData) {
        this.consentData = consentData;
    }

    public String getSignedConsentData() {
        return signedConsentData;
    }

    public void setSignedConsentData(String signedConsentData) {
        this.signedConsentData = signedConsentData;
    }

    public int getConsentId() {
        return consentId;
    }

    public void setConsentId(int consentId) {
        this.consentId = consentId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "SubscriberConsents{" +
                "id=" + id +
                ", suid='" + suid + '\'' +
                ", consentData='" + consentData + '\'' +
                ", signedConsentData='" + signedConsentData + '\'' +
                ", consentId=" + consentId +
                ", createdOn='" + createdOn + '\'' +
                '}';
    }
}
