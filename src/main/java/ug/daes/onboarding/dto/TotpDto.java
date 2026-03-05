package ug.daes.onboarding.dto;


public class TotpDto {

    private String suid;
    private String priAuthScheme;
    private String fullName;

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getPriAuthScheme() {
        return priAuthScheme;
    }

    public void setPriAuthScheme(String priAuthScheme) {
        this.priAuthScheme = priAuthScheme;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "TotpDto [suid=" + suid + ", priAuthScheme=" + priAuthScheme + ", fullName=" + fullName + "]";
    }

}
