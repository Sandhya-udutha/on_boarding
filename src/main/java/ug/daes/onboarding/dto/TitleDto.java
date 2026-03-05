package ug.daes.onboarding.dto;



public class TitleDto {


    private String suid;

    private String title;

    public String getSuid() {
        return suid;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TitleDto{" +
                "suid='" + suid + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
