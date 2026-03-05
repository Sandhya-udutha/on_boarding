package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.SubscriberDeviceHistory;

import java.util.List;


@Repository
public interface SubscriberDeviceHistoryRepoIface extends JpaRepository<SubscriberDeviceHistory, Integer>{


    @Query("SELECT s FROM SubscriberDeviceHistory s WHERE s.deviceUid = ?1 ORDER BY s.updatedDate DESC")
    List<SubscriberDeviceHistory> findBydeviceUid(String deviceId);

    @Query("SELECT s FROM SubscriberDeviceHistory s WHERE s.deviceUid = ?1 AND s.subscriberUid = ?2")
    List<SubscriberDeviceHistory> findByDeviceUidAndSubscriberUid(String deviceUid, String subscriberUid);

    @Query("SELECT s FROM SubscriberDeviceHistory s WHERE s.deviceUid = ?1 AND s.subscriberUid = ?2 ORDER BY s.updatedDate DESC")
    List<SubscriberDeviceHistory> findByDeviceUidAndSubUid(String deviceUid, String subscriberUid);

    @Query("SELECT s FROM SubscriberDeviceHistory s WHERE s.subscriberUid = ?1 ORDER BY s.updatedDate DESC")
    List<SubscriberDeviceHistory> findBySubscriberUid(String subUID);

    @Query("SELECT s FROM SubscriberDeviceHistory s WHERE s.subscriberUid = ?1 ORDER BY s.createdDate DESC")
    List<SubscriberDeviceHistory> findSubscriberDeviceHistory(String subUID);
    
}
