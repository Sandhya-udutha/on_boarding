package ug.daes.onboarding.dto;

public class SubscriberDetailsReponseDTO {
    private String suID;
    private String subscriberStatus;
    private SubscriberDetails subscriberDetails;

    public String getSuID() {
        return suID;
    }

    public void setSuID(String suID) {
        this.suID = suID;
    }

    public String getSubscriberStatus() {
        return subscriberStatus;
    }

    public void setSubscriberStatus(String subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }

    public SubscriberDetails getSubscriberDetails() {
        return subscriberDetails;
    }

    public void setSubscriberDetails(SubscriberDetails subscriberDetails) {
        this.subscriberDetails = subscriberDetails;
    }

    @Override
    public String toString() {
        return "SubscriberDetailsReponseDTO{" +
                "suID='" + suID + '\'' +
                ", subscriberStatus='" + subscriberStatus + '\'' +
                ", subscriberDetails=" + subscriberDetails +
                '}';
    }
}
