package ug.daes.onboarding.dto;

public class DeviceInfo {
    private String deviceId;
    private String appVersion;
    private String osVersion;


    public DeviceInfo() {
    }


    public DeviceInfo(String deviceId, String appVersion, String osVersion) {
        this.deviceId = deviceId;
        this.appVersion = appVersion;
        this.osVersion = osVersion;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "deviceId='" + deviceId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", osVersion='" + osVersion + '\'' +
                '}';
    }
}
