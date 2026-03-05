package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;


@Entity
@Table(name = "subscriber_devices")
@NamedQuery(name = "SubscriberDevice.findAll", query = "SELECT s FROM SubscriberDevice s")
public class SubscriberDevice implements Serializable {
    private static final long serialVersionUID = 1L;


    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "device_status")
    private String deviceStatus;

    @Column(name = "device_uid")
    private String deviceUid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscriber_device_id")
    private int subscriberDeviceId;

    @Column(name = "subscriber_uid")
    private String subscriberUid;

    @Column(name = "updated_date")
    private String updatedDate;

    /**
     * Default no-argument constructor.
     * <p>
     * Required for frameworks like Jackson for deserialization.
     * </p>
     */

    public SubscriberDevice() {
        // Intentionally empty: required for Jackson deserialization
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDeviceStatus() {
        return this.deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceUid() {
        return this.deviceUid;
    }

    public void setDeviceUid(String deviceUid) {
        this.deviceUid = deviceUid;
    }

    public int getSubscriberDeviceId() {
        return this.subscriberDeviceId;
    }

    public void setSubscriberDeviceId(int subscriberDeviceId) {
        this.subscriberDeviceId = subscriberDeviceId;
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

    @Override
    public String toString() {
        return "{" +
                "createdDate='" + createdDate + '\'' +
                ", deviceStatus='" + deviceStatus + '\'' +
                ", deviceUid='" + deviceUid + '\'' +
                ", subscriberDeviceId=" + subscriberDeviceId +
                ", subscriberUid='" + subscriberUid + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                '}';
    }
}