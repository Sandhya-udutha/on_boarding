package ug.daes.onboarding.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.constant.Constant;
import ug.daes.onboarding.exceptions.ExceptionHandlerUtil;
import ug.daes.onboarding.model.Subscriber;

import ug.daes.onboarding.model.SubscriberStatusModel;
import ug.daes.onboarding.model.TrustedUser;
import ug.daes.onboarding.repository.SubscriberRepoIface;
import ug.daes.onboarding.repository.SubscriberStatusRepoIface;
import ug.daes.onboarding.repository.TrustedUserRepoIface;
import ug.daes.onboarding.service.iface.TrustedUserIface;

@Service
public class TrustedUserIfaceImpl implements TrustedUserIface {

    private static final Logger logger = LoggerFactory.getLogger(TrustedUserIfaceImpl.class);


    private static final String CLASS = "TrustedUserIfaceImpl";
    private static final String EXCEPTION = "Unexpected exception";

    private final TrustedUserRepoIface trustedUserRepoIface;
    private final SubscriberRepoIface subscriberRepoIface;
    private final SubscriberStatusRepoIface subscriberStatusRepoIface;

    private final ExceptionHandlerUtil exceptionHandlerUtil;


    public TrustedUserIfaceImpl(
            TrustedUserRepoIface trustedUserRepoIface,
            SubscriberRepoIface subscriberRepoIface,
            SubscriberStatusRepoIface subscriberStatusRepoIface,

            ExceptionHandlerUtil exceptionHandlerUtil) {
        this.trustedUserRepoIface = trustedUserRepoIface;
        this.subscriberRepoIface = subscriberRepoIface;
        this.subscriberStatusRepoIface = subscriberStatusRepoIface;

        this.exceptionHandlerUtil = exceptionHandlerUtil;
    }

    @Override
    public ApiResponse getTrustedUserByEmail(String email) {
        logger.info(CLASS + " getTrustedUserDetails req email {}", email);
        try {
            if (email != null) {
                TrustedUser trustedUser = trustedUserRepoIface.getTrustedUserDratilsByEmail(email);
                if (Objects.nonNull(trustedUser)) {
                    logger.info(CLASS + " getTrustedUserDetails res Trusted User Details {}", trustedUser);
                    return exceptionHandlerUtil.createSuccessResponse("api.response.trusted.user.details", trustedUser);

                } else {
                    return exceptionHandlerUtil.createErrorResponse("api.error.trusted.user.details.not.found");
                }
            }
            return exceptionHandlerUtil.createErrorResponse("api.error.email.cant.be.null");


        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            logger.error(CLASS + "getTrustedUserDetails Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);

        }
    }

    @Override
    public ApiResponse updateTrustedUser(TrustedUser trustedUser) {
        try {
            logger.info(CLASS + " updateTrustedUser req {}", trustedUser);
            if (Objects.nonNull(trustedUser)) {
                Subscriber subscriber = subscriberRepoIface.findByemailId(trustedUser.getEmailId());
                if (Objects.nonNull(subscriber)) {
                    SubscriberStatusModel subscriberStatus = subscriberStatusRepoIface
                            .findBysubscriberUid(subscriber.getSubscriberUid());
                    logger.info("subscriberStatus :: {}", subscriberStatus.getSubscriberStatus());
                    logger.info(CLASS + " updateTrustedUser req subscriberStatus {}", subscriberStatus);
                    if (subscriberStatus.getSubscriberStatus().equals(Constant.ACTIVE)) {

                        return exceptionHandlerUtil.createErrorResponse("api.error.this.email.is.already.onboarded.and.active.you.cant.update");
                    } else {
                        TrustedUser user = new TrustedUser();
                        user.setTrustedUserId(trustedUser.getTrustedUserId());
                        user.setFullName(trustedUser.getFullName());
                        user.setEmailId(trustedUser.getEmailId());
                        user.setMobileNumber(trustedUser.getMobileNumber());
                        user.setTrustedUserStatus(trustedUser.getTrustedUserStatus());
                        trustedUserRepoIface.save(user);
                        logger.info(CLASS + " updateTrustedUser res Trusted user updated succesfully  {}", user);
                        return exceptionHandlerUtil.createSuccessResponse("api.response.trusted.user.updated.succesfully", user);

                    }
                } else {
                    return exceptionHandlerUtil.createErrorResponse("api.error.cant.found.any.relation.with.ugpass");

                }

            } else {
                return exceptionHandlerUtil.createErrorResponse("api.error.trusted.user.cant.be.null.or.empty");

            }

        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            logger.error(CLASS + " updateTrustedUser Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);

        }
    }

    @Override
    public ApiResponse deleteTrustedUser(String email) {
        try {
            logger.info(CLASS + " deleteTrustedUser req email {}", email);
            if (email != null) {
                TrustedUser user = trustedUserRepoIface.findByemailId(email);
                if (user != null) {
                    trustedUserRepoIface.deleteById(user.getTrustedUserId());
                    logger.info(CLASS + " deleteTrustedUser req rusted User Deleted Succssfully {}", email);
                    return exceptionHandlerUtil.successResponse("api.response.trusted.user.deleted.successfully");

                }
                return exceptionHandlerUtil.createErrorResponse("api.error.trusted.user.not.found");

            }

            return exceptionHandlerUtil.createErrorResponse("api.error.email.cant.should.be.null.or.empty");


        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            logger.info(CLASS + " deleteTrustedUser  Exception  {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);

        }
    }

    @Override
    public ApiResponse getAllTrustedUser() {
        try {
            List<TrustedUser> trustedUser = trustedUserRepoIface.findAll();
            if (trustedUser != null || !trustedUser.isEmpty()) {
                logger.info(CLASS + " getAllTrustedUser res Trusted Users Feached Successfully {}", trustedUser);
                return exceptionHandlerUtil.createSuccessResponse("api.response.trusted.Users.fetched.successfully", trustedUser);

            }
            return exceptionHandlerUtil.createErrorResponse("api.error.trusted.users.not.found");

        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            logger.info(CLASS + " getAllTrustedUser Exception {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

    @Override
    public ApiResponse addTrustedUser(List<TrustedUser> trustedUser) {
        try {
            if (Objects.nonNull(trustedUser) && !CollectionUtils.isEmpty(trustedUser)) {
                List<TrustedUser> userRes = new ArrayList<>();
                for (TrustedUser trustedUser2 : trustedUser) {
                    TrustedUser user = new TrustedUser();
                    user.setFullName(trustedUser2.getFullName());
                    user.setEmailId(trustedUser2.getEmailId());
                    user.setMobileNumber(trustedUser2.getMobileNumber());
                    user.setTrustedUserStatus(trustedUser2.getTrustedUserStatus());
                    userRes.add(user);
                }
                trustedUserRepoIface.saveAll(userRes);
                logger.info(CLASS + " addTrustedUser res Trusted User Added Succesfully {}", userRes);
                return exceptionHandlerUtil.createSuccessResponse("api.response.trusted.user.added.succesfully", userRes);
            }
            return exceptionHandlerUtil.createErrorResponse("api.error.trusted.user.cant.be.null.or.empty");
        } catch (Exception e) {
            logger.error(EXCEPTION, e);
            logger.info(CLASS + " addTrustedUser Exception  {}", e.getMessage());
            return exceptionHandlerUtil.handleException(e);
        }
    }

}
