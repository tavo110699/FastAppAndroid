package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_partner")
    @Expose
    private String id_partner;


    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("status_res")
    @Expose
    private String status_res;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("created_at")
    @Expose
    private String created_at;


    @SerializedName("orders")
    @Expose
    private ArrayList<Order> orders;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("partner")
    @Expose
    private Partner partner;

    @SerializedName("direction")
    @Expose
    private Direction direction;

    @SerializedName("order_products")
    @Expose
    private ArrayList<OrderProducts> orderProducts;
    

    public Order() {
    }

    public Order(String id, String id_partner, String id_user, String total, String status, String status_res, String message, String created_at, ArrayList<Order> orders, User user, Partner partner, Direction direction, ArrayList<OrderProducts> orderProducts) {
        this.id = id;
        this.id_partner = id_partner;
        this.id_user = id_user;
        this.total = total;
        this.status = status;
        this.status_res = status_res;
        this.message = message;
        this.created_at = created_at;
        this.orders = orders;
        this.user = user;
        this.partner = partner;
        this.direction = direction;
        this.orderProducts = orderProducts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_partner() {
        return id_partner;
    }

    public void setId_partner(String id_partner) {
        this.id_partner = id_partner;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public ArrayList<OrderProducts> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(ArrayList<OrderProducts> orderProducts) {
        this.orderProducts = orderProducts;
    }
}





