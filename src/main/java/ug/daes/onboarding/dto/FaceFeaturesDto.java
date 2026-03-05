package ug.daes.onboarding.dto;

public class FaceFeaturesDto {
    private String suid;
    private String subscriberName;
    private String subscriberPhoto;
    private String subscriberFeature;
    private String subscriberDataJson;

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getSubscriberPhoto() {
        return subscriberPhoto;
    }

    public void setSubscriberPhoto(String subscriberPhoto) {
        this.subscriberPhoto = subscriberPhoto;
    }

    public String getSubscriberFeature() {
        return subscriberFeature;
    }

    public void setSubscriberFeature(String subscriberFeature) {
        this.subscriberFeature = subscriberFeature;
    }

    public String getSubscriberDataJson() {
        return subscriberDataJson;
    }

    public void setSubscriberDataJson(String subscriberDataJson) {
        this.subscriberDataJson = subscriberDataJson;
    }

    @Override
    public String toString() {
        return "FaceFeaturesDto{" +
                "suid='" + suid + '\'' +
                ", subscriberName='" + subscriberName + '\'' +
                ", subscriberPhoto='" + subscriberPhoto + '\'' +
                ", subscriberFeature='" + subscriberFeature + '\'' +
                ", subscriberDataJson='" + subscriberDataJson + '\'' +
                '}';
    }
}
