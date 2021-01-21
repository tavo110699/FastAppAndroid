package lunainc.com.mx.fastdelivery.Model;

public class Slide {

    private String uid;
    private String name;
    private String status;
    private String photo;

    public Slide() {
    }

    public Slide(String uid, String name, String status, String photo) {
        this.uid = uid;
        this.name = name;
        this.status = status;
        this.photo = photo;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
