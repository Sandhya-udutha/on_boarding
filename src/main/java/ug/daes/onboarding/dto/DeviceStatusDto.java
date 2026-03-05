package ug.daes.onboarding.dto;

import java.io.Serializable;

public class DeviceStatusDto implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private String deviceStatus;

	private boolean consentRequired;

	private String fcmToken;

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}


	public boolean isConsentRequired() {
		return consentRequired;
	}

	public void setConsentRequired(boolean consentRequired) {
		this.consentRequired = consentRequired;
	}


	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	@Override
	public String toString() {
		return "DeviceStatusDto{" +
				"deviceStatus='" + deviceStatus + '\'' +
				", consentRequired=" + consentRequired +
				", fcmToken='" + fcmToken + '\'' +
				'}';
	}

}
