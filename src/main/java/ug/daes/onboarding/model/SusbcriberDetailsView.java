package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;


@Entity
@Table(name = "susbcriber_details")
@NamedQuery(name = "SusbcriberDetailsView.findAll", query = "SELECT s FROM SusbcriberDetailsView s")
public class SusbcriberDetailsView implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "subscriber_uid")
    private String subscriberUid;


    @Column(name = "full_name")
    private String fullName;


    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "id_doc_type")
    private String idDocType;


    @Column(name = "id_doc_number")
    private String idDocNumber;

    @Column(name = "mobile_number")
    private String mobileNumber;


    @Column(name = "email_id")
    private String eMail;


    @Column(name = "os_name")
    private String osName;

    @Column(name = "os_version")
    private String osVersion;


    @Column(name = "app_version")
    private String appVersion;


    @Column(name = "device_info")
    private String deviceInfo;


    @Column(name = "subscriber_status")
    private String subscriberStatus;


    @Column(name = "device_status")
    private String deviceStatus;


    @Column(name = "device_uid")
    private String deviceUid;

    public String getSubscriberUid() {
        return subscriberUid;
    }

    public void setSubscriberUid(String subscriberUid) {
        this.subscriberUid = subscriberUid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdDocType() {
        return idDocType;
    }

    public void setIdDocType(String idDocType) {
        this.idDocType = idDocType;
    }

    public String getIdDocNumber() {
        return idDocNumber;
    }

    public void setIdDocNumber(String idDocNumber) {
        this.idDocNumber = idDocNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
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

    public String getSubscriberStatus() {
        return subscriberStatus;
    }

    public void setSubscriberStatus(String subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceUid() {
        return deviceUid;
    }

    public void setDeviceUid(String deviceUid) {
        this.deviceUid = deviceUid;
    }

    @Override
    public String toString() {
        return "SusbcriberDetailsView [subscriberUid=" + subscriberUid + ", fullName=" + fullName + ", dateOfBirth="
                + dateOfBirth + ", idDocType=" + idDocType + ", idDocNumber=" + idDocNumber + ", mobileNumber="
                + mobileNumber + ", eMail=" + eMail + ", osName=" + osName + ", osVersion=" + osVersion
                + ", appVersion=" + appVersion + ", deviceInfo=" + deviceInfo + ", subscriberStatus=" + subscriberStatus
                + ", deviceStatus=" + deviceStatus + ", deviceUid=" + deviceUid + "]";
    }

}