package lunainc.com.mx.fastdelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lunainc.com.mx.fastdelivery.R;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Model.Notification;
import lunainc.com.mx.fastdelivery.Utils.DateTime.DateTimeUtils;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    private Context context;
    private LayoutInflater mInflater;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        DateTimeUtils.setTimeZone("America/Mexico_City");
        Date date = DateTimeUtils.formatDate(notification.getCreated_at());
        String timeAgo = DateTimeUtils.getTimeAgo(context, date);

        holder.title.setText(notification.getTitle());
        holder.message.setText(notification.getMessage());
        holder.time.setText(timeAgo);

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


    public class NotificationViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.message)
        TextView message;

        @BindView(R.id.time)
        TextView time;

        NotificationViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
