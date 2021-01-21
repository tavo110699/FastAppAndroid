package lunainc.com.mx.fastdelivery.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import lunainc.com.mx.fastdelivery.R;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Model.FavoriteProduct;
import lunainc.com.mx.fastdelivery.Model.Product;
import lunainc.com.mx.fastdelivery.customfonts.MyTextView_Roboto_Regular;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.FavoriteProductViewHolder> {


    private ItemClickListener mClickListener;
    private Context context;
    private LayoutInflater mInflater;
    private List<FavoriteProduct> favorites;

    public FavoriteProductAdapter(Context context, List<FavoriteProduct> favorites){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoriteProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_favorite_product_client, parent, false);

        return new FavoriteProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavoriteProductViewHolder holder, int position) {

        Product product = favorites.get(position).getProduct();

        Glide.with(context).load(product.getImage()).into(holder.imageProduct);
        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText("Precio: "+product.getPrice()+" MXN");
        holder.categoryProduct.setText(product.getCategoria().getDescription());

    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }


    public class FavoriteProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageProduct)
        RoundedImageView imageProduct;

        @BindView(R.id.favoriteAction)
        ImageView favoriteAction;

        @BindView(R.id.nameProduct)
        MyTextView_Roboto_Regular nameProduct;

        @BindView(R.id.priceProduct)
        MyTextView_Roboto_Regular priceProduct;

        @BindView(R.id.categoryProduct)
        MyTextView_Roboto_Regular categoryProduct;



        FavoriteProductViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener!= null){
                mClickListener.onItemClick(v, getAdapterPosition());
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
