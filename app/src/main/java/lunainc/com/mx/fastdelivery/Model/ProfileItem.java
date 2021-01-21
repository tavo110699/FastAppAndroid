package lunainc.com.mx.fastdelivery.Model;

public class ProfileItem {

    private String title;

    private Integer image;


    public ProfileItem() {
    }

    public ProfileItem(String title, Integer image) {
        this.title = title;
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
