
package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ug.daes.onboarding.model.SubscriberDevice;

import java.util.List;


@Repository
public interface SubscriberDeviceRepoIface extends JpaRepository<SubscriberDevice, Integer> {


    List<SubscriberDevice> findBysubscriberUid(String suid);


    @Query("SELECT sd FROM SubscriberDevice sd WHERE sd.deviceUid = :deviceId ORDER BY sd.updatedDate DESC")
    List<SubscriberDevice> findBydeviceUid(@Param("deviceId") String deviceId);

    SubscriberDevice findTopByDeviceUidOrderByUpdatedDateDesc(String deviceUid);

    @Query("SELECT sd FROM SubscriberDevice sd WHERE sd.deviceUid = :deviceId ORDER BY sd.updatedDate DESC")
    List<SubscriberDevice> findBydeviceDetails(@Param("deviceId") String deviceId);

    @Query("SELECT sd FROM SubscriberDevice sd WHERE sd.deviceUid = :deviceId ORDER BY sd.updatedDate DESC")
    List<SubscriberDevice> findDeviceDetailsById(@Param("deviceId") String deviceId);

    @Query("SELECT sd FROM SubscriberDevice sd WHERE sd.deviceUid = :deviceId AND sd.deviceStatus = :status")
    SubscriberDevice findBydeviceUidAndStatus(@Param("deviceId") String deviceId, @Param("status") String status);

    @Query("SELECT sd FROM SubscriberDevice sd WHERE sd.deviceUid = :deviceId ORDER BY sd.deviceStatus DESC")
    List<SubscriberDevice> findByDeviceUidDetails(@Param("deviceId") String deviceId);

    @Query("SELECT sd FROM SubscriberDevice sd WHERE sd.subscriberUid = :suid AND sd.updatedDate = (" +
            "SELECT MAX(s.updatedDate) FROM SubscriberDevice s WHERE s.subscriberUid = :suid)")
    SubscriberDevice getSubscriber(@Param("suid") String suid);

    @Modifying
    @Transactional
    @Query("UPDATE SubscriberDevice sd SET sd.deviceUid = :deviceUid, sd.deviceStatus = :deviceStatus, " +
            "sd.updatedDate = :updatedDate WHERE sd.id = :subscriberDeviceId")
    int updateSubscriber(@Param("deviceUid") String deviceUid,
                         @Param("deviceStatus") String deviceStatus,
                         @Param("updatedDate") String updatedDate,
                         @Param("subscriberDeviceId") int subscriberDeviceId);

    @Query("SELECT sd FROM SubscriberDevice sd WHERE sd.subscriberUid = :suid")
    List<SubscriberDevice> getSubscriberDeviceStatus(@Param("suid") String suid);

}
