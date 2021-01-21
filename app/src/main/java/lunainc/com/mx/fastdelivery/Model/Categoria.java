package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Categoria extends ResponseDefault {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("categorias")
    @Expose
    private ArrayList<Categoria> categorias;

    public Categoria() {
    }

    public Categoria(String status, String message, String id, String description, String image) {
        super(status, message);
        this.id = id;
        this.description = description;
        this.image = image;
    }

    public Categoria(String status, String message, String id, String description, String image, ArrayList<Categoria> categorias) {
        super(status, message);
        this.id = id;
        this.description = description;
        this.image = image;
        this.categorias = categorias;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }
}
