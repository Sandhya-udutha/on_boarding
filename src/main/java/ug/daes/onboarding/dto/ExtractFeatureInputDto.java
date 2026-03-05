package ug.daes.onboarding.dto;

public class ExtractFeatureInputDto {

    private String subscriberPhoto;

    private String image;

    public String getSubscriberPhoto() {
        return subscriberPhoto;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSubscriberPhoto(String subscriberPhoto) {
        this.subscriberPhoto = subscriberPhoto;
    }

    @Override
    public String toString() {
        return "extractFeatureInputDto{" +
                "subscriberPhoto='" + subscriberPhoto + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
