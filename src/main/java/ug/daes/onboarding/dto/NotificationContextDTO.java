
package ug.daes.onboarding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;


public class NotificationContextDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("PREF_ONBOARDING_STATUS")
    private String prefOnboardingStatus;
    @JsonProperty("PREF_ONBOARDING_APPROVAL_STATUS")

    private String prefOnboardingApprovalStatus;
    @JsonProperty("PREF_CERTIFICATE_STATUS")

    private String prefCertificateStatus;
    @JsonProperty("PREF_CERTIFICATE_REVOKE_STATUS")

    private String prefCertificateRevokeStatus;
    @JsonProperty("PROMOTIONAL_NOTIFICATION")

    private String promotionalNotification;
    @JsonProperty("pREF_PAYMENT_STATUS")

    private Map<String, String> prefPaymentStatus;
    @JsonProperty("pREF_TRANSACTION_ID")

    private Map<String, String> prefTransactionId;

    public String getPrefOnboardingStatus() {
        return prefOnboardingStatus;
    }

    public void setPrefOnboardingStatus(String prefOnboardingStatus) {
        this.prefOnboardingStatus = prefOnboardingStatus;
    }

    public String getPrefOnboardingApprovalStatus() {
        return prefOnboardingApprovalStatus;
    }

    public void setPrefOnboardingApprovalStatus(String prefOnboardingApprovalStatus) {
        this.prefOnboardingApprovalStatus = prefOnboardingApprovalStatus;
    }

    public String getPrefCertificateStatus() {
        return prefCertificateStatus;
    }

    public void setPrefCertificateStatus(String prefCertificateStatus) {
        this.prefCertificateStatus = prefCertificateStatus;
    }

    public String getPrefCertificateRevokeStatus() {
        return prefCertificateRevokeStatus;
    }

    public void setPrefCertificateRevokeStatus(String prefCertificateRevokeStatus) {
        this.prefCertificateRevokeStatus = prefCertificateRevokeStatus;
    }

    public String getPromotionalNotification() {
        return promotionalNotification;
    }

    public void setPromotionalNotification(String promotionalNotification) {
        this.promotionalNotification = promotionalNotification;
    }

    public Map<String, String> getPrefPaymentStatus() {
        return prefPaymentStatus;
    }

    public void setPrefPaymentStatus(Map<String, String> prefPaymentStatus) {
        this.prefPaymentStatus = prefPaymentStatus;
    }

    public Map<String, String> getPrefTransactionId() {
        return prefTransactionId;
    }

    public void setPrefTransactionId(Map<String, String> prefTransactionId) {
        this.prefTransactionId = prefTransactionId;
    }

    @Override
    public String toString() {
        return "NotificationContextDTO{" +
                "prefOnboardingStatus='" + prefOnboardingStatus + '\'' +
                ", prefOnboardingApprovalStatus='" + prefOnboardingApprovalStatus + '\'' +
                ", prefCertificateStatus='" + prefCertificateStatus + '\'' +
                ", prefCertificateRevokeStatus='" + prefCertificateRevokeStatus + '\'' +
                ", promotionalNotification='" + promotionalNotification + '\'' +
                ", prefPaymentStatus=" + prefPaymentStatus +
                ", prefTransactionId=" + prefTransactionId +
                '}';
    }
}