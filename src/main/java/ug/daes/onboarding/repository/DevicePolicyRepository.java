package ug.daes.onboarding.repository;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.DevicePolicyModel;

@Repository
@Transactional
public interface DevicePolicyRepository extends JpaRepository<DevicePolicyModel, Integer> {

    @Query("SELECT d FROM DevicePolicyModel d")
    DevicePolicyModel getDevicePolicyHour();
}
