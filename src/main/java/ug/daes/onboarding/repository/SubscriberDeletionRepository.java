package ug.daes.onboarding.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SubscriberDeletionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void deleteSubscriberRecord(String suid) {

        entityManager.createNativeQuery("""
            UPDATE org_subscriber_email 
            SET subscriber_uid = NULL, ugpass_user_link_approved = FALSE 
            WHERE subscriber_uid = :suid
        """).setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_card_details WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM agents_activity WHERE assisted_onboarded_suid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM onboarding_agents WHERE agent_ugpass_suid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("""
            DELETE FROM beneficiary_validity 
            WHERE beneficiary_id IN (
                SELECT id FROM beneficiaries WHERE beneficiary_digital_id = :suid
            )
        """).setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM beneficiaries WHERE beneficiary_digital_id = :suid")
                .setParameter("suid", suid).executeUpdate();


        entityManager.createNativeQuery("DELETE FROM subscriber_consents WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_certificates WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_certificate_life_cycle WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_certificate_pin_history WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_devices_history WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_devices WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_fcm_token WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM onboarding_liveliness WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_onboarding_data WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_ra_data WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_status WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_personal_documents WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        // Update again (as per your original logic)
        entityManager.createNativeQuery("""
            UPDATE org_subscriber_email 
            SET subscriber_uid = NULL, ugpass_user_link_approved = FALSE
            WHERE subscriber_uid = :suid
        """).setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscriber_ugpass_id_card WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();

        entityManager.createNativeQuery("DELETE FROM subscribers WHERE subscriber_uid = :suid")
                .setParameter("suid", suid).executeUpdate();
    }
}

