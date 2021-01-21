package lunainc.com.mx.fastdelivery.Utils;

import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;


public class FastDelivery extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(FastDelivery.this);
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
