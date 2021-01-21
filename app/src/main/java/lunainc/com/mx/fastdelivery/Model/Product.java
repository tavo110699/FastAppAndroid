package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Product extends ResponseDefault implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type_product")
    @Expose
    private String type_product;

    @SerializedName("id_partner")
    @Expose
    private String id_partner;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("number_ingredients")
    @Expose
    private String number_ingredients;

    @SerializedName("stock")
    @Expose
    private String stock;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("products")
    @Expose
    private ArrayList<Product> products;

    @SerializedName("category")
    @Expose
    private Categoria categoria;

    @SerializedName("partner")
    @Expose
    private Partner partner;

    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> ingredients;




    public Product() {
    }

    public Product(String status, String message, String id, String type_product, String id_partner, String name, String description, String price, String number_ingredients, String stock, String image, String created_at) {
        super(status, message);
        this.id = id;
        this.type_product = type_product;
        this.id_partner = id_partner;
        this.name = name;
        this.description = description;
        this.price = price;
        this.number_ingredients = number_ingredients;
        this.stock = stock;
        this.image = image;
        this.created_at = created_at;
    }

    public Product(String status, String message, String id, String type_product, String id_partner, String name, String description, String price, String number_ingredients, String stock, String image, String created_at, ArrayList<Product> products) {
        super(status, message);
        this.id = id;
        this.type_product = type_product;
        this.id_partner = id_partner;
        this.name = name;
        this.description = description;
        this.price = price;
        this.number_ingredients = number_ingredients;
        this.stock = stock;
        this.image = image;
        this.created_at = created_at;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_product() {
        return type_product;
    }

    public void setType_product(String type_product) {
        this.type_product = type_product;
    }

    public String getId_partner() {
        return id_partner;
    }

    public void setId_partner(String id_partner) {
        this.id_partner = id_partner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber_ingredients() {
        return number_ingredients;
    }

    public void setNumber_ingredients(String number_ingredients) {
        this.number_ingredients = number_ingredients;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }


    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }


    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


}
