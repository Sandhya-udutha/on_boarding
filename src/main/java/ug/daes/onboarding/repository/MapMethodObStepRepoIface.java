
package ug.daes.onboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.query.Procedure;

import org.springframework.transaction.annotation.Transactional;

import ug.daes.onboarding.model.MapMethodOnboardingStep;

public interface MapMethodObStepRepoIface extends JpaRepository<MapMethodOnboardingStep, Integer>{

	List<MapMethodOnboardingStep> findBytemplateId(int templateId);

	@Transactional
	@Procedure(procedureName = "delete_map_template_step")
	int deleteBytemplateId(int templateId);
	
}
