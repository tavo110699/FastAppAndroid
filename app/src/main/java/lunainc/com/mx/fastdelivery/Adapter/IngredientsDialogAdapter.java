package lunainc.com.mx.fastdelivery.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lunainc.com.mx.fastdelivery.R;import com.sayantan.advancedspinner.MultiSpinner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Model.Ingredient;

public class IngredientsDialogAdapter extends RecyclerView.Adapter<IngredientsDialogAdapter.ModifyIngredientsViewHolder>{

    private Context context;
    private LayoutInflater mInlflater;
    private int numProducts;
    private List<Ingredient> ingredients;
    private List<String> list;
    private boolean allSelect;

    public IngredientsDialogAdapter(Context context, int numProducts, List<Ingredient> ingredients, List<String> list, boolean allSelect){
        this.context = context;
        this.mInlflater = LayoutInflater.from(context);
        this.numProducts = numProducts;
        this.ingredients = ingredients;
        this.list = list;
        this.allSelect = allSelect;
    }

    @NonNull
    @Override
    public ModifyIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInlflater.inflate(R.layout.item_ingredients_detail, parent, false);
        return new ModifyIngredientsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ModifyIngredientsViewHolder holder, int position) {
        holder.text.setText("Pieza "+(position+1));

        holder.multi.setSpinnerList(list);
        if (allSelect){
            holder.multi.selectAll();
        }



    }

    @Override
    public int getItemCount() {
        return numProducts;
    }


    public class ModifyIngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        TextView text;

        @BindView(R.id.multi)
        MultiSpinner multi;


        ModifyIngredientsViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);




        }

    }

}
