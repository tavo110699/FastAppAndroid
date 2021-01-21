package lunainc.com.mx.fastdelivery.Model;

public class MessageEventResponseDriverToClient {

    public String message;

    public String id_order;

    public String id_driver;

    public String status;

    public MessageEventResponseDriverToClient(String message, String id_order, String id_driver, String status) {
        this.message = message;
        this.id_order = id_order;
        this.id_driver = id_driver;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getId_driver() {
        return id_driver;
    }

    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }
}
