package ug.daes.onboarding.dto;

public class PolicyDTO {
    private String suid;

    private String deviceUid;


    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getDeviceUid() {
        return deviceUid;
    }

    public void setDeviceUid(String deviceUid) {
        this.deviceUid = deviceUid;
    }


    @Override
    public String toString() {
        return "PolicyDTO{" +
                "suid='" + suid + '\'' +
                ", deviceUid='" + deviceUid + '\'' +
                '}';
    }
}
