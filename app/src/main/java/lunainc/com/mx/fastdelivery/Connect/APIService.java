package lunainc.com.mx.fastdelivery.Connect;

import lunainc.com.mx.fastdelivery.Model.Categoria;
import lunainc.com.mx.fastdelivery.Model.Direction;
import lunainc.com.mx.fastdelivery.Model.Driver;
import lunainc.com.mx.fastdelivery.Model.FavoriteProduct;
import lunainc.com.mx.fastdelivery.Model.Ingredient;
import lunainc.com.mx.fastdelivery.Model.Notification;
import lunainc.com.mx.fastdelivery.Model.Order;
import lunainc.com.mx.fastdelivery.Model.OrderDriver;
import lunainc.com.mx.fastdelivery.Model.Partner;
import lunainc.com.mx.fastdelivery.Model.Product;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.Model.ResponseLRClient;
import lunainc.com.mx.fastdelivery.Model.User;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIService {

    /**
     * Login and Register client
     */
    @POST("api/v1/register")
    Call<ResponseLRClient> registerUser(@Body RequestBody body);
    @POST("api/v1/login")
    Call<ResponseLRClient> loginUser(@Body RequestBody body);
    @POST("api/v1/logout")
    Call<ResponseDefault>  logoutUser(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/user/update/deviceToken")
    Call<ResponseDefault> updateToken(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/user/update/data")
    Call<ResponseDefault> updateData(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/user/update/photo")
    Call<ResponseDefault> updatePhoto(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);




    /**
     * Notification Client
     */
    @POST("api/v1/notifications")
    Call<Notification> getNotifications(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/notifications/count")
    Call<ResponseDefault> getNotificationsCount(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/notifications/read")
    Call<ResponseDefault> readAllNotifications(@Header("Accept") String accept, @Header("Authorization") String Auth);

    /**
     * Client Directions
     */

    @POST("api/v1/user/directions")
    Call<Direction> getDirections(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/user/directions/show")
    Call<Direction> getDirection(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/user/directions/create")
    Call<ResponseDefault> createDirection(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/user/directions/update")
    Call<ResponseDefault> updateDirection(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/user/directions/delete")
    Call<ResponseDefault> deleteDirection(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);





    @POST("api/v1/user")
    Call<User> getUserData(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/user/driver")
    Call<User> getUserDataDriver(@Header("Accept") String accept, @Header("Authorization") String Auth);





    @POST("api/v1/categorias")
    Call<Categoria> getCategorias(@Header("Accept") String accept, @Header("Authorization") String Auth);




    /**
     * Products Client Route
     */

    @POST("api/v1/products")
    Call<Product> getProductsClient(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/products/ingredients")
    Call<Ingredient> getIngredientsClient(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/products/show")
    Call<Product> getProductClient(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/products/search")
    Call<Product> searchProduct(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);


    /**
     * Orders Client Route
     */

    @POST("api/v1/orders")
    Call<Order> getOrders(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/orders")
    Call<Order> getOrder(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/orders/create")
    Call<Order> createOrders(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/orders/add/products")
    Call<ResponseDefault> addProductsToOrders(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    /**
     * Order driver
     */
    @POST("api/v1/orders/drivers/create")
    Call<OrderDriver> createOrderDriver(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/orders/drivers/search/driver")
    Call<Driver> searchDriverToOrder(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/orders/drivers/search/driver/another")
    Call<Driver> searchDriverAnotherToOrder(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/orders/drivers/accept")
    Call<ResponseDefault> acceptDriverByClient(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/orders/drivers/finish/ride")
    Call<ResponseDefault> finishRideByClient(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);




    /**
     * Driver Routes
     */
    @POST("api/v1/drivers/update/location")
    Call<ResponseDefault> updateLocationDriver(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/drivers/update/status")
    Call<ResponseDefault> updateStatusDriver(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);


    @POST("api/v1/drivers/request/ride")
    Call<ResponseDefault> requestRide(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/drivers/order/data")
    Call<OrderDriver> orderDataRide(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);





    /**
     * Send Notification Route
     */
    @POST("api/v1/send/notification/partner")
    Call<ResponseDefault> sendNotificationToPartner(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);


    /**
     * Favorites Products Route
     */

    @POST("api/v1/products/favorite")
    Call<ResponseDefault> favoriteProduct(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/products/favorites/show")
    Call<FavoriteProduct> showFavoriteProducts(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/products/favorites/verify")
    Call<ResponseDefault> verifyFavoriteProducts(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);



    /**
     * Partner Routes
     */

    @POST("api/v1/partner")
    Call<Partner> getPartner(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/partner/orders")
    Call<Order> getOrdersPartner(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/partner/orders/show")
    Call<Order> getOrderPartner(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/partner/orders/change/status")
    Call<ResponseDefault> changeStatusOrder(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);



    /**
     * Partner ingredients Routes
     */

    @POST("api/v1/partner/ingredients")
    Call<Ingredient> getIngredients(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/partner/ingredients/show")
    Call<Ingredient> getIngredient(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/partner/ingredients/create")
    Call<ResponseDefault> createIngredient(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/partner/ingredients/update")
    Call<ResponseDefault> updateIngredient(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/partner/ingredients/delete")
    Call<ResponseDefault> deleteIngredient(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);


    /**
     * Partner products Routes
     */

    @POST("api/v1/partner/products")
    Call<Product> getProducts(@Header("Accept") String accept, @Header("Authorization") String Auth);

    @POST("api/v1/partner/products/show")
    Call<Product> getProduct(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/partner/products/create")
    Call<ResponseDefault> createProduct(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/partner/products/update")
    Call<ResponseDefault> updateProduct(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);

    @POST("api/v1/partner/products/delete")
    Call<ResponseDefault> deleteProduct(@Header("Accept") String accept, @Header("Authorization") String Auth, @Body RequestBody body);











}
