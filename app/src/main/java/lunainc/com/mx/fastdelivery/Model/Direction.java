package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Direction extends ResponseDefault implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type_acc")
    @Expose
    private String type_acc;

    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("interior_number")
    @Expose
    private String interior_number;

    @SerializedName("exterior_number")
    @Expose
    private String exterior_number;

    @SerializedName("street")
    @Expose
    private String street;

    @SerializedName("colony")
    @Expose
    private String colony;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("postal_code")
    @Expose
    private String postal_code;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("direction_complete")
    @Expose
    private String direction_complete;

    @SerializedName("directions")
    @Expose
    private ArrayList<Direction> directions;

    public Direction() {
    }

    public Direction(String status, String message, String id, String type_acc, String id_user, String interior_number, String exterior_number, String street, String colony, String city, String state, String country, String postal_code, String latitude, String longitude, String created_at, String direction_complete) {
        super(status, message);
        this.id = id;
        this.type_acc = type_acc;
        this.id_user = id_user;
        this.interior_number = interior_number;
        this.exterior_number = exterior_number;
        this.street = street;
        this.colony = colony;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postal_code = postal_code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_at = created_at;
        this.direction_complete = direction_complete;
    }

    public Direction(String status, String message, String id, String type_acc, String id_user, String interior_number, String exterior_number, String street, String colony, String city, String state, String country, String postal_code, String latitude, String longitude, String created_at, String direction_complete, ArrayList<Direction> directions) {
        super(status, message);
        this.id = id;
        this.type_acc = type_acc;
        this.id_user = id_user;
        this.interior_number = interior_number;
        this.exterior_number = exterior_number;
        this.street = street;
        this.colony = colony;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postal_code = postal_code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_at = created_at;
        this.direction_complete = direction_complete;
        this.directions = directions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_acc() {
        return type_acc;
    }

    public void setType_acc(String type_acc) {
        this.type_acc = type_acc;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getInterior_number() {
        return interior_number;
    }

    public void setInterior_number(String interior_number) {
        this.interior_number = interior_number;
    }

    public String getExterior_number() {
        return exterior_number;
    }

    public void setExterior_number(String exterior_number) {
        this.exterior_number = exterior_number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getColony() {
        return colony;
    }

    public void setColony(String colony) {
        this.colony = colony;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<Direction> getDirections() {
        return directions;
    }

    public String getDirection_complete() {
        return direction_complete;
    }

    public void setDirection_complete(String direction_complete) {
        this.direction_complete = direction_complete;
    }

    public void setDirections(ArrayList<Direction> directions) {
        this.directions = directions;
    }
}
