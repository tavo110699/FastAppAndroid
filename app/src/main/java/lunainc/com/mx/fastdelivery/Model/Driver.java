package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Driver {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("credential")
    @Expose
    private String credential;

    @SerializedName("delivery_license")
    @Expose
    private String delivery_license;

    @SerializedName("modelCar")
    @Expose
    private String modelCar;

    @SerializedName("no_unidad")
    @Expose
    private String no_unidad;

    @SerializedName("placas")
    @Expose
    private String placas;

    @SerializedName("credits")
    @Expose
    private String credits;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("resp_status")
    @Expose
    private String resp_status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("user")
    @Expose
    private User user;



    public Driver() {
    }

    public Driver(String id, String id_user, String credential, String delivery_license, String modelCar, String no_unidad, String placas, String credits, String latitude, String longitude, String status, String created_at, String resp_status, String message, User user) {
        this.id = id;
        this.id_user = id_user;
        this.credential = credential;
        this.delivery_license = delivery_license;
        this.modelCar = modelCar;
        this.no_unidad = no_unidad;
        this.placas = placas;
        this.credits = credits;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.created_at = created_at;
        this.resp_status = resp_status;
        this.message = message;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getDelivery_license() {
        return delivery_license;
    }

    public void setDelivery_license(String delivery_license) {
        this.delivery_license = delivery_license;
    }

    public String getModelCar() {
        return modelCar;
    }

    public void setModelCar(String modelCar) {
        this.modelCar = modelCar;
    }

    public String getNo_unidad() {
        return no_unidad;
    }

    public void setNo_unidad(String no_unidad) {
        this.no_unidad = no_unidad;
    }

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getResp_status() {
        return resp_status;
    }

    public void setResp_status(String resp_status) {
        this.resp_status = resp_status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
