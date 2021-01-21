package lunainc.com.mx.fastdelivery.UI.Activity.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Adapter.IntroViewPagerAdapter;
import lunainc.com.mx.fastdelivery.Model.ScreenItem;
import lunainc.com.mx.fastdelivery.R;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.HomeActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Conductor.DriverHomeActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Socio.HomePartnerActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.screen_viewpager)
    ViewPager screen_viewpager;

    @BindView(R.id.tab_indicator)
    TabLayout tab_indicator;

    @BindView(R.id.btn_next)
    Button btnNext;

    @BindView(R.id.btn_get_started)
    Button btnGetStarted;

    @BindView(R.id.tv_skip)
    TextView tvSkip;

    private Animation btnAnim;

    private int position = 0 ;

    private IntroViewPagerAdapter introViewPagerAdapter;

    /**
     *  @BindView(R.id.loginAccount)
     *     Button loginAccount;
     *
     *     @BindView(R.id.createNewAccount)
     *     Button createNewAccount;
     */


    private List<ScreenItem> mList;
    private String token = "";
    private String typeAccount = "";
    private SharedPreferences sharedPreferences;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configViews();
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        initVars();

    }


    private void configViews(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    private void initVars(){
        context = this.getApplicationContext();
        sharedPreferences = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);
        token = sharedPreferences.getString(("token"), "noLogged");
        typeAccount = sharedPreferences.getString(("type_acc"), "noLogged");

        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
    }


    @Override
    protected void onStart() {
        super.onStart();
        checkSession();

        loadData();
        events();
    }


    private void loadData(){
        mList = new ArrayList<>();
        mList.add(new ScreenItem("Ordena comida","Ordena tu comida de manera, rapida y segura a tus establecimientos favoritos afiliados a Fast Delivery App",R.drawable.order_products_food));
        mList.add(new ScreenItem("Ordena tu super","Ahora hacer el super es más sencillo, ordena tus productos desde la app de Fast Delivery",R.drawable.order_super));
        mList.add(new ScreenItem("Transporte seguro","¿Necesitas transporte? pidelo por la app de manera sencilla, segura y confiable.",R.drawable.order_rider));
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screen_viewpager.setAdapter(introViewPagerAdapter);
        tab_indicator.setupWithViewPager(screen_viewpager);

    }

    private void events() {



        btnNext.setOnClickListener(v -> {

            position = screen_viewpager.getCurrentItem();
            if (position < mList.size()) {

                position++;
                screen_viewpager.setCurrentItem(position);


            }

            if (position == mList.size()-1) { // when we rech to the last screen

                // TODO : show the GETSTARTED Button and hide the indicator and the next button

                loaddLastScreen();


            }



        });



        tab_indicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {

                    loaddLastScreen();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        btnGetStarted.setOnClickListener(v -> new Constants().goTo(StartActivity.this, LoginActivity.class));


        tvSkip.setOnClickListener(v -> screen_viewpager.setCurrentItem(mList.size()));




    }

    private void loaddLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tab_indicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim);



    }



    public void checkSession(){

        if (!token.equals("noLogged") &&  !token.isEmpty()){

            if(!typeAccount.equals("noLogged") && !typeAccount.isEmpty()){

                switch (Integer.parseInt(typeAccount)){

                    case 1://cliente
                        goToHome();
                        break;

                    case 2://repartidor
                        break;
                    case 3://conductor
                        goToHomeDriver();
                        break;
                    case 4://socio
                        goToHomePartner();
                        break;

                    default:
                        break;


                }


            }



        }


    }


    public void goToHome(){

        Intent intent = new Intent(context, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private void goToHomeDriver(){
        Intent intent = new Intent(context, DriverHomeActivity.class);
        startActivity(intent);
        finish();

    }


    public void goToHomePartner(){
        Intent intent = new Intent(context, HomePartnerActivity.class);
        startActivity(intent);
        finish();
    }


}
