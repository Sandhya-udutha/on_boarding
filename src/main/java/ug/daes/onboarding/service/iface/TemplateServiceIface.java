package ug.daes.onboarding.service.iface;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.SubscriberDTO;
import ug.daes.onboarding.dto.TemplateApproveDTO;
import ug.daes.onboarding.dto.TemplateDTO;

public interface TemplateServiceIface {


    ApiResponse getTemplates();

    ApiResponse getActiveTemplate(SubscriberDTO subscriberDTO);

    ApiResponse saveTemplates(TemplateDTO template);

    ApiResponse getTemplateById(int id);


    ApiResponse getMethods();


    ApiResponse getOnBoardingSteps();

    ApiResponse updateTemplateStatus(int id, String status);

    ApiResponse testTemplate(SubscriberDTO subscriberDTO);

    ApiResponse templateApprove(TemplateApproveDTO templateId);

    ApiResponse isTemplateAlreadyExixts(String templateName, String methodId);


    ApiResponse deleteTemplateById(int id);


    ApiResponse getTemplateLatestById(int id);

}
