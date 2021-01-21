package lunainc.com.mx.fastdelivery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLRClient {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("type_acc")
    @Expose
    private String type_acc;

    @SerializedName("token")
    @Expose
    private String token;

    public ResponseLRClient() {
    }

    public ResponseLRClient(String status, String message, String type_acc, String token) {
        this.status = status;
        this.message = message;
        this.type_acc = type_acc;
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType_acc() {
        return type_acc;
    }

    public void setType_acc(String type_acc) {
        this.type_acc = type_acc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
