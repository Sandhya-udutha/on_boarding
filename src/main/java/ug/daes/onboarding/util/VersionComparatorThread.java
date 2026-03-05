package ug.daes.onboarding.util;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ug.daes.onboarding.model.Subscriber;
import ug.daes.onboarding.repository.SubscriberRepoIface;

public class VersionComparatorThread implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(VersionComparatorThread.class);

    private final Subscriber subscriber;
    private final SubscriberRepoIface subscriberRepoIface;
    private final HttpServletRequest httpServletRequest;


    public VersionComparatorThread(Subscriber subscriber,
                                   SubscriberRepoIface subscriberRepoIface,
                                   HttpServletRequest httpServletRequest) {
        this.subscriber = subscriber;
        this.subscriberRepoIface = subscriberRepoIface;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public void run() {
        try {


            if (isNewerVersion(httpServletRequest.getHeader("appVersion"), subscriber.getAppVersion())) {
                logger.info("New version is greater");
                subscriber.setAppVersion(httpServletRequest.getHeader("appVersion"));
                subscriber.setOsVersion(httpServletRequest.getHeader("osVersion"));
                subscriber.setUpdatedDate(AppUtil.getDate());
                subscriberRepoIface.save(subscriber);
            } else {
                logger.info("Current version is up-to-date or newer");
            }


        } catch (Exception e) {
            logger.info("Error while checking Current version is up-to-date or newer");
        }
    }

    public static boolean isNewerVersion(String newVersion, String currentVersion) {
        String[] newParts = newVersion.split("\\.");
        String[] currentParts = currentVersion.split("\\.");

        int length = Math.max(newParts.length, currentParts.length);

        for (int i = 0; i < length; i++) {
            int newPart = i < newParts.length ? Integer.parseInt(newParts[i]) : 0;
            int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;

            if (newPart > currentPart) {
                return true;
            } else if (newPart < currentPart) {
                return false;
            }
        }
        return false;
    }

}