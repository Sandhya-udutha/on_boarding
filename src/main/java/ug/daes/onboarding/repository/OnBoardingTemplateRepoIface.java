
package ug.daes.onboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ug.daes.onboarding.model.SubscriberOnboardingTemplate;


@Repository
public interface OnBoardingTemplateRepoIface extends JpaRepository<SubscriberOnboardingTemplate, Integer> {

    SubscriberOnboardingTemplate findBytemplateId(int id);

    SubscriberOnboardingTemplate findBytemplateMethod(String methodName);


    @Query("SELECT t FROM SubscriberOnboardingTemplate t " +
            "WHERE t.templateMethod = :methodName AND t.publishedStatus = :status AND t.state = 'ACTIVE'")
    SubscriberOnboardingTemplate getPublishTemplate(String methodName, String status);


    @Procedure(name = "SubscriberOnboardingTemplate.updatePublishedStatus")
    void updateTemplateStatus(@Param("status") String status, @Param("id") int id);

    @Procedure(procedureName = "delete_map_method_onboarding_step_id")
    void deleteTemplateById(@Param("id") int id);

    @Query("SELECT COUNT(t) FROM SubscriberOnboardingTemplate t WHERE t.templateName = :templateName")
    int isTemplateExist(String templateName);

    @Query("SELECT COUNT(t) FROM SubscriberOnboardingTemplate t " +
            "WHERE t.templateName = :templateName OR t.templateMethod = :method")
    int isTemplateExistWithMethod(String templateName, String method);

    @Query("SELECT s FROM SubscriberOnboardingTemplate s")
    List<SubscriberOnboardingTemplate> getAllTemplate();

}
