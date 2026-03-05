package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriber_ra_data")
@NamedQuery(name = "SubscriberRaData.findAll", query = "SELECT s FROM SubscriberRaData s")
public class SubscriberRaData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "certificate_type")
    private String certificateType;

    @Column(name = "common_name")
    private String commonName;

    @Column(name = "country_name")
    private String countryName;


    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "pki_password")
    private String pkiPassword;

    @Column(name = "pki_password_hash")
    private String pkiPasswordHash;

    @Column(name = "pki_user_name")
    private String pkiUserName;

    @Column(name = "pki_user_name_hash")
    private String pkiUserNameHash;

    @Id
    @Column(name = "subscriber_uid")
    private String subscriberUid;

    @Column(name = "updated_date")
    private String updatedDate;


    /**
     * Default no-argument constructor.
     * <p>
     * Required for frameworks like Jackson for deserialization.
     * </p>
     */
    public SubscriberRaData() {
        // Intentionally empty: required for Jackson deserialization
    }

    public String getCertificateType() {
        return this.certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCommonName() {
        return this.commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPkiPassword() {
        return this.pkiPassword;
    }

    public void setPkiPassword(String pkiPassword) {
        this.pkiPassword = pkiPassword;
    }

    public String getPkiPasswordHash() {
        return this.pkiPasswordHash;
    }

    public void setPkiPasswordHash(String pkiPasswordHash) {
        this.pkiPasswordHash = pkiPasswordHash;
    }

    public String getPkiUserName() {
        return this.pkiUserName;
    }

    public void setPkiUserName(String pkiUserName) {
        this.pkiUserName = pkiUserName;
    }

    public String getPkiUserNameHash() {
        return this.pkiUserNameHash;
    }

    public void setPkiUserNameHash(String pkiUserNameHash) {
        this.pkiUserNameHash = pkiUserNameHash;
    }

    public String getSubscriberUid() {
        return this.subscriberUid;
    }

    public void setSubscriberUid(String subscriberUid) {
        this.subscriberUid = subscriberUid;
    }

    public String getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

}