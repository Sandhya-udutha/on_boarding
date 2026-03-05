
package ug.daes.onboarding.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ug.daes.onboarding.model.Subscriber;
import java.util.List;

@Repository
@Transactional
public interface SubscriberRepoIface extends JpaRepository<Subscriber, Integer> {

	@Query("SELECT COUNT(s.mobileNumber) FROM Subscriber s WHERE s.mobileNumber = ?1")
	int countSubscriberMobile(String mobileNo);

	@Query("SELECT COUNT(s.emailId) FROM Subscriber s WHERE s.emailId = ?1")
	int countSubscriberEmailId(String emailId);

	@Query("SELECT COUNT(d.deviceUid) FROM SubscriberDevice d WHERE d.deviceUid = ?1")
	int countSubscriberDevice(String deviceId);

	@Query("SELECT s FROM Subscriber s WHERE s.mobileNumber = ?1 AND s.emailId = ?2")
	Subscriber findFCMTokenByMobileEamil(String mobileNo, String email);

	Subscriber findByemailId(String emailId);

	Subscriber findBysubscriberUid(String subscriberUid);

	Subscriber findBymobileNumber(String mobileNo);

	Subscriber findByIdDocNumber(String idDocNumber);

	Subscriber findByNationalId(String nationalId);

	@Query("SELECT s FROM Subscriber s WHERE s.idDocNumber = ?1")
	Subscriber getSubscriberByIdDocumentNumber(String idDocumentNumber);

	@Query("SELECT s FROM Subscriber s WHERE s.emailId = ?1 OR s.mobileNumber = ?2")
	Subscriber getSubscriberUidByEmailAndMobile(String email, String mobile);

	@Query("SELECT s FROM Subscriber s WHERE s.emailId = ?1 AND s.mobileNumber = ?2")
	Subscriber getSubscriberDetailsByEmailAndMobile(String email, String mobile);

	@Query("SELECT d.videoUrl FROM SubscriberCompleteDetail d WHERE d.subscriberUid = ?1")
	String getSubscriberUid(String subscriberUid);

	@Query("SELECT COUNT(o.idDocNumber) FROM SubscriberOnboardingData o WHERE o.idDocNumber = ?1 AND o.subscriberUid = ?2")
	int getSubscriberIdDocNumber(String idDocNumber, String subscriberUid);

	@Query("SELECT COUNT(o.idDocNumber) FROM SubscriberOnboardingData o WHERE o.idDocNumber = ?1")
	int getIdDocCount(String idDocNumber);

	@Query("SELECT s.subscriberStatus FROM SubscriberStatusModel s WHERE s.subscriberUid = ?1")
	String getSubscriberStatus(String subscriberUid);


	@Query("SELECT c.certificateStatus FROM SubscriberCertificateLifeCycle c WHERE c.subscriberUid = ?1 AND c.certificateType = 'SIGN' ORDER BY c.createdDate DESC")
	List<String> getCertStatus(String subscriberUid);


	@Query("SELECT COUNT(s) FROM Subscriber s")
	int countOnboarding();


	@Query("SELECT p.paymentStatus FROM SubscriberPaymentHistory p " + "WHERE p.subscriberSuid = :suid "
			+ "AND (p.paymentCategory = 'ONE_TIME_AND_CERT_FEE_COLLECTION' "
			+ "     OR p.paymentCategory = 'USER_SUBSCRIBTION_FEE') "
			+ "AND p.paymentStatus IN ('Success','Failed','Initiated') " + "ORDER BY p.createdOn DESC")
	List<String> subscriberPaymnetStatus(@Param("suid") String suid);

	@Query("SELECT p.paymentStatus FROM SubscriberPaymentHistory p WHERE p.subscriberSuid = ?1 AND p.paymentCategory = 'ONE_TIME_AND_CERT_FEE_COLLECTION' AND p.paymentStatus IN ('Success','Failed','Initiated') ORDER BY p.createdOn DESC")
	String subscriberPaymnetStatusOLD(String suid);

	@Query("SELECT p.paymentStatus FROM SubscriberPaymentHistory p " + "WHERE p.subscriberSuid = ?1 "
			+ "AND (p.paymentCategory = 'CERT_FEE_COLLECTION' "
			+ "     OR p.paymentCategory = 'USER_SUBSCRIBTION_FEE') "
			+ "AND p.paymentStatus IN ('Success','Failed','Initiated') " + "ORDER BY p.createdOn DESC")
	List<String> subscriberPaymnetCertStatus(String suid);


	@Query("SELECT p.paymentStatus FROM SubscriberPaymentHistory p " + "WHERE p.subscriberSuid = ?1 "
			+ "AND (p.paymentCategory = 'ONE_TIME_AND_CERT_FEE_COLLECTION' "
			+ "     OR p.paymentCategory = 'USER_SUBSCRIBTION_FEE') " + "AND p.paymentStatus = 'Initiated' "
			+ "ORDER BY p.createdOn DESC")
	List<String> subscriberPaymnetInitaiatedStatus(String suid);

	@Query("SELECT p.paymentStatus FROM SubscriberPaymentHistory p WHERE p.subscriberSuid = ?1 AND p.paymentCategory = 'ONE_TIME_AND_CERT_FEE_COLLECTION' AND p.paymentStatus = 'Initiated' ORDER BY p.createdOn DESC")
	String subscriberPaymnetInitaiatedStatusOLD(String suid);


	@Query("SELECT p.paymentStatus FROM SubscriberPaymentHistory p " + "WHERE p.subscriberSuid = ?1 "
			+ "AND (p.paymentCategory = 'ONE_TIME_AND_CERT_FEE_COLLECTION' "
			+ "     OR p.paymentCategory = 'USER_SUBSCRIBTION_FEE') " + "AND p.paymentStatus = 'Success' "
			+ "ORDER BY p.createdOn DESC")
	List<String> firstTimeOnboardingPaymentStatus(String suid);

	@Query("SELECT p.paymentStatus FROM SubscriberPaymentHistory p WHERE p.subscriberSuid = ?1 AND p.paymentCategory = 'ONE_TIME_AND_CERT_FEE_COLLECTION' AND p.paymentStatus = 'Success' ORDER BY p.createdOn DESC")
	String firstTimeOnboardingPaymentStatusOLD(String suid);

	@Query("SELECT s FROM Subscriber s WHERE s.idDocNumber = ?1")
	Subscriber findbyDocumentNumber(String idDocument);

	@Query("SELECT s.emailId FROM Subscriber s WHERE s.emailId LIKE %?1%")
	List<String> getSubscriberListByEmailId(String emailId);

	@Query("SELECT s.mobileNumber FROM Subscriber s WHERE s.mobileNumber LIKE %?1%")
	List<String> getSubscriberListByMobileNo(String mobileNo);

	@Query("SELECT b.selfieImage FROM SimulatedBoarderControl b WHERE b.idDocNumber = ?1")
	String getSimulatedBoarderControlImage(String idDocNumber);

	@Query("""
			SELECT
			  CASE
			    WHEN s.passportNumber = :passport THEN 'PASSPORT'
			    WHEN s.nationalIdNumber = :nid THEN 'NATIONAL_ID'
			    WHEN s.nationalIdCardNumber = :nidCard THEN 'NATIONAL_ID_CARD'
			  END
			FROM Subscriber s
			WHERE (:passport IS NOT NULL AND s.passportNumber = :passport)
			   OR (:nid IS NOT NULL AND s.nationalIdNumber = :nid)
			   OR (:nidCard IS NOT NULL AND s.nationalIdCardNumber = :nidCard)
			""")
	List<String> findDuplicateReason(@Param("passport") String passport, @Param("nid") String nid,
			@Param("nidCard") String nidCard);



}
