package lunainc.com.mx.fastdelivery.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Ingredient extends ResponseDefault implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("partner_id")
    @Expose
    private String partner_id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> ingredients;

    @SerializedName("ingredient")
    @Expose
    private Ingredient ingredient;


    public Ingredient() {
    }

    public Ingredient(String status, String message, String id, String partner_id, String description, String price, String created_at) {
        super(status, message);
        this.id = id;
        this.partner_id = partner_id;
        this.description = description;
        this.price = price;
        this.created_at = created_at;
    }


    public Ingredient(String status, String message, String id, String partner_id, String description, String price, String created_at, ArrayList<Ingredient> ingredients) {
        super(status, message);
        this.id = id;
        this.partner_id = partner_id;
        this.description = description;
        this.price = price;
        this.created_at = created_at;
        this.ingredients = ingredients;
    }

    public Ingredient(String status, String message, String id, String partner_id, String description, String price, String created_at, ArrayList<Ingredient> ingredients, Ingredient ingredient) {
        super(status, message);
        this.id = id;
        this.partner_id = partner_id;
        this.description = description;
        this.price = price;
        this.created_at = created_at;
        this.ingredients = ingredients;
        this.ingredient = ingredient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }


}
