package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.SubscriberPersonalDocument;

@Repository
public interface SubscriberPersonalDocumentRepo extends JpaRepository<SubscriberPersonalDocument, Integer>{
	
	SubscriberPersonalDocument findBySubscriberUniqueId(String suid);

}
