package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.SubscriberCertificatePinHistory;
@Repository
public interface SubscriberCertPinHistoryRepoIface extends JpaRepository<SubscriberCertificatePinHistory,String>{
	SubscriberCertificatePinHistory findBysubscriberUid(String uid);
}