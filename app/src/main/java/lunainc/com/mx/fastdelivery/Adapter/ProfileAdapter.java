package lunainc.com.mx.fastdelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import lunainc.com.mx.fastdelivery.R;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Model.ProfileItem;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>{


    private ItemClickListener mClickListener;
    private List<ProfileItem> items;
    private Context context;
    private LayoutInflater mInflater;


    public ProfileAdapter(Context context, List<ProfileItem> items){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.items = items;

    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_profile_client, parent, false);

        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        ProfileItem item = items.get(position);
        Glide.with(context).load(item.getImage()).into(holder.itemIconAction);
        holder.itemTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.itemIconAction)
        ImageView itemIconAction;


        @BindView(R.id.itemTitle)
        TextView itemTitle;

        ProfileViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);


            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null){
                mClickListener.onItemClick(v, getAdapterPosition());
            }

        }
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
