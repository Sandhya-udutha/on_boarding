package ug.daes.onboarding.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.SubscriberView;

@Repository
@Transactional
public interface SubscriberViewRepoIface extends JpaRepository<SubscriberView, String> {


    @Query("SELECT sv FROM SubscriberView sv WHERE sv.subscriberUid = :suid")
    SubscriberView findSubscriberDetails(@Param("suid") String suid);


    @Query("SELECT sv FROM SubscriberView sv " +
            "WHERE sv.idDocNumber = :documentId " +
            "AND sv.subscriberStatus NOT IN ('CERT_REVOKED', 'INACTIVE', 'CERT_EXPIRED')")
    List<SubscriberView> findSubscriberByDocId(@Param("documentId") String documentId);

    @Query("SELECT sv FROM SubscriberView sv " +
            "WHERE sv.idDocNumber = :documentId " +
            "AND sv.subscriberStatus NOT IN ('CERT_REVOKED', 'INACTIVE', 'CERT_EXPIRED')")
    SubscriberView findSubscriberByDocIdCertRevoked(@Param("documentId") String documentId);
}
