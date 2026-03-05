package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ug.daes.onboarding.model.OnboardingStepDetails;

import java.util.List;

@Repository

public interface OnboardingStepDetailsRepoIface extends JpaRepository<OnboardingStepDetails, Integer> {

    @Query("SELECT COUNT(o.stepId) FROM OnboardingStepDetails o")
    int getNoOfOnboardingSteps();

    @Query("SELECT o FROM OnboardingStepDetails o WHERE o.stepId = ?1")
    OnboardingStepDetails getStepDetails(int stepId);

    @Query("SELECT o FROM OnboardingStepDetails o")
    List<OnboardingStepDetails> getAllSteps();

}
