package ug.daes.onboarding.dto;

public class PinStatus{
	
	private boolean isAuthPinSet;
	private boolean isSignPinSet;

	public PinStatus() {
		// Intentionally empty: required for Jackson deserialization
	}
	
	public boolean isAuthPinSet() {
	return isAuthPinSet;
	}
	
	public void setAuthPinSet(boolean authPinSet) {
	isAuthPinSet = authPinSet;
	}
	
	public boolean isSignPinSet() {
	return isSignPinSet;
	}
	
	public void setSignPinSet(boolean signPinSet) {
	isSignPinSet = signPinSet;
	}
	
	@Override
	public String toString() {
	return "PinStatus{" +
	"isAuthPinSet=" + isAuthPinSet +
	", isSignPinSet=" + isSignPinSet +
	'}';
	}
}