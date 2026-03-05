
package ug.daes.onboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.SubscriberOnboardingData;


@Repository
public interface SubscriberOnboardingDataRepoIface extends JpaRepository<SubscriberOnboardingData, Integer> {


    SubscriberOnboardingData findBysubscriberUid(String suid);

    @Query("SELECT s FROM SubscriberOnboardingData s WHERE s.subscriberUid = ?1 ORDER BY s.createdDate DESC")
    List<SubscriberOnboardingData> getBySubUid(String uid); // You can fetch first in Java: list.get(0)

    @Query("SELECT s FROM SubscriberOnboardingData s WHERE s.subscriberUid = ?1 ORDER BY s.createdDate DESC")
    List<SubscriberOnboardingData> findLatestSubscriber(String suid);

    @Query("SELECT COUNT(s) FROM SubscriberOnboardingData s WHERE s.optionalData1 = ?1 AND s.onboardingMethod = 'NIN'")
    int getOptionalData1(String optionalData1);

    @Query("SELECT DISTINCT s.subscriberUid FROM SubscriberOnboardingData s WHERE s.optionalData1 = ?1")
    String getOptionalData1Subscriber(String optionalData1);

    @Query("SELECT s FROM SubscriberOnboardingData s WHERE s.idDocNumber = ?1")
    List<SubscriberOnboardingData> findSubscriberByDocId(String documentNumber);

    @Query("SELECT s FROM SubscriberOnboardingData s")
    List<SubscriberOnboardingData> getAllSelfies();


    @Query("SELECT s FROM SubscriberOnboardingData s WHERE s.idDocNumber = ?1 ORDER BY s.createdDate DESC")
    List<SubscriberOnboardingData> findSubscriberByDocIdLatestRecord(String documentNumber);


}
