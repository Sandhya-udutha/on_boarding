
package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ug.daes.onboarding.model.OnboardingSteps;


public interface OnBoardingStepRepoIface extends JpaRepository<OnboardingSteps, Integer> {

}
