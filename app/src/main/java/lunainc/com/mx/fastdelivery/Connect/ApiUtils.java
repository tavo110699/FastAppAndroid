package lunainc.com.mx.fastdelivery.Connect;

public class ApiUtils {

    private ApiUtils() {}

    private static final String BASE_URL = "http://192.168.1.76:8080";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
