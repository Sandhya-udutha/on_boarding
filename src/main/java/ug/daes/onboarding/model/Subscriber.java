package ug.daes.onboarding.model;

import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "subscribers")
@NamedQuery(name = "Subscriber.findAll", query = "SELECT s FROM Subscriber s")
public class Subscriber implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscriber_id")
    private int subscriberId;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "id_doc_number")
    private String idDocNumber;

    @Column(name = "id_doc_type")
    private String idDocType;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "subscriber_uid")
    private String subscriberUid;

    @Column(name = "updated_date")
    private String updatedDate;

    @Column(name = "os_name")
    private String osName;

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "title")
    private String title;

    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "national_id_number")
    private String nationalIdNumber;

    @Column(name = "national_id_card_number")
    private String nationalIdCardNumber;

    /**
     * Default no-argument constructor.
     * <p>
     * Required for frameworks like Jackson for deserialization.
     * </p>
     */

    public Subscriber() {
        // Intentionally empty: required for Jackson deserialization
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdDocNumber() {
        return this.idDocNumber;
    }

    public void setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
    }

    public String getIdDocType() {
        return this.idDocType;
    }

    public void setIdDocType(String idDocType) {
        this.idDocType = idDocType;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }


    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }


    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public void setNationalIdNumber(String nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    public String getNationalIdCardNumber() {
        return nationalIdCardNumber;
    }

    public void setNationalIdCardNumber(String nationalIdCardNumber) {
        this.nationalIdCardNumber = nationalIdCardNumber;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "subscriberId=" + subscriberId +
                ", createdDate='" + createdDate + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", emailId='" + emailId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", idDocNumber='" + idDocNumber + '\'' +
                ", idDocType='" + idDocType + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", subscriberUid='" + subscriberUid + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                ", osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", title='" + title + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", nationalIdNumber='" + nationalIdNumber + '\'' +
                ", nationalIdCardNumber='" + nationalIdCardNumber + '\'' +
                '}';
    }


}