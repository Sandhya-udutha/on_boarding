package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.SusbcriberDetailsView;

@Repository
public interface SusbcriberDetailsViewRepo extends JpaRepository<SusbcriberDetailsView, Integer> {

    SusbcriberDetailsView findBysubscriberUid(String subscriberUniqueId);

    @Query("SELECT s.subscriberUid FROM SusbcriberDetailsView s WHERE s.mobileNumber = ?1")
    String findBymobileNumber(String mobileNumber);

    @Query("SELECT s.subscriberUid FROM SusbcriberDetailsView s WHERE s.eMail = ?1")
    String findByemailId(String emailId);


}
