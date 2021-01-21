package lunainc.com.mx.fastdelivery.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import lunainc.com.mx.fastdelivery.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Cesta;


public class CestaAdapter extends RecyclerView.Adapter<CestaAdapter.CestaViewHolder>{


    private List<Cesta> cestas;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;

    private APIService apiService;
    private SharedPreferences sharedPref;
    private String token;


    public CestaAdapter(Context context, List<Cesta> cestas){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.cestas = cestas;


        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);

        apiService = ApiUtils.getAPIService();
        token = "Bearer " + sharedPref.getString(("token"), "noLogged");

    }

    @NonNull
    @Override
    public CestaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_cesta, parent, false);
        return new CestaViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CestaViewHolder holder, int position) {
        Cesta cesta = cestas.get(position);
        holder.name.setText(cesta.getProduct_name()+ " - "+cesta.getName_partner());
        holder.desc.setText(cesta.getDescripcion());
        holder.cantidad.setText("Cantidad: "+cesta.getCantidad()+
                " Total: "+( (Integer.parseInt(cesta.getCantidad()) * Float.parseFloat(cesta.getStatic_price_product())) + Float.parseFloat(cesta.getExtra())  )+" MXN");
        Glide.with(context).load(cesta.getProduct_image()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return cestas.size();
    }


    public class CestaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.desc)
        TextView desc;

        @BindView(R.id.cantidad)
        TextView cantidad;

        @BindView(R.id.image)
        RoundedImageView image;

        CestaViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mClickListener != null){
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null){
                mLongClickListener.onItemLongClick(v, getAdapterPosition());
            }

            return false;
        }
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.mLongClickListener = itemLongClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface ItemLongClickListener {
        void onItemLongClick(View view, int position);
    }



}
