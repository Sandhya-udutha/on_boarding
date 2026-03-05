package ug.daes.onboarding.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.TitleDto;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.Subscriber;
import ug.daes.onboarding.repository.PreferedTitlesRepo;
import ug.daes.onboarding.repository.SubscriberRepoIface;
import ug.daes.onboarding.service.iface.PreferredTitleIface;


import java.util.List;


@Service
public class PreferredTitleImpl implements PreferredTitleIface {
    private static Logger logger = LoggerFactory.getLogger(PreferredTitleImpl.class);

    private final ExceptionHandlerUtil exceptionHandlerUtil;
    private final SubscriberRepoIface subscriberRepoIface;
    private final PreferedTitlesRepo preferedTitlesRepol;


    public PreferredTitleImpl(ExceptionHandlerUtil exceptionHandlerUtil,
                              SubscriberRepoIface subscriberRepoIface,
                              PreferedTitlesRepo preferedTitlesRepol) {
        this.exceptionHandlerUtil = exceptionHandlerUtil;
        this.subscriberRepoIface = subscriberRepoIface;
        this.preferedTitlesRepol = preferedTitlesRepol;
    }

    @Override
    public ApiResponse getPreferredTitles() {
        try {
            List<String> preferedTitlesList = preferedTitlesRepol.getPreferedTitles();
            logger.info("preferedTitlesList {}", preferedTitlesList);
            return exceptionHandlerUtil.createSuccessResponse("api.response.title.fetched", preferedTitlesList);

        } catch (Exception e) {
            logger.error("Unexpected exception", e);
            return exceptionHandlerUtil.handleHttpException(e);
        }

    }

    @Override
    public ApiResponse addUpdateTitle(TitleDto titleDto) {
        try {
            if (titleDto.getSuid() == null || titleDto.getSuid().isEmpty()) {
                return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.suid.cantbe.null.or.empty");
            }

            Subscriber subscriber = subscriberRepoIface.findBysubscriberUid(titleDto.getSuid());
            if (subscriber == null) {
                return exceptionHandlerUtil.createErrorResponse("api.error.subscriber.not.found");
            }

            if (titleDto.getTitle().equals("None")) {
                subscriber.setTitle("");
                subscriberRepoIface.save(subscriber);
            } else {
                subscriber.setTitle(titleDto.getTitle());
                subscriberRepoIface.save(subscriber);
            }
            return exceptionHandlerUtil.successResponse("api.response.title.updated");
        } catch (Exception e) {
            logger.error("Unexpected exception", e);
            return exceptionHandlerUtil.handleHttpException(e);
        }
    }
}
