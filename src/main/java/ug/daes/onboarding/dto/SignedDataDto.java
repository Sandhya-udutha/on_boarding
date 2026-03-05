package ug.daes.onboarding.dto;

import java.io.Serializable;

public class SignedDataDto implements Serializable {


    private static final long serialVersionUID = 1L;

    private String docData;
    private String subscriberUniqueId;
    private String documentType;

    public String getDocData() {
        return docData;
    }

    public void setDocData(String docData) {
        this.docData = docData;
    }

    public String getSubscriberUniqueId() {
        return subscriberUniqueId;
    }

    public void setSubscriberUniqueId(String subscriberUniqueId) {
        this.subscriberUniqueId = subscriberUniqueId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @Override
    public String toString() {
        return "SignedDataDto [docData=" + docData + ", subscriberUniqueId=" + subscriberUniqueId + ", documentType="
                + documentType + "]";
    }


}
