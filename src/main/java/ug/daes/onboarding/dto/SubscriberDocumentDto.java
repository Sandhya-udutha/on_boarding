package ug.daes.onboarding.dto;

import java.io.Serializable;

public class SubscriberDocumentDto implements Serializable{


	private static final long serialVersionUID = 1L;
	private String subscriberUID;
	private String document;
	public String getSubscriberUID() {
		return subscriberUID;
	}
	public void setSubscriberUID(String subscriberUID) {
		this.subscriberUID = subscriberUID;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	@Override
	public String toString() {
		return "SubscriberDocumentDto [subscriberUID=" + subscriberUID + ", document=" + document + "]";
	}
	

}
