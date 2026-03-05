package ug.daes.onboarding.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ug.daes.onboarding.model.SubscriberConsents;

@Repository
public interface SubscriberConsentsRepo extends JpaRepository<SubscriberConsents,Integer> {
    @Query("SELECT sc FROM SubscriberConsents sc WHERE sc.suid = ?1 AND sc.consentId = ?2")
    SubscriberConsents findSubscriberConsentBySuidAndConsentId(String suid, int consentId);
}
