package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.SubscriberCertificate;

import java.util.List;

@Repository
public interface SubscriberCertificatesRepoIface extends JpaRepository<SubscriberCertificate, String> {


	@Query("SELECT s.certificateStatus FROM SubscriberCertificate s " +
			"WHERE s.createdDate = (" +
			"SELECT MAX(sc.createdDate) FROM SubscriberCertificate sc " +
			"WHERE sc.subscriberUid = ?1 AND sc.certificateType = ?2)")
	List<String> getSubscriberCertificateStatus(String uid, String type, String status);


	@Query(value = "SELECT MAX(certificate_status) FROM subscriber_certificate_life_cycle WHERE subscriber_uid = ?1 AND certificate_type = ?2 AND certificate_status = ?3", nativeQuery = true)
	String getSubscriberCertificateStatusLifeHistory(String uid, String type, String status);

	@Query("SELECT s FROM SubscriberCertificate s WHERE s.subscriberUid = ?1 ORDER BY s.createdDate DESC")
	List<SubscriberCertificate> findBySubscriberUniqueId(String subscriberUid);

}