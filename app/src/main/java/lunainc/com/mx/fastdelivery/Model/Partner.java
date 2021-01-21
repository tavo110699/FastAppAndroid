package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Partner  implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("rfc")
    @Expose
    private String rfc;

    @SerializedName("type_payment")
    @Expose
    private String type_payment;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("street")
    @Expose
    private String street;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("colony")
    @Expose
    private String colony;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("cp")
    @Expose
    private String cp;

    @SerializedName("direction_complete")
    @Expose
    private String direction_complete;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("visibility")
    @Expose
    private String visibility;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("partners")
    @Expose
    private ArrayList<Partner> partners;

    public Partner() {
    }

    public Partner(String status, String message, String id, String name, String rfc, String type_payment, String longitude, String latitude, String street, String city, String colony, String state, String country, String cp, String direction_complete, String status1, String visibility, String created_at) {
        this.id = id;
        this.name = name;
        this.rfc = rfc;
        this.type_payment = type_payment;
        this.longitude = longitude;
        this.latitude = latitude;
        this.street = street;
        this.city = city;
        this.colony = colony;
        this.state = state;
        this.country = country;
        this.cp = cp;
        this.direction_complete = direction_complete;
        this.status = status1;
        this.visibility = visibility;
        this.created_at = created_at;
    }

    public Partner(String status, String message, String id, String name, String rfc, String type_payment, String longitude, String latitude, String street, String city, String colony, String state, String country, String cp, String direction_complete, String status1, String visibility, String created_at, ArrayList<Partner> partners) {
        this.id = id;
        this.name = name;
        this.rfc = rfc;
        this.type_payment = type_payment;
        this.longitude = longitude;
        this.latitude = latitude;
        this.street = street;
        this.city = city;
        this.colony = colony;
        this.state = state;
        this.country = country;
        this.cp = cp;
        this.direction_complete = direction_complete;
        this.status = status1;
        this.visibility = visibility;
        this.created_at = created_at;
        this.partners = partners;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getType_payment() {
        return type_payment;
    }

    public void setType_payment(String type_payment) {
        this.type_payment = type_payment;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getColony() {
        return colony;
    }

    public void setColony(String colony) {
        this.colony = colony;
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

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getDirection_complete() {
        return direction_complete;
    }

    public void setDirection_complete(String direction_complete) {
        this.direction_complete = direction_complete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<Partner> getPartners() {
        return partners;
    }

    public void setPartners(ArrayList<Partner> partners) {
        this.partners = partners;
    }
}
