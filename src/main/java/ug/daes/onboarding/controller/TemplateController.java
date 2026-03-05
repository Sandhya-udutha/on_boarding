
package ug.daes.onboarding.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.constant.Constant;
import ug.daes.onboarding.dto.SubscriberDTO;
import ug.daes.onboarding.dto.TemplateApproveDTO;
import ug.daes.onboarding.dto.TemplateDTO;
import ug.daes.onboarding.service.iface.TemplateServiceIface;


@RestController
@CrossOrigin
public class TemplateController {

    private static final Logger logger = LoggerFactory.getLogger(TemplateController.class);


    private static final String CLASS = "TemplateController";

    private final TemplateServiceIface templateServiceIface;


    public TemplateController(TemplateServiceIface templateServiceIface) {
        this.templateServiceIface = templateServiceIface;
    }

    @GetMapping(Constant.GET_TEMPLATES)
    public ApiResponse getTemplates() {
        return templateServiceIface.getTemplates();
    }

    @PostMapping(Constant.SAVE_TEMPLATES)
    public ApiResponse saveTemplates(@RequestBody TemplateDTO templateDTO) {
        logger.info(CLASS + ">> saveTemplates() >> req {}", templateDTO);
        return templateServiceIface.saveTemplates(templateDTO);
    }

    @GetMapping(value = "/api/auth/get/methods")
    public ApiResponse getMethod() {

        return templateServiceIface.getMethods();
    }

    @GetMapping(value = "/api/auth/get/onboarding-step")
    public ApiResponse getOnBoardingStep() {

        return templateServiceIface.getOnBoardingSteps();
    }

    @GetMapping(value = "/api/auth/get/template-by-id")
    public ApiResponse getTemplateById(@RequestParam int id) {
        logger.info(CLASS + " >> getTemplateById() >> req >> id {} ", id);
        return templateServiceIface.getTemplateById(id);
    }

    @GetMapping(value = "/api/update/template-status")
    public ApiResponse updateTemplateStatus(@RequestParam int id, @RequestParam String status) {
        logger.info(CLASS + " >> updateTemplateStatus() >> req >> id {} and status {}", id, status);
        return templateServiceIface.updateTemplateStatus(id, status);
    }

    @PostMapping(value = "/api/post/activte-template")
    public ApiResponse getActviteTemplate(HttpServletRequest request, @RequestBody SubscriberDTO subscriberDTO) {
        logger.info(CLASS + " >> getActviteTemplate() >> req >>{} ", subscriberDTO);
        return templateServiceIface.getActiveTemplate(subscriberDTO);
    }

    @PostMapping(value = "/api/post/approve-template")
    public ApiResponse approveTemplate(@RequestBody TemplateApproveDTO templateApprove) {
        logger.info(CLASS + " >> approveTemplate() >> req {}", templateApprove);
        return templateServiceIface.templateApprove(templateApprove);

    }

    @PostMapping(value = "/api/post/test/activte-template")
    public ApiResponse testActviteTemplate(@RequestBody SubscriberDTO subscriberDTO) {
        logger.info(CLASS + " >> testActviteTemplate() >> req {}", subscriberDTO);
        return templateServiceIface.testTemplate(subscriberDTO);
    }

    @GetMapping(value = "/api/delete/template-by-id")
    public ApiResponse deleteTemplateById(@RequestParam int id) {
        logger.info(CLASS + " >> deleteTemplateById() >> req >> id {} ", id);
        return templateServiceIface.deleteTemplateById(id);
    }

    @GetMapping(value = "/api/get/template-exist")
    public ApiResponse isTemplateExist(@RequestParam String templateName, String methodName) {
        logger.info(CLASS + ">> isTemplateExist() >> req templateName {} and MethodName {} ", templateName, methodName);
        return templateServiceIface.isTemplateAlreadyExixts(templateName, methodName);
    }

    @GetMapping(value = "/api/get/template-latest")
    public ApiResponse getTemplateLatestById(@RequestParam int id) {
        logger.info(CLASS + " >> getTemplateLatestById() >> req >> id {}", id);
        return templateServiceIface.getTemplateLatestById(id);

    }
}
