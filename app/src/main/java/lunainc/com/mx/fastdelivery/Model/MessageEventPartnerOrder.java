package lunainc.com.mx.fastdelivery.Model;

public class MessageEventPartnerOrder {


    public String message;

    public String id_order;

    public MessageEventPartnerOrder(String message, String id_order) {
        this.message = message;
        this.id_order = id_order;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }
}
