package ug.daes.onboarding.dto;

import java.io.Serializable;

public class CertificateDetailDto implements Serializable{


	private static final long serialVersionUID = 1L;
	private String certStatus;
	private String issueDate;
	private String expiryDate;
	private String revokeDate;
	public String getCertStatus() {
		return certStatus;
	}
	public void setCertStatus(String certStatus) {
		this.certStatus = certStatus;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getRevokeDate() {
		return revokeDate;
	}
	public void setRevokeDate(String revokeDate) {
		this.revokeDate = revokeDate;
	}
	@Override
	public String toString() {
		return "CertificateDetailDto [certStatus=" + certStatus + ", issueDate=" + issueDate + ", expiryDate="
				+ expiryDate + ", revokeDate=" + revokeDate + "]";
	}
}
