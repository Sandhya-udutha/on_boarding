package ug.daes.onboarding.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.SubscriberPreferenceRequestDTO;
import ug.daes.onboarding.service.iface.SubscriberPreferencesIface;

@RestController
@RequestMapping("/api")
@Validated
public class SubscriberPreferncesController {

    private static final Logger log = LoggerFactory.getLogger(SubscriberPreferncesController.class);
    private final SubscriberPreferencesIface subscriberPreferencesIface;

    public SubscriberPreferncesController(SubscriberPreferencesIface subscriberPreferencesIface) {
        this.subscriberPreferencesIface = subscriberPreferencesIface;
    }

    @PostMapping("/post/save/subscriber/preferences")
    public ApiResponse saveSubscriberPreferences( @Valid @RequestBody SubscriberPreferenceRequestDTO subscriberPreferenceRequestDTO){

        log.info("Saving language preference for suid: {} language: {}", subscriberPreferenceRequestDTO.getSuid(), subscriberPreferenceRequestDTO.getLanguage());
       return subscriberPreferencesIface.saveLanguagePreference(subscriberPreferenceRequestDTO);
    }

    @GetMapping("/get/subscriber/preferences/by/suid/{suid}")
    public ApiResponse getSubscriberPreferencesBySuid(@PathVariable String suid){
        return subscriberPreferencesIface.getPreferenceBySuid(suid);
    }
}
