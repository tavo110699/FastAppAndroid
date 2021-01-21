package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User  extends ResponseDefault implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_type_acc")
    @Expose
    private String id_type_acc;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("last_name_p")
    @Expose
    private String last_name_p;

    @SerializedName("last_name_m")
    @Expose
    private String last_name_m;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("phone_verified")
    @Expose
    private String phone_verified;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("thumb_image")
    @Expose
    private String thumb_image;

    @SerializedName("device_token")
    @Expose
    private String device_token;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("driver")
    @Expose
    private Driver driver;

    @SerializedName("created_at")
    @Expose
    private String created_at;


    public User() {
    }


    public User(String status, String message, String id, String id_type_acc, String name, String last_name_p, String last_name_m, String email, String phone, String phone_verified, String image, String thumb_image, String device_token, String password, Driver driver, String created_at) {
        super(status, message);
        this.id = id;
        this.id_type_acc = id_type_acc;
        this.name = name;
        this.last_name_p = last_name_p;
        this.last_name_m = last_name_m;
        this.email = email;
        this.phone = phone;
        this.phone_verified = phone_verified;
        this.image = image;
        this.thumb_image = thumb_image;
        this.device_token = device_token;
        this.password = password;
        this.driver = driver;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_type_acc() {
        return id_type_acc;
    }

    public void setId_type_acc(String id_type_acc) {
        this.id_type_acc = id_type_acc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name_p() {
        return last_name_p;
    }

    public void setLast_name_p(String last_name_p) {
        this.last_name_p = last_name_p;
    }

    public String getLast_name_m() {
        return last_name_m;
    }

    public void setLast_name_m(String last_name_m) {
        this.last_name_m = last_name_m;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getPhone_verified() {
        return phone_verified;
    }

    public void setPhone_verified(String phone_verified) {
        this.phone_verified = phone_verified;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
