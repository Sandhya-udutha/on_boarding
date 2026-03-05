package ug.daes.onboarding.model;

import java.io.File;
import java.io.Serializable;

import jakarta.persistence.*;


@Entity
@Table(name = "onboarding_liveliness")
@NamedQuery(name = "OnboardingLiveliness.findAll", query = "SELECT ol FROM OnboardingLiveliness ol")
public class OnboardingLiveliness implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "subscriber_uid")
    private String subscriberUid;

    @Column(name = "file")
    private File file;

    @Column(name = "recorded_time")
    private String recordedTime;

    @Column(name = "recorded_geo_location")
    private String recordedGeoLocation;

    @Column(name = "verification_first")
    private String verificationFirst;

    @Column(name = "verification_second")
    private String verificationSecond;

    @Column(name = "verification_third")
    private String verificationThird;

    @Column(name = "type_of_service")
    private String typeOfService;

    @Column(name = "url")
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscriberUid() {
        return subscriberUid;
    }

    public void setSubscriberUid(String subscriberUid) {
        this.subscriberUid = subscriberUid;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(String recordedTime) {
        this.recordedTime = recordedTime;
    }

    public String getRecordedGeoLocation() {
        return recordedGeoLocation;
    }

    public void setRecordedGeoLocation(String recordedGeoLocation) {
        this.recordedGeoLocation = recordedGeoLocation;
    }

    public String getVerificationFirst() {
        return verificationFirst;
    }

    public void setVerificationFirst(String verificationFirst) {
        this.verificationFirst = verificationFirst;
    }

    public String getVerificationSecond() {
        return verificationSecond;
    }

    public void setVerificationSecond(String verificationSecond) {
        this.verificationSecond = verificationSecond;
    }

    public String getVerificationThird() {
        return verificationThird;
    }

    public void setVerificationThird(String verificationThird) {
        this.verificationThird = verificationThird;
    }

    public String getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(String typeOfService) {
        this.typeOfService = typeOfService;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
