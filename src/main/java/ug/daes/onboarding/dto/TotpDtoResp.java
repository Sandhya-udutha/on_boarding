package ug.daes.onboarding.dto;



public class TotpDtoResp {


	private String authData;
	private String priauthscheme;
	public String getAuthData() {
		return authData;
	}
	public void setAuthData(String authData) {
		this.authData = authData;
	}
	public String getPriauthscheme() {
		return priauthscheme;
	}
	public void setPriauthscheme(String priauthscheme) {
		this.priauthscheme = priauthscheme;
	}
	@Override
	public String toString() {
		return "TotpDtoResp [authData=" + authData + ", priauthscheme=" + priauthscheme + "]";
	}
}
