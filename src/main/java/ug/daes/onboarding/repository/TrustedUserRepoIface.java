package ug.daes.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ug.daes.onboarding.model.TrustedUser;

import java.util.List;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface TrustedUserRepoIface extends JpaRepository<TrustedUser, Integer> {
    TrustedUser findByemailId(String emailId);

    @Query("SELECT t.emailId FROM TrustedUser t")
    List<String> getTrustedEmails();

    @Query("SELECT t FROM TrustedUser t WHERE t.emailId = ?1")
    TrustedUser getTrustedUserDratilsByEmail(String email);

}
