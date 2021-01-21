package lunainc.com.mx.fastdelivery.Model;

public class Cesta {


    private int id;

    private String id_product;

    private String product_name;

    private String product_image;

    private String static_price_product;

    private String id_partner;

    private String name_partner;

    private String type_product;

    private String descripcion;

    private String cantidad;

    private String extra;

    public Cesta() {
    }

    public Cesta(int id, String id_product, String product_name, String product_image, String static_price_product, String id_partner, String name_partner ,String type_product, String descripcion, String cantidad, String extra) {
        this.id = id;
        this.id_product = id_product;
        this.product_name = product_name;
        this.product_image = product_image;
        this.static_price_product = static_price_product;
        this.id_partner = id_partner;
        this.name_partner = name_partner;
        this.type_product = type_product;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.extra = extra;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getStatic_price_product() {
        return static_price_product;
    }

    public void setStatic_price_product(String static_price_product) {
        this.static_price_product = static_price_product;
    }

    public String getName_partner() {
        return name_partner;
    }

    public void setName_partner(String name_partner) {
        this.name_partner = name_partner;
    }

    public String getId_partner() {
        return id_partner;
    }

    public void setId_partner(String id_partner) {
        this.id_partner = id_partner;
    }

    public String getType_product() {
        return type_product;
    }

    public void setType_product(String type_product) {
        this.type_product = type_product;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
