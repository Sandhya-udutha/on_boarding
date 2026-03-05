package ug.daes.onboarding.dto;


public class TemplateApproveDTO {


    private int templateId;

    private boolean approve;

    private String remarks;

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "TemplateApproveDTO [templateId=" + templateId + ", approve=" + approve + ", remarks=" + remarks + "]";
    }

}