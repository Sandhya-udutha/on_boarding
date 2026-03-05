package ug.daes.onboarding.dto;

import java.io.Serializable;
import java.util.List;

public class TrustedUserDto {


    private List<TrustedEmails> emails;

    private String createdBy;

    private String updatedBy;

    public List<TrustedEmails> getEmails() {
        return emails;
    }

    public void setEmails(List<TrustedEmails> emails) {
        this.emails = emails;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "TrustedUserDto [emails=" + emails + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
    }

}
