package ug.daes.onboarding.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.constant.Constant;
import ug.daes.onboarding.dto.EditTemplateDTO;
import ug.daes.onboarding.dto.MobileTemplateDTO;
import ug.daes.onboarding.dto.SubscriberDTO;
import ug.daes.onboarding.dto.TemplateApproveDTO;
import ug.daes.onboarding.dto.TemplateDTO;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.MapMethodOnboardingStep;

import ug.daes.onboarding.model.OnboardingMethodModel;
import ug.daes.onboarding.model.OnboardingSteps;
import ug.daes.onboarding.model.SubscriberOnboardingTemplate;
import ug.daes.onboarding.repository.MapMethodObStepRepoIface;
import ug.daes.onboarding.repository.OnBoardingMethodRepoIface;
import ug.daes.onboarding.repository.OnBoardingStepRepoIface;
import ug.daes.onboarding.repository.OnBoardingTemplateRepoIface;
import ug.daes.onboarding.service.iface.TemplateServiceIface;
import ug.daes.onboarding.util.AppUtil;

@Service
public class OnBoardingTemplateServiceImpl implements TemplateServiceIface {

    private static final Logger logger = LoggerFactory.getLogger(OnBoardingTemplateServiceImpl.class);

    private static final String CLASS = "OnBoardingTemplateServiceImpl";
    private static final String EXCEPTION = "Unexpected exception";
    private static final String TEMPLATE_NOT_FOUND = "api.error.template.not.found";
    private static final String PUBLISHED = "PUBLISHED";
    private static final String UNPUBLISHED = "UNPUBLISHED";
    private static final String RESPONSE_TEMPLATE = "api.response.template";

    private final OnBoardingMethodRepoIface methodRepoIface;
    private final OnBoardingTemplateRepoIface templateRepoIface;
    private final OnBoardingStepRepoIface stepRepoIface;
    private final MapMethodObStepRepoIface mapStepRepoIface;

    private final ExceptionHandlerUtil exceptionHandlerUtil;
    private final OnBoardingTemplateRepoIface templateRepo;

    public OnBoardingTemplateServiceImpl(
            OnBoardingMethodRepoIface methodRepoIface,
            OnBoardingTemplateRepoIface templateRepoIface,
            OnBoardingStepRepoIface stepRepoIface,
            MapMethodObStepRepoIface mapStepRepoIface,

            ExceptionHandlerUtil exceptionHandlerUtil,
            OnBoardingTemplateRepoIface templateRepo) {

        this.methodRepoIface = methodRepoIface;
        this.templateRepoIface = templateRepoIface;
        this.stepRepoIface = stepRepoIface;
        this.mapStepRepoIface = mapStepRepoIface;

        this.exceptionHandlerUtil = exceptionHandlerUtil;
        this.templateRepo = templateRepo;
    }

    @Override
    public ApiResponse getTemplates() {
        List<SubscriberOnboardingTemplate> templates = new ArrayList<>();

        try {
            templates = templateRepoIface.getAllTemplate();

            if (templates != null) {
                logger.info(CLASS + " getTemplates res {}", templates);
                return exceptionHandlerUtil.createSuccessResponse("api.response.template.list", templates);
            } else {
                return exceptionHandlerUtil.successResponse("api.response.template.list.is.empty");
            }
        } catch (Exception e) {
            logger.error(CLASS + " getTemplates Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse getActiveTemplate(SubscriberDTO subscriberDTO) {
        logger.info(CLASS + " SubscriberDTO received: {}", subscriberDTO);
        logger.info(CLASS + " getActiveTemplate req {}", subscriberDTO.getMethodName());
        SubscriberOnboardingTemplate template = new SubscriberOnboardingTemplate();
        EditTemplateDTO templateDTO = new EditTemplateDTO();
        try {
            if (subscriberDTO.getMethodName() != null) {
                template = templateRepoIface.getPublishTemplate(subscriberDTO.getMethodName(), PUBLISHED);

                List<MapMethodOnboardingStep> stepList = mapStepRepoIface.findBytemplateId(template.getTemplateId());

                HashMap<String, MapMethodOnboardingStep> hm = new HashMap<>();
                stepList.forEach(mapMethodOnboardingStep ->
                        hm.put(mapMethodOnboardingStep.getOnboardingStep(), mapMethodOnboardingStep)
                );
                templateDTO.setSteps(hm);
                templateDTO.setTemplateName(template.getTemplateName());
                templateDTO.setTemplateMethod(template.getTemplateMethod());
                templateDTO.setPublishedStatus(template.getPublishedStatus());
                templateDTO.setState(template.getState());
                templateDTO.setTemplateId(template.getTemplateId());

                if (templateDTO != null) {
                    logger.info(CLASS + " getActviteTemplate res Template {}", templateDTO);
                    return exceptionHandlerUtil.createSuccessResponse(RESPONSE_TEMPLATE, templateDTO);
                } else {
                    return exceptionHandlerUtil.createErrorResponse(TEMPLATE_NOT_FOUND);
                }
            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.method.name.is.empty");
            }
        } catch (Exception e) {
            logger.error(CLASS + " getActviteTemplate Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }

    }


    @Override
    public ApiResponse saveTemplates(TemplateDTO templateDTO) {
        logger.info(CLASS + " saveTemplates req {}", templateDTO.getTemplateName());

        if (templateDTO == null) {
            return exceptionHandlerUtil.createErrorResponse("api.error.saving.template.entity.cant.be.null");
        }

        try {
            int count = templateRepo.isTemplateExistWithMethod(templateDTO.getTemplateName(), templateDTO.getTemplateMethod());
            if (count > 0) {
                return exceptionHandlerUtil.createErrorResponse("api.error.template.methodname.alreday.exist");
            }

            SubscriberOnboardingTemplate template = buildOrUpdateTemplate(templateDTO);

            List<MapMethodOnboardingStep> onboardingStepList = buildOnboardingSteps(templateDTO, template);
            List<MapMethodOnboardingStep> onboardingStepSavedList = mapStepRepoIface.saveAll(onboardingStepList);

            if (template != null && onboardingStepSavedList != null) {
                logger.info(CLASS + " saveTemplates  res  Template Saved  {}", template);
                return exceptionHandlerUtil.createSuccessResponse("api.response.template.saved", template);
            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.template.not.saved");
            }

        } catch (Exception e) {
            logger.error(CLASS + " saveTemplates Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    /**
     * Build or update the template object
     */
    private SubscriberOnboardingTemplate buildOrUpdateTemplate(TemplateDTO templateDTO) {
        SubscriberOnboardingTemplate template = new SubscriberOnboardingTemplate();
        template.setTemplateName(templateDTO.getTemplateName());
        template.setTemplateMethod(templateDTO.getTemplateMethod());
        template.setCreatedDate(AppUtil.getDate());
        template.setUpatedDate(AppUtil.getDate());
        template.setApprovedBy(templateDTO.getApprovedBy());

        if (templateDTO.getTemplateId() == 0) {
            template.setCreatedBy(templateDTO.getCreatedBy());
        } else {
            handleExistingTemplate(templateDTO, template);
        }

        template.setPublishedStatus(UNPUBLISHED);
        return templateRepoIface.save(template);
    }

    private void handleExistingTemplate(TemplateDTO templateDTO, SubscriberOnboardingTemplate template) {
        SubscriberOnboardingTemplate templateStatus = templateRepoIface.findBytemplateId(templateDTO.getTemplateId());
        template.setTemplateId(templateDTO.getTemplateId());

        if (templateStatus != null && PUBLISHED.equals(templateStatus.getPublishedStatus())) {
            throw new IllegalStateException("api.response.your.template.status.is.published.please.unpublished.it.before.making.any.modifications");
        }

        template.setUpdatedBy(templateDTO.getUpdatedBy());
        mapStepRepoIface.deleteBytemplateId(templateDTO.getTemplateId());
    }

    private List<MapMethodOnboardingStep> buildOnboardingSteps(TemplateDTO templateDTO, SubscriberOnboardingTemplate template) {
        List<MapMethodOnboardingStep> stepList = new ArrayList<>();
        int i = 1;
        for (OnboardingSteps steps : templateDTO.getSteps()) {
            MapMethodOnboardingStep mapStep = new MapMethodOnboardingStep();
            mapStep.setCreatedDate(AppUtil.getDate());
            mapStep.setIntegrationUrl(steps.getIntegrationUrl());
            mapStep.setMethodName(templateDTO.getTemplateMethod());
            mapStep.setOnboardingStep(steps.getOnboardingStep());
            mapStep.setOnboardingStepThreshold(steps.getOnboardingStepThreshold());
            mapStep.setAndriodTFliteThreshold(steps.getAndriodTFliteThreshold());
            mapStep.setAndriodDTTThreshold(steps.getAndriodDTTThreshold());
            mapStep.setIosTFliteThreshold(steps.getIosTFliteThreshold());
            mapStep.setIosDTTThreshold(steps.getIosDTTThreshold());
            mapStep.setTemplateId(template.getTemplateId());
            mapStep.setSequence(i++);
            stepList.add(mapStep);
        }
        return stepList;
    }

    @Override
    public ApiResponse getTemplateById(int id) {
        logger.info(CLASS + " getTemplateById req  id {}", id);
        SubscriberOnboardingTemplate template = new SubscriberOnboardingTemplate();
        MobileTemplateDTO templateDTO = new MobileTemplateDTO();
        try {
            template = templateRepoIface.findBytemplateId(id);

            List<MapMethodOnboardingStep> stepList = mapStepRepoIface.findBytemplateId(template.getTemplateId());

            for (MapMethodOnboardingStep mapMethodOnboardingStep : stepList) {

                if (mapMethodOnboardingStep.getOnboardingStep().equals("SELFIE_CAPTURING")) {
                    mapMethodOnboardingStep.setOnboardingStepId(1);
                } else if (mapMethodOnboardingStep.getOnboardingStep().equals("MRZ_SCANNING")) {
                    mapMethodOnboardingStep.setOnboardingStepId(2);
                } else if (mapMethodOnboardingStep.getOnboardingStep().equals("PDF417_READING")) {
                    mapMethodOnboardingStep.setOnboardingStepId(3);
                } else if (mapMethodOnboardingStep.getOnboardingStep().equals("NFC")) {
                    mapMethodOnboardingStep.setOnboardingStepId(4);
                } else if (mapMethodOnboardingStep.getOnboardingStep().equals("UNID")) {
                    mapMethodOnboardingStep.setOnboardingStepId(5);
                }
            }
            logger.info(CLASS + " getTemplateById req stepList {}", stepList);
            templateDTO.setSteps(stepList);
            templateDTO.setTemplateName(template.getTemplateName());
            templateDTO.setTemplateMethod(template.getTemplateMethod());
            templateDTO.setPublishedStatus(template.getPublishedStatus());
            templateDTO.setState(template.getState());
            templateDTO.setTemplateId(template.getTemplateId());

            if (template != null) {
                logger.info(CLASS + " getTemplateById res  Template by Id {}", templateDTO);
                return exceptionHandlerUtil.createSuccessResponse("api.response.template.by.id", templateDTO);
            } else {
                return exceptionHandlerUtil.successResponse(TEMPLATE_NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(CLASS + "getTemplateById  Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse getMethods() {

        List<OnboardingMethodModel> methods = new ArrayList<>();

        try {
            methods = methodRepoIface.findAll();
            if (methods == null) {
                return exceptionHandlerUtil.successResponse("api.response.method.list.is.empty");
            } else {
                logger.info(CLASS + " getMethod res Method List {}", methods);
                return exceptionHandlerUtil.createSuccessResponse("api.response.method.list", methods);
            }
        } catch (Exception e) {
            logger.error(CLASS + " getMethod Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse getOnBoardingSteps() {
        List<OnboardingSteps> steps = new ArrayList<>();

        try {
            steps = stepRepoIface.findAll();

            if (steps != null) {
                logger.info(CLASS + " getOnBoardingStep res List of Steps {}", steps);
                return exceptionHandlerUtil.createSuccessResponse("api.response.list.of.steps", steps);
            } else {
                return exceptionHandlerUtil.successResponse("api.response.list.of.steps");

            }
        } catch (Exception e) {
            logger.error(CLASS + " getOnBoardingSteps Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }

    }

    @Override
    public ApiResponse updateTemplateStatus(int id, String status) {
        logger.info(CLASS + " updateTemplateStatus req id  {} and  status {} ", id, status);
        SubscriberOnboardingTemplate template = templateRepoIface.findBytemplateId(id);
        try {

            if (template.getPublishedStatus() == status || template.getPublishedStatus().equals(status)) {
                return exceptionHandlerUtil.createErrorResponse("api.response.template.is.already" + " " + status);
            }
            template.setState(Constant.ACTIVE);
            template.setPublishedStatus(status);
            template = templateRepoIface.save(template);
            if (template != null) {
                logger.info(CLASS + " updateTemplateStatus res Template has been {},  {} ", status, template);
                return exceptionHandlerUtil.createSuccessResponse("api.response.template.has.been" + " " + status,
                        template);
            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.template.status.not.updated");
            }
        } catch (Exception e) {
            logger.error(CLASS + " updateTemplateStatus Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }

    }

    @Override
    public ApiResponse testTemplate(SubscriberDTO subscriberDTO) {
        logger.info(CLASS + " testActviteTemplate req  {}", subscriberDTO);
        SubscriberOnboardingTemplate template = new SubscriberOnboardingTemplate();
        EditTemplateDTO templateDTO = new EditTemplateDTO();
        try {
            if (subscriberDTO.getMethodName() != null) {
                template = templateRepoIface.getPublishTemplate(subscriberDTO.getMethodName(), PUBLISHED);

                List<MapMethodOnboardingStep> stepList = mapStepRepoIface.findBytemplateId(template.getTemplateId());

                HashMap<String, MapMethodOnboardingStep> hm = new HashMap<>();
                stepList.forEach(mapMethodOnboardingStep ->
                        hm.put(mapMethodOnboardingStep.getOnboardingStep(), mapMethodOnboardingStep)
                );
                templateDTO.setSteps(hm);

                templateDTO.setTemplateName(template.getTemplateName());
                templateDTO.setTemplateMethod(template.getTemplateMethod());
                templateDTO.setPublishedStatus(template.getPublishedStatus());
                templateDTO.setState(template.getState());
                templateDTO.setTemplateId(template.getTemplateId());

                if (templateDTO != null) {
                    logger.info(CLASS + " testActviteTemplate res Template {}", templateDTO);
                    return exceptionHandlerUtil.createSuccessResponse(RESPONSE_TEMPLATE, templateDTO);

                } else {
                    return exceptionHandlerUtil.successResponse("api.response.template.not.found");

                }
            } else {
                return exceptionHandlerUtil.successResponse("api.response.method.name.is.empty");

            }
        } catch (Exception e) {
            logger.error(CLASS + " testActviteTemplate Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse templateApprove(TemplateApproveDTO templateApproveDTO) {
        logger.info(CLASS + " approveTemplate req {}", templateApproveDTO);
        try {
            SubscriberOnboardingTemplate template = templateRepoIface
                    .findBytemplateId(templateApproveDTO.getTemplateId());

            if (PUBLISHED.equals(template.getPublishedStatus())) {
                return exceptionHandlerUtil.successResponse(
                        "api.response.your.template.status.is.published.please.unpublished.it.before.making.any.modifications");
            }

            template.setRemarks(templateApproveDTO.getRemarks());

            template = templateRepoIface.save(template);
            if (template != null) {
                logger.info(CLASS + " approveTemplate res Template State Updated {}", template);
                return exceptionHandlerUtil.createSuccessResponse("api.response.template.state.updated", template);
            } else {
                return exceptionHandlerUtil.successResponse("api.response.template.state.not.updated");
            }

        } catch (Exception e) {
            logger.error(CLASS + " approveTemplate Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }
    }

    enum templateApproveEnum {
        NEW, ACTIVE, MODIFIED, DECLINED, DELETE, DELETED
    }

    @Override
    public ApiResponse deleteTemplateById(int id) {
        logger.info(CLASS + " deleteTemplateById req id {}", id);
        try {
            SubscriberOnboardingTemplate template = templateRepoIface.findBytemplateId(id);
            if (template == null) {
                return exceptionHandlerUtil.createErrorResponse(TEMPLATE_NOT_FOUND);

            }
            if (template.getPublishedStatus().equals(PUBLISHED)) {
                return exceptionHandlerUtil.createErrorResponse("api.error.template.is.in.use.cannot.deleted");

            } else if (template.getPublishedStatus().equals(UNPUBLISHED)
                    || template.getPublishedStatus() == UNPUBLISHED) {
                template.setPublishedStatus(templateApproveEnum.DELETED.toString());
                template.setState(templateApproveEnum.MODIFIED.toString());
                template.setUpatedDate(AppUtil.getDate());
                templateRepoIface.save(template);
                logger.info(CLASS + " deleteTemplateById  res  Template Status to DELETED ");
                return exceptionHandlerUtil.successResponse("api.response.template.status.to.deleted");

            } else if (template.getPublishedStatus().equals("DELETED") || template.getPublishedStatus() == "DELETED") {
                return exceptionHandlerUtil.createErrorResponse("api.error.template.already.deleted");

            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.unpublished.the.template.first");

            }
        } catch (Exception e) {
            logger.error(CLASS + " deleteTemplateById  Exception  {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);

        }
    }

    @Override
    public ApiResponse isTemplateAlreadyExixts(String templateName, String methodId) {
        logger.info(CLASS + " isTemplateExist req   templateName {}  and MethodName {} ", templateName, methodId);
        try {
            int a = templateRepoIface.isTemplateExist(templateName);
            if (a == 0) {
                return exceptionHandlerUtil.createErrorResponse("api.error.not.exist");

            } else {
                return exceptionHandlerUtil.successResponse("api.response.exist");

            }
        } catch (Exception e) {
            logger.error(CLASS + " isTemplateAlreadyExixts Exception {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);

        }
    }

    @Override
    public ApiResponse getTemplateLatestById(int id) {
        logger.info(CLASS + " getTemplateLatestById req id  {}", id);
        SubscriberOnboardingTemplate template = templateRepoIface.findBytemplateId(id);
        EditTemplateDTO templateDTO = new EditTemplateDTO();

        try {
            if (template != null) {
                List<MapMethodOnboardingStep> stepList = mapStepRepoIface.findBytemplateId(template.getTemplateId());

                HashMap<String, MapMethodOnboardingStep> hm = new HashMap<>();
                stepList.forEach(mapMethodOnboardingStep ->
                        hm.put(mapMethodOnboardingStep.getOnboardingStep(), mapMethodOnboardingStep)
                );
                templateDTO.setTemplateId(id);
                templateDTO.setSteps(hm);
                templateDTO.setTemplateName(template.getTemplateName());
                templateDTO.setTemplateMethod(template.getTemplateMethod());
                templateDTO.setPublishedStatus(template.getPublishedStatus());
                templateDTO.setState(template.getState());
                templateDTO.setTemplateId(template.getTemplateId());

                if (templateDTO != null) {
                    logger.info(CLASS + " getTemplateLatestById  res  Template  {}", templateDTO);
                    return exceptionHandlerUtil.createSuccessResponse(RESPONSE_TEMPLATE, templateDTO);

                } else {
                    return exceptionHandlerUtil.createErrorResponse(TEMPLATE_NOT_FOUND);

                }
            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.template.is.empty");
            }
        } catch (Exception e) {
            logger.error(CLASS + " getTemplateLatestById  Exception  {}", e.getMessage());
            logger.error(EXCEPTION, e);
            return exceptionHandlerUtil.handleException(e);
        }

    }

}
