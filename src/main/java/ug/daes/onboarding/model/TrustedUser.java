package ug.daes.onboarding.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "trusted_users")
@NamedQuery(name = "TrustedUser.findAll", query = "SELECT t FROM TrustedUser t")
public class TrustedUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    private int trustedUserId;
    @Column(name = "email")
    private String emailId;

    @Column(name = "name")
    private String fullName;

    @Column(name = "mobile")
    private String mobileNumber;

    @Column(name = "status")
    private String trustedUserStatus;

    public int getTrustedUserId() {
        return trustedUserId;
    }

    public void setTrustedUserId(int trustedUserId) {
        this.trustedUserId = trustedUserId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTrustedUserStatus() {
        return trustedUserStatus;
    }

    public void setTrustedUserStatus(String trustedUserStatus) {
        this.trustedUserStatus = trustedUserStatus;
    }

    @Override
    public String toString() {
        return "TrustedUser [trustedUserId=" + trustedUserId + ", emailId=" + emailId + ", fullName=" + fullName
                + ", mobileNumber=" + mobileNumber + ", trustedUserStatus=" + trustedUserStatus + "]";
    }
}