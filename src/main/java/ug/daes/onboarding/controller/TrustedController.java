package ug.daes.onboarding.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.model.TrustedUser;
import ug.daes.onboarding.service.iface.TrustedUserIface;

@RestController
public class TrustedController {

    private static final Logger logger = LoggerFactory.getLogger(TrustedController.class);

    private static final String CLASS = "TrustedController";

    private final TrustedUserIface trustedUserIface;


    public TrustedController(TrustedUserIface trustedUserIface) {
        this.trustedUserIface = trustedUserIface;
    }


    @GetMapping("/api/get/trusted/user")
    public ApiResponse getTrustedUserDetails(@RequestParam String email) {
        logger.info(CLASS + " getTrustedUserDetails req >> email {}", email);
        return trustedUserIface.getTrustedUserByEmail(email);
    }

    @PostMapping("/api/post/update/trusted/user")
    public ApiResponse updateTrustedUser(@RequestBody TrustedUser trustedUser) {
        logger.info(CLASS + ">> updateTrustedUser() >> req {} ", trustedUser);
        return trustedUserIface.updateTrustedUser(trustedUser);
    }

    @DeleteMapping("/api/delete/trusted/user")
    public ApiResponse deleteTrustedUser(@RequestParam String email) {
        logger.info(CLASS + ">> deleteTrustedUser() >> req >> email {} ", email);
        return trustedUserIface.deleteTrustedUser(email);
    }

    @GetMapping("/api/get/all/trusted/user")
    public ApiResponse getAllTrustedUser() {

        return trustedUserIface.getAllTrustedUser();
    }

    @PostMapping("/api/post/add/trusted/user")
    public ApiResponse addTrustedUser(@RequestBody List<TrustedUser> trustedUser) {
        logger.info(CLASS + ">> addTrustedUser() >> req {} ", trustedUser);
        return trustedUserIface.addTrustedUser(trustedUser);
    }
}
