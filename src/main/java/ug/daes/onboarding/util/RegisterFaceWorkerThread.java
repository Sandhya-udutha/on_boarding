package ug.daes.onboarding.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.FaceFeaturesDto;


public class RegisterFaceWorkerThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(RegisterFaceWorkerThread.class);


    private final String registerFaceURL;
    private final FaceFeaturesDto faceFeaturesDto;

    public RegisterFaceWorkerThread(String registerFaceURL, FaceFeaturesDto faceFeaturesDto) {
        this.registerFaceURL = registerFaceURL;
        this.faceFeaturesDto = faceFeaturesDto;
    }

    @Override
    public void run() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> reqEntity = new HttpEntity<>(faceFeaturesDto, headers);
            String url = registerFaceURL;
            logger.info(" face url  http://10.4.72.20:8083/register_face  {}+", url);
            ResponseEntity<ApiResponse> res = restTemplate.exchange(registerFaceURL, HttpMethod.POST, reqEntity,
                    ApiResponse.class);
            if (res.getStatusCodeValue() == 200 || res.getStatusCodeValue() == 201) {
                logger.info(" face features result : status code 200 ");
            }
        } catch (Exception e) {
            logger.info(" failed register_face {} ", e.getMessage());
            logger.error("Unexpected exception", e);
        }


    }

}
