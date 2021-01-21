package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderProducts implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_order")
    @Expose
    private String id_order;

    @SerializedName("id_product")
    @Expose
    private String id_product;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("extra")
    @Expose
    private String extra;

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("details")
    @Expose
    private String details;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("product")
    @Expose
    private Product product;


    public OrderProducts(String id, String id_order, String id_product, String price, String extra, String quantity, String details, String created_at, Product product) {
        this.id = id;
        this.id_order = id_order;
        this.id_product = id_product;
        this.price = price;
        this.extra = extra;
        this.quantity = quantity;
        this.details = details;
        this.created_at = created_at;
        this.product = product;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
