package ug.daes.onboarding.model;

import java.io.Serializable;

import jakarta.persistence.*;


@Entity
@Table(name = "onboarding_methods")
@NamedQuery(name = "OnboardingMethod.findAll", query = "SELECT o FROM OnboardingMethod o")
public class OnboardingMethodModel implements Serializable {
    private static final long serialVersionUID = 1L;


    @Column(name = "level_of_assurance")
    private String levelOfAssurance;

    @Column(name = "onboarding_method")
    private String onboardingMethod;

    @Id
    @Column(name = "onboarding_method_id")
    private String onboardingMethodId;


    public String getLevelOfAssurance() {
        return this.levelOfAssurance;
    }

    public void setLevelOfAssurance(String levelOfAssurance) {
        this.levelOfAssurance = levelOfAssurance;
    }

    public String getOnboardingMethod() {
        return this.onboardingMethod;
    }

    public void setOnboardingMethod(String onboardingMethod) {
        this.onboardingMethod = onboardingMethod;
    }

}