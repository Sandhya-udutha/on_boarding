
package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.OnboardingMethodModel;


@Repository
public interface OnBoardingMethodRepoIface extends JpaRepository<OnboardingMethodModel, Integer> {

    OnboardingMethodModel findByonboardingMethod(String methodName);

}
