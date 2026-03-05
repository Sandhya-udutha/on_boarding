
package ug.daes.onboarding.dto;

import java.io.Serializable;


public class SubscriberDocuments implements Serializable {


    private static final long serialVersionUID = 1L;

    private String documentName;

    private String documentImage;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    @Override
    public String toString() {
        return "SubscriberDocuments [documentName=" + documentName + ", documentImage=" + documentImage + "]";
    }

}
