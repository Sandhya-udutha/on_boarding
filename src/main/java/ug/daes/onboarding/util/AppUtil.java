package ug.daes.onboarding.util;


import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import ug.daes.DAESService;
import ug.daes.Result;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.exceptions.EncryptionException;
import ug.daes.onboarding.exceptions.HmacGenerationException;
import ug.daes.onboarding.exceptions.InvalidDateOfBirthException;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;


import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class AppUtil {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private static final DateTimeFormatter ISO_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final String UNEXPECTED_EXCEPTION = "Unexpected exception";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;


    private final String secretKey;

    public AppUtil(@Value("${hmac.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    @PostConstruct
    private void checkSecretKey() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("AES/HMAC secret key is not set!");
        }
    }

    private static SecretKeySpec secretKeySpec;
    private static Logger logger = LoggerFactory.getLogger(AppUtil.class);


    public static String getUUId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    //to getcurrent date
    public static Date getCurrentDate() {
        return new Date();
    }

    public static ApiResponse createApiResponse(boolean success, String msg, Object object) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(msg);
        apiResponse.setResult(object);
        apiResponse.setSuccess(success);
        return apiResponse;

    }

    public static String getBase64FromByteArr(byte[] bytes) {
        Base64.Encoder base64 = Base64.getEncoder();
        return base64.encodeToString(bytes);
    }

    public static byte[] getSalt(String salt) {
        return salt.getBytes();
    }

    public String encrypt(String plainText) {
        try {

            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);


            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), getSalt(plainText), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);


            SecretKeySpec aesKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");


            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, gcmSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));


            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
            byteBuffer.put(iv);
            byteBuffer.put(encrypted);

            return Base64.getEncoder().encodeToString(byteBuffer.array());

        } catch (Exception e) {
            throw new EncryptionException("Error while encrypting data", e);
        }
    }


    public static String getDate() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }


    public static String getTimeStamping() {
        return LocalDateTime.now().format(ISO_FORMATTER);
    }


    public static double getDifferenceInSeconds(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff / 1000.0;
    }


    public static String getTimeStampString(Date date) {

        LocalDateTime dateTime = (date != null)
                ? date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                : LocalDateTime.now();

        return dateTime.format(ISO_DATE_TIME_FORMATTER);
    }

    public static long getDifferenceBetDates(String createdDate) throws ParseException {
        Date d2 = AppUtil.getCurrentDate();
        Date d1 = new SimpleDateFormat(DATE_TIME_PATTERN, Locale.ENGLISH).parse(createdDate);
        long differenceInTime = d2.getTime() - d1.getTime();
        long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInTime);
        logger.info("difference_In_Days: {}", differenceInDays);
        return differenceInDays;
    }

    public static String removeTimeStamp(String date) {

        LocalDateTime dateTime = LocalDateTime.parse(date, DATE_TIME_FORMATTER);

        return dateTime.toLocalDate().format(DATE_FORMATTER);
    }

    public static LocalDateTime getLocalDateTime(String date) {
        return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    }


    public static String encryptedString(String s) {
        try {

            Result result = DAESService.encryptData(s);
            return new String(result.getResponse());
        } catch (Exception e) {
            logger.error(UNEXPECTED_EXCEPTION, e);
            return e.getMessage();
        }
    }

    public static LocalDate parseToLocalDate(String dobStr) {
        logger.info("parseToLocalDate :: {}", dobStr);

        if (dobStr == null || dobStr.isBlank()) {
            throw new InvalidDateOfBirthException("dateOfBirth is required");
        }

        try {
            LocalDateTime dobDateTime = LocalDateTime.parse(dobStr, DATE_FORMATTER);
            return dobDateTime.toLocalDate();
        } catch (Exception e) {
            throw new InvalidDateOfBirthException("Invalid dateOfBirth format: " + dobStr, e);
        }

    }

    public static int calculateAge(LocalDate dob) {

        if (dob == null) {
            throw new InvalidDateOfBirthException("DOB cannot be null");
        }

        if (dob.isAfter(LocalDate.now())) {
            throw new InvalidDateOfBirthException("Invalid DOB: DOB cannot be future date");
        }

        return Period.between(dob, LocalDate.now()).getYears();
    }


    public static String hmacSha256Base64(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new HmacGenerationException("Error while generating HMAC SHA256", e);
        }
    }


}
