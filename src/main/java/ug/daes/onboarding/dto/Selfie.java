
package ug.daes.onboarding.dto;

public class Selfie {
	

	private String subscriberSelfie;
	private String subscriberUniqueId;
	// Required by Jackson for object deserialization.
	// No initialization logic needed.
	public Selfie() {
		// Intentionally empty: required for Jackson deserialization
	}

	public String getSubscriberSelfie() {
	    return subscriberSelfie;
	}

	public void setSubscriberSelfie(String subscriberSelfie) {
	    this.subscriberSelfie = subscriberSelfie;
	}

	public String getSubscriberUniqueId() {
	    return subscriberUniqueId;
	}

	public void setSubscriberUniqueId(String subscriberUniqueId) {
	    this.subscriberUniqueId = subscriberUniqueId;
	}

	@Override
	public String toString() {
	    return "Selfie{" +
	            "subscriberSelfie='" + subscriberSelfie + '\'' +
	            ", subscriberUniqueId='" + subscriberUniqueId + '\'' +
	            '}';
	}
	
}
