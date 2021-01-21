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
import lunainc.com.mx.fastdelivery.Model.Product;
import lunainc.com.mx.fastdelivery.customfonts.MyTextView_Roboto_Regular;

public class ProductClientAdapter extends RecyclerView.Adapter<ProductClientAdapter.ProductClientViewHolder> {

    private List<Product> products;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;

    public ProductClientAdapter(Context context, List<Product> products){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.products = products;
    }

    @NonNull
    @Override
    public ProductClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_products_client, parent, false);
        return new ProductClientViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductClientViewHolder holder, int position) {
        Product product = products.get(position);


        Glide.with(context).load(product.getImage()).into(holder.imageProduct);
        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText("Precio: "+product.getPrice()+" MXN");
        holder.categoryProduct.setText(product.getCategoria().getDescription());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ProductClientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{


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


        ProductClientViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mLongClickListener != null){
                mLongClickListener.onItemLongClick(view, getAdapterPosition());
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
