package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;

import java.util.Date;


@Table(name = "assurance_levels")
@NamedQuery(name = "AssuranceLevel.findAll", query = "SELECT a FROM AssuranceLevel a")
public class AssuranceLevelModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "assurance_level")
    private String assuranceLevel;

    @Column(name = "assurance_level_value")
    private int assuranceLevelValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;


    public String getAssuranceLevel() {
        return assuranceLevel;
    }

    public void setAssuranceLevel(String assuranceLevel) {
        this.assuranceLevel = assuranceLevel;
    }

    public int getAssuranceLevelValue() {
        return assuranceLevelValue;
    }

    public void setAssuranceLevelValue(int assuranceLevelValue) {
        this.assuranceLevelValue = assuranceLevelValue;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}