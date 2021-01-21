package lunainc.com.mx.fastdelivery.UI.Activity.Client;

import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.TextBadgeItem;
import lunainc.com.mx.fastdelivery.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.MessageEventClientNotification;
import lunainc.com.mx.fastdelivery.Model.MessageEventPartnerOrder;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.UI.Fragment.Client.CarritoFragment;
import lunainc.com.mx.fastdelivery.UI.Fragment.Client.DescubreFragment;
import lunainc.com.mx.fastdelivery.UI.Fragment.Client.NotificationsFragment;
import lunainc.com.mx.fastdelivery.UI.Fragment.Client.HomeFragment;
import lunainc.com.mx.fastdelivery.UI.Fragment.Client.ProfileFragment;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import lunainc.com.mx.fastdelivery.Utils.DBHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.bottomBar)
    BottomNavigationBar bottomBar;


    private DBHelper db;
    private TextBadgeItem badgeItem;
    private TextBadgeItem badgeNotification;

    private APIService apiService;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        db = new DBHelper(this);
        apiService = ApiUtils.getAPIService();
        token = new Constants().getToken(this);
        badgeItem = new TextBadgeItem();
        badgeNotification = new TextBadgeItem();
        loadBadgeInfo();
        getNotifications();


        bottomBar.addItem(new BottomNavigationItem(R.drawable.ic_taxi, "Taxi").setInactiveIcon(getDrawable(R.drawable.ic_taxi)))
        .addItem(new BottomNavigationItem(R.drawable.ic_search_color, "Explorar").setInactiveIcon(getDrawable(R.drawable.ic_search_color)))
        .addItem(new BottomNavigationItem(R.drawable.ic_carrito, "Carrito").setInactiveIcon(getDrawable(R.drawable.ic_carrito)).setBadgeItem(badgeItem))
        .addItem(new BottomNavigationItem(R.drawable.ic_notificacion, "Notificaciones").setInactiveIcon(getDrawable(R.drawable.ic_notificacion)).setBadgeItem(badgeNotification))
        .addItem(new BottomNavigationItem(R.drawable.ic_ninja_profile, "Perfil").setInactiveIcon(getDrawable(R.drawable.ic_ninja_profile)))
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setMode(BottomNavigationBar.MODE_FIXED_NO_TITLE)
        .setFirstSelectedPosition(4)
        .initialise();
        changeView(4);






        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                changeView(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });



    }


    public void getNotifications(){
        apiService.getNotificationsCount("Accept", token).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {
                if (response.isSuccessful()){

                    int num = Integer.parseInt(Objects.requireNonNull(response.body()).getMessage());
                    loadBadgeNotification(num);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {

            }
        });

    }

    public void loadBadgeNotification(int num){

        if (num > 0){
            badgeNotification.setText(String.valueOf(num))
                    .setHideOnSelect(true);
        }else {
            badgeNotification.hide();
        }
    }
    public void loadBadgeInfo(){
        int numberItems = db.numberRows();

        if (numberItems > 0){
            badgeItem.setText(String.valueOf(numberItems))
                     .setHideOnSelect(true);
        }else {
            badgeItem.hide();
        }

    }

    public void changeView(int position){

        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);


        switch (position){

            case 0:
                new Constants().goTo(this, DriverOrderClient.class);
               break;
            case 1:

                DescubreFragment descubreFragment = new DescubreFragment();
                fragmentTransaction.replace(R.id.container, descubreFragment);
                fragmentTransaction.commit();


                break;
            case 2:
                CarritoFragment carritoFragment = new CarritoFragment();
                fragmentTransaction.replace(R.id.container, carritoFragment);
                fragmentTransaction.commit();
                break;
            case 3:

                NotificationsFragment notificationsFragment = new NotificationsFragment();
                fragmentTransaction.replace(R.id.container, notificationsFragment);
                fragmentTransaction.commit();

                break;
            case 4:

                ProfileFragment profileFragment = new ProfileFragment();
                fragmentTransaction.replace(R.id.container, profileFragment);
                fragmentTransaction.commit();
                break;

            default:


        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        new Constants().updateDeviceToken(apiService, new Constants().getToken(this));

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventClientNotification event) {
        Toasty.success(this, "Tienes una nueva notificacion").show();

    }

}
