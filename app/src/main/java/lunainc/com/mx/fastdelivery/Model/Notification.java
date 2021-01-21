package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Notification {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("type_acc")
    @Expose
    private String type_acc;

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("status_res")
    @Expose
    private String status_res;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("notifications")
    @Expose
    private ArrayList<Notification> notifications;

    public Notification() {
    }


    public Notification(String id, String id_user, String type_acc, String title, String status_res, String message, String status, String created_at) {
        this.id = id;
        this.id_user = id_user;
        this.type_acc = type_acc;
        this.title = title;
        this.status_res = status_res;
        this.message = message;
        this.status = status;
        this.created_at = created_at;
    }

    public Notification(String id, String id_user, String type_acc, String title, String status_res, String message, String status, String created_at, ArrayList<Notification> notifications) {
        this.id = id;
        this.id_user = id_user;
        this.type_acc = type_acc;
        this.title = title;
        this.status_res = status_res;
        this.message = message;
        this.status = status;
        this.created_at = created_at;
        this.notifications = notifications;
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

    public String getType_acc() {
        return type_acc;
    }

    public void setType_acc(String type_acc) {
        this.type_acc = type_acc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus_res() {
        return status_res;
    }

    public void setStatus_res(String status_res) {
        this.status_res = status_res;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }
}
