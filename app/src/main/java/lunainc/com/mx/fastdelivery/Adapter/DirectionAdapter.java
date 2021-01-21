package lunainc.com.mx.fastdelivery.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import lunainc.com.mx.fastdelivery.R;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Model.Direction;

public class DirectionAdapter extends RecyclerView.Adapter<DirectionAdapter.DirectionViewHolder> {


    private Context context;
    private LayoutInflater mInflater;
    private List<Direction> directions;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;

    public DirectionAdapter(Context context, List<Direction> directions){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.directions = directions;

    }

    @NonNull
    @Override
    public DirectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_direction_client, parent, false);
        return new DirectionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DirectionViewHolder holder, int position) {

        Direction direction = directions.get(position);
        holder.principalLine.setText(direction.getStreet()+" "+direction.getColony());
        holder.secondaryLine.setText(direction.getCity()+" "+direction.getState());
        holder.numDirection.setText("Numero: "+direction.getExterior_number());
        holder.postalCode.setText("C.P. "+direction.getPostal_code());
    }

    @Override
    public int getItemCount() {
        return directions.size();
    }


    public class DirectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        @BindView(R.id.principalLine)
        TextView principalLine;

        @BindView(R.id.secondaryLine)
        TextView secondaryLine;

        @BindView(R.id.numDirection)
        TextView numDirection;

        @BindView(R.id.postalCode)
        TextView postalCode;

        @BindView(R.id.btnAction)
        Button btnAction;

        DirectionViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

            btnAction.setOnClickListener(this);
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
            mLongClickListener.onItemLongClick(v, getAdapterPosition());
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
