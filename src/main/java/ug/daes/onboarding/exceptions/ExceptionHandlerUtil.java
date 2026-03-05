package ug.daes.onboarding.exceptions;

import java.util.Locale;

import org.hibernate.PessimisticLockException;
import org.hibernate.QueryTimeoutException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.util.AppUtil;
import ug.daes.onboarding.util.Utility;

@Component
public class ExceptionHandlerUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerUtil.class);
    private static final String CLASS = ExceptionHandlerUtil.class.getSimpleName();

    private final MessageSource messageSource;

    public ExceptionHandlerUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ApiResponse handleException(Exception e) {

        Locale locale = LocaleContextHolder.getLocale();

        String errorKey = "api.error.generic";
        String errorCode = ErrorCodeException.GENERIC_ERROR.getCode();

        if (e instanceof JDBCConnectionException) {
            errorKey = "api.error.connection";
            errorCode = ErrorCodeException.CONNECTION_ERROR.getCode();

            logger.error("{} - {} : Database connection error occurred: {}",
                    CLASS, Utility.getMethodName(), e.getMessage());

        } else if (e instanceof ConstraintViolationException
                || e instanceof DataException
                || e instanceof LockAcquisitionException
                || e instanceof PessimisticLockException
                || e instanceof QueryTimeoutException
                || e instanceof SQLGrammarException
                || e instanceof GenericJDBCException) {

            errorKey = "api.error.database";
            errorCode = ErrorCodeException.DATABASE_ERROR.getCode();

            logger.error("{} - {} : Database-related error occurred: {}",
                    CLASS, Utility.getMethodName(), e.getMessage());

        } else {
            logger.error("{} - {} : An unexpected error occurred: {}",
                    CLASS, Utility.getMethodName(), e.getMessage());
        }

        String errorMessage = messageSource.getMessage(errorKey, null, locale);
        String formattedMessage = String.format("%s [ErrorCode: %s]", errorMessage, errorCode);

        logger.info("{} - {} : Returning error response: errorCode={}, message={}",
                CLASS, Utility.getMethodName(), errorCode, formattedMessage);

        return createErrorResponse(formattedMessage);
    }


    public ApiResponse handleHttpException(Exception e) {

        String errorCode;
        String errorMessage;
        Locale locale = LocaleContextHolder.getLocale();

        if (e instanceof HttpStatusCodeException httpEx) {

            HttpStatus status =
                    HttpStatus.valueOf(httpEx.getStatusCode().value());


            ErrorCodeException errorEnum = switch (status) {
                case BAD_REQUEST -> ErrorCodeException.BAD_REQUEST;
                case UNAUTHORIZED, FORBIDDEN, NOT_FOUND -> ErrorCodeException.REST_CLIENT_ERROR;
                case INTERNAL_SERVER_ERROR -> ErrorCodeException.INTERNAL_SERVER_ERROR;
                case SERVICE_UNAVAILABLE, GATEWAY_TIMEOUT -> ErrorCodeException.SERVICE_UNAVAILABLE;
                default -> ErrorCodeException.UNKNOWN_ERROR;
            };


            errorCode = errorEnum.getCode();
            errorMessage = messageSource.getMessage(
                    errorEnum.getMessageKey(),
                    null,
                    locale
            );

            String formattedMessage = String.format(
                    "HTTP Error: %s - %s (%s)",
                    status.value(),
                    errorMessage,
                    errorCode
            );

            logger.error("{} - {} : HTTP exception occurred: status={}, errorCode={}, message={}",
                    CLASS, Utility.getMethodName(), status.value(), errorCode, e.getMessage());

            return AppUtil.createApiResponse(false, formattedMessage, null);

        } else if (e instanceof ResourceAccessException) {

            ErrorCodeException errorEnum = ErrorCodeException.REST_CONNECTION_ERROR;

            errorCode = errorEnum.getCode();
            errorMessage = messageSource.getMessage(
                    errorEnum.getMessageKey(),
                    null,
                    locale
            );

            String formattedMessage = String.format(
                    "Network Error: %s (%s)",
                    errorMessage,
                    errorCode
            );

            logger.error("{} - {} : Network exception occurred: message={}",
                    CLASS, Utility.getMethodName(), e.getMessage());

            return AppUtil.createApiResponse(false, formattedMessage, null);

        } else {

            ErrorCodeException errorEnum = ErrorCodeException.UNKNOWN_ERROR;

            errorCode = errorEnum.getCode();
            errorMessage = messageSource.getMessage(
                    errorEnum.getMessageKey(),
                    null,
                    locale
            );

            String formattedMessage = String.format(
                    "Unexpected Error: %s (%s)",
                    errorMessage,
                    errorCode
            );

            logger.error("{} - {} : Unexpected exception occurred: message={}",
                    CLASS, Utility.getMethodName(), e.getMessage());

            return AppUtil.createApiResponse(false, formattedMessage, null);
        }
    }


    public ApiResponse createSuccessResponse(String successMessage, Object result) {
        ApiResponse response = new ApiResponse();

        Locale locale = LocaleContextHolder.getLocale();
        String successMsg = messageSource.getMessage(successMessage, null, locale);

        response.setSuccess(true);
        response.setMessage(successMsg);
        response.setResult(result);

        return response;
    }


    public ApiResponse createSuccessResponseWithCustomMessage(String successMessage, Object result) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setMessage(successMessage);
        response.setResult(result);
        return response;
    }


    public ApiResponse createFailedResponseWithCustomMessage(String messageKey, Object result) {

        ApiResponse response = new ApiResponse();

        Locale locale = LocaleContextHolder.getLocale();

        String message;


        message = messageSource.getMessage(
                messageKey,
                new Object[]{result},
                locale
        );




        response.setSuccess(false);
        response.setMessage(message);
        response.setResult(null);


        return response;
    }
    public ApiResponse successResponse(String successMessage) {
        ApiResponse response = new ApiResponse();
        Locale locale = LocaleContextHolder.getLocale();
        String successMsg = messageSource.getMessage(successMessage, null, locale);
        response.setSuccess(true);
        response.setMessage(successMsg);
        response.setResult(null);
        return response;
    }

    public ApiResponse createErrorResponseWithResult(String successMessage, Object result) {
        ApiResponse response = new ApiResponse();
        Locale locale = LocaleContextHolder.getLocale();
        String successMeg = messageSource.getMessage(successMessage, null, locale);
        response.setSuccess(false);
        response.setMessage(successMeg);
        response.setResult(result);
        return response;
    }


    public ApiResponse createErrorResponse(String messageKey) {

        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = messageSource.getMessage(messageKey, null, locale);
        logger.error("Error response created with message: {}", errorMessage);
        return AppUtil.createApiResponse(false, errorMessage, null);
    }

    public ApiResponse handleErrorRestTemplateResponse(int statusCode) {
        switch (statusCode) {
            case 500:
                return AppUtil.createApiResponse(false,
                        messageSource.getMessage("api.error.internal.server.error", null, Locale.ENGLISH), statusCode);
            case 400:
                return AppUtil.createApiResponse(false,
                        messageSource.getMessage("api.error.bad.request", null, Locale.ENGLISH), null);
            case 401:
                return AppUtil.createApiResponse(false,
                        messageSource.getMessage("api.error.unauthorized", null, Locale.ENGLISH), null);
            case 403:
                return AppUtil.createApiResponse(false,
                        messageSource.getMessage("api.error.forbidden", null, Locale.ENGLISH), null);
            case 408:
                return AppUtil.createApiResponse(false,
                        messageSource.getMessage("api.error.request.timeout", null, Locale.ENGLISH), null);
            default:
                return AppUtil.createApiResponse(false, messageSource.getMessage(
                        "api.error.something.went.wrong.please.try.after.sometime", null, Locale.ENGLISH), null);
        }
    }

}
