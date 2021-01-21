package lunainc.com.mx.fastdelivery.Adapter.Partner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Model.Ingredient;
import lunainc.com.mx.fastdelivery.R;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{


    private List<Ingredient> ingredients;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;
    private LayoutInflater mInflater;


    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.mInflater = LayoutInflater.from(context);
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_ingredients_partner, parent, false);

        return new IngredientViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.name.setText(ingredient.getDescription());
        holder.price.setText("Precio: "+ingredient.getPrice()+" MXN");

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.price)
        TextView price;

        IngredientViewHolder(View itemView){
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

    public void setLongClickListener(ItemLongClickListener itemLongClickListener){
        this.mLongClickListener = itemLongClickListener;
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public interface ItemLongClickListener{
        void onItemLongClick(View view, int position);
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
