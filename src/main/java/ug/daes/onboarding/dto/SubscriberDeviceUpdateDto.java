package ug.daes.onboarding.dto;

import java.io.Serializable;

public class SubscriberDeviceUpdateDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private String subscriberUid;
	
	private String fullName;
	
	private String dateOfBirth;
	
	private String idDocType;
	
	private String idDocNumber;
	
	private String mobileNumber;
	
	private String eMail;
	
	private String osName;
	
	private String osVersion;
	
	private String appVersion;
	
	private String deviceInfo;
	
	private String subscriberStatus;
	
	private String deviceStatus;
	
	private String deviceUid;
	
	private String createdDate;
	
	private String updatedDate;

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
	
	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "SubscriberDeviceUpdateDto [subscriberUid=" + subscriberUid + ", fullName=" + fullName + ", dateOfBirth="
				+ dateOfBirth + ", idDocType=" + idDocType + ", idDocNumber=" + idDocNumber + ", mobileNumber="
				+ mobileNumber + ", eMail=" + eMail + ", osName=" + osName + ", osVersion=" + osVersion
				+ ", appVersion=" + appVersion + ", deviceInfo=" + deviceInfo + ", subscriberStatus=" + subscriberStatus
				+ ", deviceStatus=" + deviceStatus + ", deviceUid=" + deviceUid + ", createdDate=" + createdDate
				+ ", updatedDate=" + updatedDate + "]";
	}

}
