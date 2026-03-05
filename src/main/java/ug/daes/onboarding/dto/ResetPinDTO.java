package ug.daes.onboarding.dto;

public class ResetPinDTO {

	private String idDocNumber;
	
	private String selfie;

	public String getIdDocNumber() {
		return idDocNumber;
	}

	public void setIdDocNumber(String idDocNumber) {
		this.idDocNumber = idDocNumber;
	}

	public String getSelfie() {
		return selfie;
	}

	public void setSelfie(String selfie) {
		this.selfie = selfie;
	}

	@Override
	public String toString() {
		return "ResetPinDTO [idDocNumber=" + idDocNumber + ", selfie=" + selfie + "]";
	}	
}
