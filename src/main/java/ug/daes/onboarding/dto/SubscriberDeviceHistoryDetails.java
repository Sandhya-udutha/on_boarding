

package ug.daes.onboarding.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ug.daes.onboarding.model.Subscriber;
import ug.daes.onboarding.model.SubscriberDevice;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriberDeviceHistoryDetails implements Serializable {


    private static final long serialVersionUID = 1L;


    private Subscriber subscriber;


    private SubscriberDevice subscriberDevice;


    private transient List<HashMap<String, String>> subscriberDeviceHistory;

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public SubscriberDevice getSubscriberDevice() {
        return subscriberDevice;
    }

    public void setSubscriberDevice(SubscriberDevice subscriberDevice) {
        this.subscriberDevice = subscriberDevice;
    }

    public List<HashMap<String, String>> getSubscriberDeviceHistory() {
        return subscriberDeviceHistory;
    }

    public void setSubscriberDeviceHistory(List<HashMap<String, String>> subscriberDeviceHistory) {
        this.subscriberDeviceHistory = subscriberDeviceHistory;
    }

    @Override
    public String toString() {
        return "SubscriberDeviceHistoryDetails [subscriber=" + subscriber + ", subscriberDevice=" + subscriberDevice
                + ", subscriberDeviceHistory=" + subscriberDeviceHistory + "]";
    }
}
