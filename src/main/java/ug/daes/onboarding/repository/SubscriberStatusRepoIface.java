
package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.SubscriberStatusModel;


@Repository
public interface SubscriberStatusRepoIface extends JpaRepository<SubscriberStatusModel, Integer> {


    @Query("SELECT s FROM SubscriberStatus s WHERE s.subscriberUid = :suid")
    SubscriberStatusModel findBysubscriberUid(@Param("suid") String suid);

}
