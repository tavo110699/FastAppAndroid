package lunainc.com.mx.fastdelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import lunainc.com.mx.fastdelivery.R;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Model.Categoria;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import lunainc.com.mx.fastdelivery.customfonts.TextView_Helvetica_Neue_bold;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriaViewHolder> {

    private List<Categoria> categorias;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public CategoriasAdapter(Context context, List<Categoria> categorias){
        this.mInflater = LayoutInflater.from(context);
        this.categorias = categorias;
    }


    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_descubre, parent, false);

        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);

        holder.title.setText(categoria.getDescription());

       // Glide.with(holder.itemView.getContext()).load(new Constants().CURRENT_URL+"/"+categoria.getImage()).into(holder.imageView);

        Picasso.get().load(new Constants().CURRENT_URL+"/"+categoria.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }


    public class CategoriaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public @BindView(R.id.image)
        ImageView imageView;

        public @BindView(R.id.title)
        TextView_Helvetica_Neue_bold title;

        CategoriaViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
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
