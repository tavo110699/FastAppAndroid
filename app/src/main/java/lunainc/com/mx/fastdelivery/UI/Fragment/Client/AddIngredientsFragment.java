package lunainc.com.mx.fastdelivery.UI.Fragment.Client;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import lunainc.com.mx.fastdelivery.R;import com.sayantan.advancedspinner.MultiSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Adapter.IngredientsDialogAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Ingredient;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.ProductDetailActivity;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddIngredientsFragment extends DialogFragment {


    private View view;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sizProducts)
    TextView sizeProducts;

    @BindView(R.id.containerItems)
    LinearLayout containerItems;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.actionBtn)
    FloatingActionButton actionBtn;

    private int numProducts = 0;
    private String token;
    private String partner_id;
    private APIService apiService;
    private ArrayList<Ingredient> ingredients;
    private  ArrayList<String> list;
    private IngredientsDialogAdapter ingredientsDialogAdapter;

    public AddIngredientsFragment newInstance(int numProducts, String tokenAPI, String partner_id){
        AddIngredientsFragment f = new AddIngredientsFragment();
        Bundle args = new Bundle();
        args.putInt("numProducts", numProducts);
        args.putString("token", tokenAPI);
        args.putString("partner_id", partner_id);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numProducts = Objects.requireNonNull(getArguments()).getInt("numProducts");
        token = getArguments().getString("token");
        partner_id = getArguments().getString("partner_id");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_ingredients, container, false);
        ButterKnife.bind(this, view);
        initVars();
        configViews();



        return view;
    }

    private void initVars(){
        apiService = ApiUtils.getAPIService();

        list=new ArrayList<>();

        ingredients = new ArrayList<Ingredient>();

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("partner_id", partner_id)
                .build();

        apiService.getIngredientsClient("Accept", token, body).enqueue(new Callback<Ingredient>() {
            @Override
            public void onResponse(@NotNull Call<Ingredient> call, @NotNull Response<Ingredient> response) {


                if (response.isSuccessful()){

                    if (Objects.requireNonNull(response.body()).getStatus().equals("success")){

                        int tam = response.body().getIngredients().size();

                        if ( tam > 0){



                            for (int i = 0; i < tam; i++) {
                                Ingredient ingredient = (Ingredient) response.body().getIngredients().get(i);
                                ingredients.add(ingredient);
                                list.add(ingredient.getDescription()+" - $"+ingredient.getPrice()+" MXN");

                            }
                            createItems();


                        }


                    }else {
                        Toasty.warning(Objects.requireNonNull(getActivity()), "Ocurrio un error " + response.code(), Toast.LENGTH_SHORT, true).show();

                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<Ingredient> call, @NotNull Throwable t) {

            }
        });


    }


    private void configViews(){

        toolbar.setTitle("Agregar ingredientes");
        toolbar.setNavigationIcon(R.drawable.ic_close_dialog);
        toolbar.setNavigationOnClickListener(v -> dismiss());

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onStart() {
        super.onStart();
        setData();
        events();

    }

    private void events() {
        actionBtn.setOnClickListener(v -> dismiss());

    }

    @SuppressLint("SetTextI18n")
    private void createItems(){
        ingredientsDialogAdapter = new IngredientsDialogAdapter(getActivity(), numProducts, ingredients, list, false);
        ingredientsDialogAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(ingredientsDialogAdapter);




    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {

        Ingredient[] ingredientsResult= new Ingredient[numProducts];


        for (int i = 0; i < numProducts; i++) {

            //getElement (MultiSpinner)
            MultiSpinner mul = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(i))
                    .itemView.findViewById(R.id.multi);

            //auxiliar para guardar la lista de ingredientes seleccionado en la pieza 'n'

            if (mul.getSelectedItemsIndex().size() > 0){
                ArrayList<Ingredient> selectedIngredients = new ArrayList<>();

                for (int j = 0; j < mul.getSelectedItemsIndex().size(); j++) {
                    // Agregar el ingredinete a la lista de ingredientes de la pieza 'n'
                    selectedIngredients.add(ingredients.get(mul.getSelectedItemsIndex().get(j)));
                    ingredientsResult[i] = ingredients.get(mul.getSelectedItemsIndex().get(j));

                }

                ingredientsResult[i].setIngredients(selectedIngredients);
            }



        }
        
        ((ProductDetailActivity) Objects.requireNonNull(getActivity())).getDataFromAddFragment(numProducts, ingredientsResult);
    }

    @SuppressLint("SetTextI18n")
    private void setData(){

        sizeProducts.setText(getString(R.string.size_products_dialog)+" "+ numProducts);

    }

}
