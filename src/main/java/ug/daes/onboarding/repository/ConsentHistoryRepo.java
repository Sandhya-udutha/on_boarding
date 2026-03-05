package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ug.daes.onboarding.model.ConsentHistory;


import java.util.List;


@Repository
public interface ConsentHistoryRepo extends JpaRepository<ConsentHistory, Integer> {



    @Query("SELECT c FROM ConsentHistory c WHERE c.consentRequired = true ORDER BY c.createdOn DESC")
    List<ConsentHistory> findLatestConsent();


    ConsentHistory findTopByConsentIdOrderByCreatedOnDesc(int id);



}
