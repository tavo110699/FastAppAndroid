package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FavoriteProduct extends ResponseDefault {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_producto")
    @Expose
    private String id_producto;

    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("favorites")
    @Expose
    private ArrayList<FavoriteProduct> favorites;

    @SerializedName("product")
    @Expose
    private Product product;

    public FavoriteProduct() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<FavoriteProduct> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<FavoriteProduct> favorites) {
        this.favorites = favorites;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
