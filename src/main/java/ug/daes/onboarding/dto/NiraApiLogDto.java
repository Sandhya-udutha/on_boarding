package ug.daes.onboarding.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NiraApiLogDto implements Serializable {


    private static final long serialVersionUID = 1L;

    private String subscriberUniqueId;

    private String serviceName;

    private String logMessageType;

    private String logMessage;

    public String getSubscriberUniqueId() {
        return subscriberUniqueId;
    }

    public void setSubscriberUniqueId(String subscriberUniqueId) {
        this.subscriberUniqueId = subscriberUniqueId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLogMessageType() {
        return logMessageType;
    }

    public void setLogMessageType(String logMessageType) {
        this.logMessageType = logMessageType;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    @Override
    public String toString() {
        return "NiraApiLogDto [subscriberUniqueId=" + subscriberUniqueId + ", serviceName=" + serviceName
                + ", logMessageType=" + logMessageType + ", logMessage=" + logMessage + "]";
    }

}
