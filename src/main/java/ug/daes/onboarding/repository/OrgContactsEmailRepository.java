package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.OrgContactsEmail;

@Repository
public interface OrgContactsEmailRepository extends JpaRepository<OrgContactsEmail, Integer> {

    @Query("SELECT COUNT(ose) FROM OrgContactsEmail ose WHERE ose.employeeEmail = ?1 AND ose.ugpassEmail <> ?2")
    int findByOrgEmailAndNotUgPassEmail(String organizationEmail, String ugpassEmail);


    @Query("SELECT COUNT(ose) FROM OrgContactsEmail ose WHERE ose.employeeEmail = ?1 AND ose.mobileNumber <> ?2")
    int findByOrgEmailAndNotMobile(String organizationEmail, String mobileNumber);

    @Query("SELECT COUNT(ose) FROM OrgContactsEmail ose WHERE ose.employeeEmail = ?1 AND ose.passportNumber <> ?2")
    int findByOrgEmailAndNotPassport(String organizationEmail, String passportNumber);

    @Query("SELECT COUNT(ose) FROM OrgContactsEmail ose WHERE ose.employeeEmail = ?1 AND ose.nationalIdNumber <> ?2")
    int findByOrgEmailAndNotNin(String organizationEmail, String nationalIdNumber);


}
