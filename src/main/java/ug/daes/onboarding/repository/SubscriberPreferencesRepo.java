package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ug.daes.onboarding.model.SubscriberPreferences;

@Repository
public interface SubscriberPreferencesRepo extends JpaRepository<SubscriberPreferences, Integer> {

    @Query("SELECT s FROM SubscriberPreferences s WHERE s.suid = ?1")
    SubscriberPreferences getBySubUid(String uid);
}
