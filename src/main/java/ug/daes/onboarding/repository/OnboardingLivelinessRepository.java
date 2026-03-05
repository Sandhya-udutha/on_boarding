package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.OnboardingLiveliness;

@Repository
public interface OnboardingLivelinessRepository extends JpaRepository<OnboardingLiveliness, Integer> {
    @Query("SELECT o.url FROM OnboardingLiveliness o WHERE o.subscriberUid = :subscriberUid")
    String getSubscriberUid(@Param("subscriberUid") String subscriberUid);
}
