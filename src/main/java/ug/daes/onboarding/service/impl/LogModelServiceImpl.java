package ug.daes.onboarding.service.impl;

import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ug.daes.DAESService;
import ug.daes.Result;
import ug.daes.onboarding.dto.LogModelDTO;
import ug.daes.onboarding.dto.NiraApiLogDto;
import ug.daes.onboarding.enums.LogMessageType;
import ug.daes.onboarding.enums.TransactionType;
import ug.daes.onboarding.service.iface.LogModelServiceIface;
import ug.daes.onboarding.util.AppUtil;

@Service
public class LogModelServiceImpl implements LogModelServiceIface {

	private static final Logger logger = LoggerFactory.getLogger(LogModelServiceImpl.class);


	private static final String CLASS = "LogModelServiceImpl";
	private static final String EXCEPTION="Unexpected exception";

	private final KafkaSender mqSender;

	public LogModelServiceImpl(KafkaSender mqSender) {
		this.mqSender = mqSender;
	}


	@Override
	public void setLogModel(Boolean response, String identifier, String geoLocation, String serviceName,
			String correlationID, String totalTime, Date startTime, Date endTime, String otpStatus)
			throws ParseException {
		LogModelDTO logModel = new LogModelDTO();
		logModel.setIdentifier(identifier);
		logModel.setCorrelationID(correlationID);
		logModel.setTransactionID(correlationID);
		logModel.setTimestamp(null);
		logModel.setStartTime(AppUtil.getTimeStampString(startTime));
		logModel.setEndTime(AppUtil.getTimeStampString(endTime));
		logModel.setServiceName(serviceName);
		logModel.setLogMessage("Total Time Taken " + totalTime + " sec");
		logModel.setTransactionType(TransactionType.BUSINESS.toString());
		logModel.setGeoLocation(geoLocation);
		logModel.seteSealUsed(false);
		logModel.setSignatureType(null);
		logModel.setCallStack(otpStatus);
		if (Boolean.TRUE.equals(response)) {
			logModel.setLogMessageType(LogMessageType.SUCCESS.toString());
		} else {
			logModel.setLogMessageType(LogMessageType.FAILURE.toString());
		}
		logModel.setChecksum(null);

		try {

			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(logModel);
			Result checksumResult = DAESService.addChecksumToTransaction(json);
			String push = new String(checksumResult.getResponse());
			LogModelDTO log = objectMapper.readValue(push, LogModelDTO.class);
			mqSender.send(log);
			logger.info(CLASS + " setLogModel log {}",log );
		} catch (Exception e) {
			logger.error(EXCEPTION, e);
			logger.error(CLASS + " setLogModel Exception  {]", e.getMessage() );
		}
	}
	
	@Override
	public void setLogModelDTO(Boolean response, String identifier, String geoLocation, String serviceName,
			String correlationID, String message, Date startTime, Date endTime, String otpStatus)
			throws ParseException {
		LogModelDTO logModel = new LogModelDTO();
		logModel.setIdentifier(identifier);
		logModel.setCorrelationID(null);
		logModel.setTransactionID(null);
		logModel.setTimestamp(null);
		logModel.setStartTime(AppUtil.getTimeStampString(startTime));
		logModel.setEndTime(AppUtil.getTimeStampString(endTime));
		logModel.setServiceName(serviceName);
		logModel.setLogMessage(message);
		logModel.setTransactionType(TransactionType.BUSINESS.toString());
		logModel.setGeoLocation(geoLocation);
		logModel.seteSealUsed(false);
		logModel.setSignatureType(null);
		logModel.setCallStack(otpStatus);
		if (Boolean.TRUE.equals(response)){
			logModel.setLogMessageType(LogMessageType.SUCCESS.toString());
		} else {
			logModel.setLogMessageType(LogMessageType.FAILURE.toString());
		}
		logModel.setChecksum(null);

		try {

			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(logModel);
			Result checksumResult = DAESService.addChecksumToTransaction(json);
			String push = new String(checksumResult.getResponse());
			LogModelDTO log = objectMapper.readValue(push, LogModelDTO.class);
			mqSender.send(log);
			logger.info(CLASS + " setLogModel log {}",log );
		} catch (Exception e) {
			logger.error(EXCEPTION, e);
			logger.error(CLASS + " setLogModel Exception  {]", e.getMessage() );
		}
	}

	@Override
	public void setLogModelNiraApi(NiraApiLogDto niraApiLogDto) throws ParseException {
		try {
			LogModelDTO logModel = new LogModelDTO();
			logModel.setIdentifier(niraApiLogDto.getSubscriberUniqueId());
			logModel.setCorrelationID(AppUtil.getUUId());
			logModel.setTransactionID(AppUtil.getUUId());
			logModel.setTimestamp(null);
			logModel.setStartTime(AppUtil.getTimeStamping());
			logModel.setEndTime(AppUtil.getTimeStamping());
			logModel.setServiceName(niraApiLogDto.getServiceName());
			logModel.setLogMessage(niraApiLogDto.getLogMessage());
			logModel.setLogMessageType(LogMessageType.FAILURE.toString());
			logModel.setTransactionType(TransactionType.BUSINESS.toString());
			logModel.setGeoLocation(null);
			logModel.seteSealUsed(false);
			logModel.setSignatureType(null);
			logModel.setCallStack(null);
			logModel.setChecksum(null);
			logModel.setServiceProviderAppName(null);
			logModel.setServiceProviderName(null);
			logModel.setTransactionSubType(null);
			logModel.setSubTransactionID(null);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(logModel);
			Result checksumResult = DAESService.addChecksumToTransaction(json);
			String push = new String(checksumResult.getResponse());
			LogModelDTO log = objectMapper.readValue(push, LogModelDTO.class);
			mqSender.send(log);
			logger.info(CLASS + "setLogModelNiraApi log {}", log );
		} catch (Exception e) {
			logger.error(EXCEPTION, e);
			logger.error(CLASS + " setLogModelNiraApi Exception {}",e.getMessage() );
		}

	}
	
	@Override
	public void setLogModelFCMToken(Boolean response, String identifier, String geoLocation, String serviceName,
			String correlationID, String message, Date startTime, Date endTime, String otpStatus)
			throws ParseException {
		LogModelDTO logModel = new LogModelDTO();
		logModel.setIdentifier(identifier);
		logModel.setCorrelationID(correlationID);
		logModel.setTransactionID(correlationID);
		logModel.setTimestamp(null);
		logModel.setStartTime(AppUtil.getTimeStampString(startTime));
		logModel.setEndTime(AppUtil.getTimeStampString(endTime));
		logModel.setServiceName(serviceName);
		logModel.setLogMessage(message);
		logModel.setTransactionType(TransactionType.BUSINESS.toString());
		logModel.setGeoLocation(geoLocation);
		logModel.seteSealUsed(false);
		logModel.setSignatureType(null);
		logModel.setCallStack(otpStatus);
		if (Boolean.TRUE.equals(response)){
			logModel.setLogMessageType(LogMessageType.SUCCESS.toString());
		} else {
			logModel.setLogMessageType(LogMessageType.FAILURE.toString());
		}
		logModel.setChecksum(null);

		try {

			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(logModel);
			Result checksumResult = DAESService.addChecksumToTransaction(json);
			String push = new String(checksumResult.getResponse());
			LogModelDTO log = objectMapper.readValue(push, LogModelDTO.class);
			mqSender.send(log);
		} catch (Exception e) {
			logger.error(EXCEPTION, e);
		}
	}

}
