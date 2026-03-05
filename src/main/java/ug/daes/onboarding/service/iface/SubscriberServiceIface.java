
package ug.daes.onboarding.service.iface;

import java.net.UnknownHostException;
import java.text.ParseException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.*;


public interface SubscriberServiceIface {


    ApiResponse saveSubscribersData(MobileOTPDto subscriberDTO) throws ParseException, UnknownHostException;

    ApiResponse saveSubscriberDocument(SubscriberDocumentDto subscriberDocumentDto);

    ApiResponse addSubscriberObData(SubscriberObRequestDTO obRequestDTO);

    ApiResponse reOnboardAddSubscriberObData(SubscriberObRequestDTO obRequestDTO);

    ApiResponse getSubscriberObData(HttpServletRequest request, GetSubscriberObDataDTO subscriberUID);

    ApiResponse getVerificationChannelResponse(HttpServletRequest request, String subscriberUID);

    ApiResponse resetPin(GetSubscriberObDataDTO subscriberObDataDTO);

    ApiResponse getSubscriberSelfie(String uri);

    ResponseEntity<Object> getVideoLiveStreaming(String subscriberUid);

    ResponseEntity<Object> getVideoLiveStreamingLocalEdms(String subscriberUid);

    ApiResponse addTrustedUsers(TrustedUserDto emails);

    ApiResponse getSubscriberDetailsReports(String startDate, String endDte);

    ApiResponse updatePhoneNumber(UpdateDto updateDto);

    ApiResponse updateEmail(UpdateDto updateDto);

    ApiResponse sendOtpEmail(UpdateOtpDto otpDto);

    ApiResponse sendOtpMobile(UpdateOtpDto otpDto);

    ApiResponse deleteRecord(String mobileNo, String email);

    ApiResponse getDeviceStatus(HttpServletRequest request);

    ApiResponse getSubscriberDetailsBySerachType(String searchType, String searchValue);

    ApiResponse updateSusbcriberDeviceStatus(String suid);

    ApiResponse getSubscriberListBySerachType(String searchType, String searchValue);

    ApiResponse updateFcmTokenDetails(String suid, String fcmToken);

    ApiResponse getFCMToken(String suid);

    ApiResponse getSubDetailsBySerachType(HttpServletRequest request, String searchType, String searchValue);


    ApiResponse getSusbcriberDeviceHistory(String suid);

    ApiResponse deleteRecordBySuid(String subscriberUid);

    ApiResponse getAllSubscribersDataFromView();

    ApiResponse getTotp(TotpDto totpDto);


}
