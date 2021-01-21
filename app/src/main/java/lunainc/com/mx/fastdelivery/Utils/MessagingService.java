package lunainc.com.mx.fastdelivery.Utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import lunainc.com.mx.fastdelivery.Model.MessageEventDriver;
import lunainc.com.mx.fastdelivery.Model.MessageEventResponseDriverToClient;
import lunainc.com.mx.fastdelivery.R;
import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.MessageEventClientNotification;
import lunainc.com.mx.fastdelivery.Model.MessageEventPartnerOrder;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.DriverOrderClient;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.HomeActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Conductor.DriverHomeActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Socio.HomePartnerActivity;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MessagingService extends FirebaseMessagingService{

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        APIService apiService = ApiUtils.getAPIService();

        new Constants().updateTokenFromNew(apiService, s, new Constants().getToken(this));
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_white_24dp);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        //Check android version

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "fastappnotification";
            CharSequence name = "fastappnotification";
            String description = "Channel description";


            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setShowBadge(false);
            Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "fastappnotification")
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_check_white_24dp)
                .setLargeIcon(bitmap)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);


        if (Objects.requireNonNull(remoteMessage.getData().get("type_acc")).equals("partner")){
            Intent intent = new Intent(this, HomePartnerActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(HomePartnerActivity.class);
            stackBuilder.addNextIntent(intent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
            notification.setContentIntent(resultPendingIntent);

            EventBus.getDefault().post(new MessageEventPartnerOrder(remoteMessage.getData().get("type_acc"),
                    remoteMessage.getData().get("id_order")));

            Objects.requireNonNull(notificationManager).notify(0, notification.build());
        }else if(Objects.requireNonNull(remoteMessage.getData().get("type_acc")).equals("client")){

            Intent intent = new Intent(this, HomeActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(HomeActivity.class);
            stackBuilder.addNextIntent(intent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
            notification.setContentIntent(resultPendingIntent);

            EventBus.getDefault().post(new MessageEventClientNotification(remoteMessage.getData().get("title"),
                    remoteMessage.getData().get("body"),
                    remoteMessage.getData().get("notification_id")));

        }else if(Objects.requireNonNull(remoteMessage.getData().get("type_acc")).equals("driver")){

            Intent intent = new Intent(this, DriverHomeActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(DriverHomeActivity.class);
            stackBuilder.addNextIntent(intent);


            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
            notification.setContentIntent(resultPendingIntent);


            EventBus.getDefault().post(new MessageEventDriver(remoteMessage.getNotification().getBody(),
                    remoteMessage.getData().get("direction"),
                    remoteMessage.getData().get("id_order"),
                    remoteMessage.getData().get("id_client")
                    ));

        }else if(Objects.requireNonNull(remoteMessage.getData().get("type_acc")).equals("client_ride")){
            Intent intent = new Intent(this, DriverOrderClient.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(DriverOrderClient.class);
            stackBuilder.addNextIntent(intent);


            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
            notification.setContentIntent(resultPendingIntent);


            EventBus.getDefault().post(new MessageEventResponseDriverToClient(remoteMessage.getNotification().getBody(),
                    remoteMessage.getData().get("id_order"),
                    remoteMessage.getData().get("id_driver"),
                    remoteMessage.getData().get("status")
            ));
        }


        notificationManager.notify(0, notification.build());
    }
}
