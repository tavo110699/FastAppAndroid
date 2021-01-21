package lunainc.com.mx.fastdelivery.UI.Fragment.Client;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import lunainc.com.mx.fastdelivery.R;import com.sayantan.advancedspinner.MultiSpinner;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Adapter.IngredientsDialogAdapter;
import lunainc.com.mx.fastdelivery.Model.Ingredient;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.ProductDetailActivity;

public class ModifyIngredientsFragment extends DialogFragment {

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
    private IngredientsDialogAdapter ingredientsDialogAdapter;


    private ArrayList<Ingredient> ingredients;
    private  ArrayList<String> list;
    public ModifyIngredientsFragment newInstance(int numProducts, Serializable param1){
        ModifyIngredientsFragment f = new ModifyIngredientsFragment();
        Bundle args = new Bundle();
        args.putInt("numProducts", numProducts);
        args.putSerializable("ingredients", param1);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numProducts = Objects.requireNonNull(getArguments()).getInt("numProducts");
        ingredients = (ArrayList<Ingredient>)  getArguments().getSerializable("ingredients");
        list=new ArrayList<>();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_ingredients, container, false);
        ButterKnife.bind(this, view);
        configViews();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        setData();
        createItems();
        events();



    }

    private void events() {
        actionBtn.setOnClickListener(v -> dismiss());

    }


    private void configViews(){

        toolbar.setTitle("Editar ingredientes");
        toolbar.setNavigationIcon(R.drawable.ic_close_dialog);
        toolbar.setNavigationOnClickListener(v -> dismiss());


        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @SuppressLint("SetTextI18n")
    private void setData(){
        sizeProducts.setText(getString(R.string.size_products_dialog)+" "+ numProducts);
        for (int i = 0; i < ingredients.size(); i++) {
            list.add(ingredients.get(i).getIngredient().getDescription());

        }



    }


    @SuppressLint("SetTextI18n")
    private void createItems(){
        ingredientsDialogAdapter = new IngredientsDialogAdapter(getActivity(),numProducts,ingredients,list, true);
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
                    selectedIngredients.add(ingredients.get(mul.getSelectedItemsIndex().get(j)).getIngredient());
                    ingredientsResult[i] = ingredients.get(mul.getSelectedItemsIndex().get(j)).getIngredient();

                }

                ingredientsResult[i].setIngredients(selectedIngredients);

            }


        }

        ((ProductDetailActivity) Objects.requireNonNull(getActivity())).getDataFromModifyFragment(numProducts, ingredientsResult);
    }



}
