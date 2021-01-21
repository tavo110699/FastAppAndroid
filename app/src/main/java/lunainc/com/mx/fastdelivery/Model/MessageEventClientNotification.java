package lunainc.com.mx.fastdelivery.Model;

public class MessageEventClientNotification {

    public String title;

    public String message;

    public String notification_id;

    public MessageEventClientNotification(String title, String message, String notification_id) {
        this.title = title;
        this.message = message;
        this.notification_id = notification_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }
}
