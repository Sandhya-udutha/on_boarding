package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "device_policy")
public class DevicePolicyModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "device_change_policy_hour")
    private int devicePolicyHour;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDevicePolicyHour() {
        return devicePolicyHour;
    }

    public void setDevicePolicyHour(int devicePolicyHour) {
        this.devicePolicyHour = devicePolicyHour;
    }

    @Override
    public String toString() {
        return "DevicePolicyModel [id=" + id + ", devicePolicyHour=" + devicePolicyHour + "]";
    }
}
