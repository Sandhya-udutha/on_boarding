package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;


@Entity
@Table(name = "subscriber_personal_documents")
@NamedQuery(name = "SubscriberPersonalDocument.findAll", query = "SELECT s FROM SubscriberPersonalDocument s")
public class SubscriberPersonalDocument implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "subscriber_uid")
    private String subscriberUniqueId;
    @Column(name = "document")
    private String document;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "updated_date")
    private String updatedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubscriberUniqueId() {
        return subscriberUniqueId;
    }

    public void setSubscriberUniqueId(String subscriberUniqueId) {
        this.subscriberUniqueId = subscriberUniqueId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "SubscriberPersonalDocument [id=" + id + ", subscriberUniqueId=" + subscriberUniqueId + ", document="
                + document + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
    }

}
