package ug.daes.onboarding.controller;

import java.text.ParseException;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ug.daes.onboarding.dto.NiraApiLogDto;
import ug.daes.onboarding.service.iface.LogModelServiceIface;

@RestController
public class LogController {


    private static final Logger logger = LoggerFactory.getLogger(LogController.class);
    private static final String CLASS = "LogController";

    private final LogModelServiceIface logModelServiceIface;

    public LogController(LogModelServiceIface logModelServiceIface) {
        this.logModelServiceIface = logModelServiceIface;
    }

    @PostMapping("/api/save/nira/logs")
    public void saveNiraApiLogs(HttpServletRequest request, @RequestBody NiraApiLogDto niraApiLogDto) throws ParseException {
        logger.info(CLASS + ">> saveNiraApiLogs >> req {} ", niraApiLogDto);
        logModelServiceIface.setLogModelNiraApi(niraApiLogDto);
    }

}
