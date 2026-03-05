package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ug.daes.onboarding.model.SubscriberContactHistory;

import jakarta.transaction.Transactional;

import java.util.Date;


@Repository
@Transactional
public interface SubscriberHistoryRepo extends JpaRepository<SubscriberContactHistory, Integer> {

    @Query("SELECT MAX(s.createdDate) FROM SubscriberContactHistory s WHERE s.subscriberUid = ?1 AND s.emailId IS NOT NULL")
    Date getLatestForEmail(String suid);

    @Query("SELECT MAX(s.createdDate) FROM SubscriberContactHistory s WHERE s.subscriberUid = ?1 AND s.mobileNumber IS NOT NULL")
    Date getLatestForMobile(String suid);

}
