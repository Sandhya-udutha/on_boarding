package ug.daes.onboarding.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriber_preferences")
public class SubscriberPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "suid")
    private String suid;

    @Column(name = "language_preferred")
    private String languagePreferred;

    @Column(name = "created_on")
    private String createdOn;


    @Column(name = "updated_on")
    private String updatedOn;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getLanguagePreferred() {
        return languagePreferred;
    }

    public void setLanguagePreferred(String languagePreferred) {
        this.languagePreferred = languagePreferred;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        return "SubscriberPreferences{" +
                "id=" + id +
                ", suid='" + suid + '\'' +
                ", languagePreferred='" + languagePreferred + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }
}
