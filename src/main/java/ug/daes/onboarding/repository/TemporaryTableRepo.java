package ug.daes.onboarding.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.TemporaryTable;

@Repository
public interface TemporaryTableRepo extends JpaRepository<TemporaryTable, Integer> {


    @Query("SELECT t FROM TemporaryTable t WHERE t.idDocNumber = ?1")
    TemporaryTable getbyidDocNumber(String idDocNumber);

    @Query("SELECT COUNT(t) FROM TemporaryTable t WHERE t.optionalData1 = ?1")
    int getCountOfOptionalData(String optionalData1);

    @Query("SELECT t FROM TemporaryTable t WHERE t.step3Data = ?1")
    TemporaryTable getByMobNumber(String mob);

    @Query("SELECT t FROM TemporaryTable t WHERE t.deviceId = ?1")
    TemporaryTable getByDevice(String deviceId);

    @Query("SELECT t FROM TemporaryTable t WHERE t.step4Data = ?1")
    TemporaryTable getByEmail(String email);

    @Transactional
    @Modifying
    @Query("""
                DELETE FROM TemporaryTable t
                WHERE (:mobile IS NULL OR t.step3Data = :mobile)
                  AND (:email IS NULL OR t.step4Data = :email)
                  AND (:deviceId IS NULL OR t.deviceId = :deviceId)
            """)
    int deleteRecord(@Param("mobile") String mobile, @Param("email") String email, @Param("deviceId") String deviceId);


    @Procedure(procedureName = "ra_0_2.delete_subscriber_temporary_mob_or_email_deviceId")
    int deleteByCriteria(@Param("anyValue1") String anyValue1, @Param("anyValue2") String anyValue2,
                         @Param("anyValue3") String anyValue3);


    @Modifying
    @Transactional
    @Query("DELETE FROM TemporaryTable t WHERE t.idDocNumber = :idDocNumber")
    int deleteRecordByIdDocumentNumber(@Param("idDocNumber") String idDocNumber);

}
