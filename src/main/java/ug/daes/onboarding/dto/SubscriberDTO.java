package ug.daes.onboarding.dto;

import java.io.Serializable;
import java.util.Arrays;


public class SubscriberDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    private String mobileNumber;

    private String emailId;

    private String deviceId;

    private String nin;

    private String subscriberUniqueId;

    private Integer template;

    private String name;

    private String nidData;

    private String ePassData;

    private String additionalFields;

    private byte[] docs;

    private String methodName;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getSubscriberUniqueId() {
        return subscriberUniqueId;
    }

    public void setSubscriberUniqueId(String subscriberUniqueId) {
        this.subscriberUniqueId = subscriberUniqueId;
    }

    public Integer getTemplate() {
        return template;
    }

    public void setTemplate(Integer template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNidData() {
        return nidData;
    }

    public void setNidData(String nidData) {
        this.nidData = nidData;
    }

    public String getePassData() {
        return ePassData;
    }

    public void setePassData(String ePassData) {
        this.ePassData = ePassData;
    }

    public String getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(String additionalFields) {
        this.additionalFields = additionalFields;
    }

    public byte[] getDocs() {
        return docs;
    }

    public void setDocs(byte[] docs) {
        this.docs = docs;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "SubscriberDTO [mobileNumber=" + mobileNumber + ", emailId=" + emailId + ", deviceId=" + deviceId
                + ", nin=" + nin + ", subscriberUniqueId=" + subscriberUniqueId + ", template=" + template + ", name="
                + name + ", nidData=" + nidData + ", ePassData=" + ePassData + ", additionalFields=" + additionalFields
                + ", docs=" + Arrays.toString(docs) + ", methodName=" + methodName + "]";
    }
}
