package ug.daes.onboarding.dto;

import jakarta.validation.constraints.NotBlank;

public class SubscriberPreferenceRequestDTO {

    @NotBlank(message = "suid must not be blank")
    private String suid;

    @NotBlank(message = "language must not be blank")
    private String language;

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "SubscriberPreferenceRequestDTO{" +
                "suid='" + suid + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
