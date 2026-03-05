package ug.daes.onboarding.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.TitleDto;
import ug.daes.onboarding.model.PreferedTitlesModel;
import ug.daes.onboarding.repository.PreferedTitlesRepo;
import ug.daes.onboarding.service.iface.PreferredTitleIface;
import ug.daes.onboarding.util.AppUtil;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class TitleController {
    private static final String PREFERRED_TITLES_ADDED_SUCCESSFULLY = "Preferred titles added successfully";
    private static Logger logger = LoggerFactory.getLogger(TitleController.class);
    private final PreferedTitlesRepo repository;
    private final PreferredTitleIface preferredTitleIface;


    public TitleController(PreferedTitlesRepo repository, PreferredTitleIface preferredTitleIface) {
        this.repository = repository;
        this.preferredTitleIface = preferredTitleIface;
    }

    @GetMapping("/api/get/preferredTitle")
    public ApiResponse getPreferredTitles() {
        return preferredTitleIface.getPreferredTitles();
    }

    @PostMapping("/api/addUpdate/title")
    public ApiResponse addUpdateTitle(@RequestBody TitleDto titleDto) {
        return preferredTitleIface.addUpdateTitle(titleDto);
    }

    @PostMapping("/api/add/preferred-titles")
    public ApiResponse savePreferredTitles(@RequestBody List<String> titles) {
        try {
            List<PreferedTitlesModel> entities = titles.stream()
                    .map(title -> {
                        PreferedTitlesModel pt = new PreferedTitlesModel();
                        pt.setPreferedTitles(title);
                        return pt;
                    })
                    .collect(Collectors.toList());

            List<PreferedTitlesModel> savedTitles = repository.saveAll(entities);

            logger.info(PREFERRED_TITLES_ADDED_SUCCESSFULLY);
            return AppUtil.createApiResponse(true, PREFERRED_TITLES_ADDED_SUCCESSFULLY, savedTitles);
        } catch (Exception e) {
            logger.info("{}", e.getMessage());
            return AppUtil.createApiResponse(false, "Failed to save preferred titles: " + e.getMessage(), null
            );
        }
    }
}
