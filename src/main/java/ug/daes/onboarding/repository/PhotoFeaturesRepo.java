package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ug.daes.onboarding.model.PhotoFeaturesModel;


@Repository
public interface PhotoFeaturesRepo extends JpaRepository<PhotoFeaturesModel,Integer> {

}
