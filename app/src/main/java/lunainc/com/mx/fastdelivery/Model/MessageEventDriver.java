package lunainc.com.mx.fastdelivery.Model;

public class MessageEventDriver {

    public String message;

    public String direction;

    public String id_order;

    public String id_client;

    public MessageEventDriver(String message, String direction, String id_order, String id_client) {
        this.message = message;
        this.direction = direction;
        this.id_order = id_order;
        this.id_client = id_client;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }
}
