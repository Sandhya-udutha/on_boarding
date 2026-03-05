package ug.daes.onboarding.model;


import jakarta.persistence.*;

import java.sql.Blob;


@Entity
@Table(name = "photo_features")
public class PhotoFeaturesModel {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int photoFeatureId;

    @Column(name = "subscriber_uid")
    private String suid;

    @Lob
    @Column(name = "photo_features")
    private Blob photoFeatures;

    @Column(name = "created_on")
    private String createdOn;

    @Column(name = "updated_on")
    private String updatedOn;

    public int getPhotoFeatureId() {
        return photoFeatureId;
    }

    public void setPhotoFeatureId(int photoFeatureId) {
        this.photoFeatureId = photoFeatureId;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public Blob getPhotoFeatures() {
        return photoFeatures;
    }

    public void setPhotoFeatures(Blob photoFeatures) {
        this.photoFeatures = photoFeatures;
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
        return "PhotoFeatures{" +
                "photoFeatureId=" + photoFeatureId +
                ", suid='" + suid + '\'' +
                ", photoFeatures=" + photoFeatures +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }
}


