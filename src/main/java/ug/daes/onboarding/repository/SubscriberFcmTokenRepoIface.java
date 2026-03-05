
package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.SubscriberFcmToken;


@Repository
public interface SubscriberFcmTokenRepoIface extends JpaRepository<SubscriberFcmToken, Integer>{

	SubscriberFcmToken findBysubscriberUid(String suid);
	
}
