package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderDriver extends ResponseDefault implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_driver")
    @Expose
    private String id_driver;

    @SerializedName("id_client")
    @Expose
    private String id_client;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("latitude_start")
    @Expose
    private String latitude_start;

    @SerializedName("longitude_start")
    @Expose
    private String longitude_start;

    @SerializedName("latitude_finish")
    @Expose
    private String latitude_finish;

    @SerializedName("longitude_finish")
    @Expose
    private String longitude_finish;

    @SerializedName("cal_service")
    @Expose
    private String cal_service;

    public OrderDriver() {
    }

    public OrderDriver(String status, String message, String id, String id_driver, String id_client, String price, String latitude_start, String longitude_start, String latitude_finish, String longitude_finish, String cal_service) {
        super(status, message);
        this.id = id;
        this.id_driver = id_driver;
        this.id_client = id_client;
        this.price = price;
        this.latitude_start = latitude_start;
        this.longitude_start = longitude_start;
        this.latitude_finish = latitude_finish;
        this.longitude_finish = longitude_finish;
        this.cal_service = cal_service;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_driver() {
        return id_driver;
    }

    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLatitude_start() {
        return latitude_start;
    }

    public void setLatitude_start(String latitude_start) {
        this.latitude_start = latitude_start;
    }

    public String getLongitude_start() {
        return longitude_start;
    }

    public void setLongitude_start(String longitude_start) {
        this.longitude_start = longitude_start;
    }

    public String getLatitude_finish() {
        return latitude_finish;
    }

    public void setLatitude_finish(String latitude_finish) {
        this.latitude_finish = latitude_finish;
    }

    public String getLongitude_finish() {
        return longitude_finish;
    }

    public void setLongitude_finish(String longitude_finish) {
        this.longitude_finish = longitude_finish;
    }

    public String getCal_service() {
        return cal_service;
    }

    public void setCal_service(String cal_service) {
        this.cal_service = cal_service;
    }
}
