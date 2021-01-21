package lunainc.com.mx.fastdelivery.UI.Fragment.Client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Adapter.ProfileAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.ProfileItem;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.Model.User;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.AccountClientActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.Directions.DirectionsActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.FavoritesActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.HistorialClientActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Login.LoginActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Login.StartActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment  extends Fragment implements ProfileAdapter.ItemClickListener {

    private View view;

    @BindView(R.id.profileImage)
    CircleImageView profileImage;

    @BindView(R.id.userName)
    TextView userName;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String token = "";
    private APIService apiService;
    private SharedPreferences sharedPref;
    private Context context;

    private ProfileAdapter profileAdapter;
    private ArrayList<ProfileItem> items;
    private User userG;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this, view);
        initVars();
        return view;
    }

    private void initVars() {

        context = getActivity().getApplicationContext();
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);

        apiService = ApiUtils.getAPIService();
        token = "Bearer " + sharedPref.getString(("token"), "noLogged");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        loadDataList();

        profileAdapter = new ProfileAdapter(context, items);
        profileAdapter.notifyDataSetChanged();
        profileAdapter.setClickListener(this);


        recyclerView.setAdapter(profileAdapter);



    }


    @Override
    public void onStart() {
        super.onStart();
        getDataUser();

    }

    private void getDataUser() {

        apiService.getUserData("Accept", token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {

                if(response.isSuccessful()){
                    User user = response.body();
                    userG = user;


                   try{
                       SharedPreferences.Editor editor = sharedPref.edit();
                       editor.putString("username", user.getName());
                       editor.commit();
                       editor.apply();
                   }catch (NullPointerException nulP){
                       Log.e("Error", nulP.getMessage());
                   }

                    if (user.getImage().equals("avatar.png")){
                        Glide.with(context).load(new Constants().CURRENT_URL+"/avatars/"+user.getId()+"/avatar.png")
                        .into(profileImage);
                    }else{
                        Glide.with(context).load(user.getImage())
                                .into(profileImage);
                    }


                   String fullName = user.getName()+" "+user.getLast_name_p()+" "+user.getLast_name_m();

                    userName.setText(fullName);




                }

            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {

            }
        });

    }


    private void loadDataList(){
        items = new ArrayList<ProfileItem>();

        items.add(new ProfileItem("Historial", R.drawable.ic_history));
        items.add(new ProfileItem("Favoritos", R.drawable.ic_favorite));
        items.add(new ProfileItem("Direcciones", R.drawable.ic_direction));
        items.add(new ProfileItem("Tu cuenta", R.drawable.ic_personal_info));
        items.add(new ProfileItem("Compartir con amigos", R.drawable.ic_compartir));
        items.add(new ProfileItem("Quejas y sugerencias", R.drawable.ic_quejas_sugerencias));
        items.add(new ProfileItem("Cerrar sesión", R.drawable.ic_logout));


    }


    @Override
    public void onItemClick(View view, int position) {

        Intent intent;
        switch (position){
            case 0:
                intent = new Intent(context, HistorialClientActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(context, FavoritesActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(context, DirectionsActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(context, AccountClientActivity.class);
                intent.putExtra("user", userG);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
                break;
            case 4:
                shareAction();
                break;
            case 5:
                quejas();
                break;
            case 6:
                logOut();
                break;
            default:
                break;

        }

    }



    private void quejas(){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "fastappdelivery@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Buzon de sugerencias/quejas");
        email.putExtra(Intent.EXTRA_TEXT, "Escribe el mensaje");

        //need this to prompts email client only
        email.setType("message/rfc822");

       startActivity(Intent.createChooser(email, "Elige un cliente de correo electronico: "));

    }

    private void shareAction(){


        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Descarga Fast Delivery App para realizar tus pedidos");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Fast Delivery App");
        startActivity(Intent.createChooser(sharingIntent, "Compartir en: "));

    }


    private void logOut(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("Cerrar sesión")
                .setMessage("¿Realmente deseas cerrar tu sesión?")
                .setPositiveButton("Aceptar", (dialog, id) -> {



                    apiService.logoutUser("Accept", token).enqueue(new Callback<ResponseDefault>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {
                            if (response.isSuccessful()){

                                if (response.body().getStatus().equals("success")){

                                    Toasty.success(context, response.body().getMessage()).show();
                                    sharedPref.edit().clear().commit();
                                    sharedPref.edit().clear().apply();
                                    new Constants().goTo(getActivity(), StartActivity.class);

                                }else{
                                    Toasty.error(context, response.body().getMessage()).show();

                                }

                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                            Log.e("Error", t.getMessage());
                        }
                    });

                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();


    }


}
