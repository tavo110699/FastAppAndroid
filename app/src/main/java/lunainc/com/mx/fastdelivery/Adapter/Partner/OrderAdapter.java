package lunainc.com.mx.fastdelivery.Adapter.Partner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import lunainc.com.mx.fastdelivery.R;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import lunainc.com.mx.fastdelivery.Model.Order;
import lunainc.com.mx.fastdelivery.Utils.DateTime.DateTimeUtils;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    private List<Order> orders;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public OrderAdapter(Context context, List<Order> orders){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.orders = orders;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_order_partner, parent, false);

        return new OrderViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        DateTimeUtils.setTimeZone("America/Mexico_City");


        holder.name.setText("Cliente: "+order.getUser().getName()+" "+order.getUser().getLast_name_p()+" "+order.getUser().getLast_name_m());
        holder.direction.setText("Dirección: "+order.getDirection().getDirection_complete());
        Date date = DateTimeUtils.formatDate(order.getCreated_at());
        String timeAgo = DateTimeUtils.getTimeAgo(context, date);

        holder.total.setText("Total: "+order.getTotal()+" MXN, Pedido realizado: "+timeAgo);
        holder.statusText.setText("Pedido "+order.getStatus());
        if (!order.getStatus().equalsIgnoreCase("entregado")){

            holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.colorOrange));
            holder.statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
            holder.iconStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.orange_circle));
        }



    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        @BindView(R.id.statusView)
        View statusView;

        @BindView(R.id.iconStatus)
        View iconStatus;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.direction)
        TextView direction;

        @BindView(R.id.total)
        TextView total;

        @BindView(R.id.statusText)
        TextView statusText;

        @BindView(R.id.btnAction)
        Button btnAction;

        OrderViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnAction.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
