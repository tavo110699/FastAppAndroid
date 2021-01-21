package lunainc.com.mx.fastdelivery.UI.Fragment.Client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import lunainc.com.mx.fastdelivery.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lunainc.com.mx.fastdelivery.Adapter.CategoriasAdapter;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.Categoria;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.ShowListProductsActivity;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.ShowListSearchResultActivity;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescubreFragment extends Fragment implements CategoriasAdapter.ItemClickListener {


    private View view;
    
    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;

    @BindView(R.id.recyclerView)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.refreh)
    SwipeRefreshLayout refresh;
    private String token = "";
    private APIService apiService;
    private SharedPreferences sharedPref;
    private CategoriasAdapter categoriasAdapter;
    private ArrayList<Categoria> categorias;
    private Context context;
    private String completeToken = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.descubre_fragment, container, false);
        ButterKnife.bind(this, view);
        initVars();

        return view;
    }


    public void initVars() {
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        sharedPref = context.getSharedPreferences(
                "credentials", Context.MODE_PRIVATE);

        apiService = ApiUtils.getAPIService();
        token = sharedPref.getString(("token"), "noLogged");
        completeToken = "Bearer " + token;

        categorias = loadData();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

       reloadData();





        recyclerView.postDelayed(() -> recyclerView.hideShimmerAdapter(), 3000);



        refresh.setOnRefreshListener(() -> {
            reloadData();

            recyclerView.postDelayed(() -> {
                recyclerView.hideShimmerAdapter();
                refresh.setRefreshing(false);
            }, 3000);


        });


        new Constants().updateDeviceToken(apiService, completeToken);
        
        
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                
            }

            @Override
            public void onSearchAction(String currentQuery) {


                Intent intent = new Intent(getActivity(), ShowListSearchResultActivity.class);
                intent.putExtra("search", currentQuery);
                startActivity(intent);
                
            }
        });
    }


    private ArrayList<Categoria> loadData() {
        ArrayList<Categoria> cat = new ArrayList<Categoria>();

        apiService.getCategorias("Accept", completeToken).enqueue(new Callback<Categoria>() {
            @Override
            public void onResponse(@NotNull Call<Categoria> call, @NotNull Response<Categoria> response) {


                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("success")) {

                        int tam = response.body().getCategorias().size();

                        if (tam > 0) {
                            for (int i = 0; i < tam; i++) {
                                cat.add(response.body().getCategorias().get(i));
                            }
                        }

                    }


                }
                Log.e("statusResponseCategorie", response.body().getCategorias().get(0).getDescription());

            }

            @Override
            public void onFailure(@NotNull Call<Categoria> call, @NotNull Throwable t) {
                Toasty.warning(context, "Ocurrio un error: " + t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });


        return cat;

    }

    @Override
    public void onItemClick(View view, int position) {
        Categoria categoria = categorias.get(position);
        Intent intent = new Intent(getActivity(), ShowListProductsActivity.class);
        intent.putExtra("type_product", categoria.getId());
        intent.putExtra("nameType", categoria.getDescription());
        Objects.requireNonNull(getActivity()).startActivity(intent);
        getActivity().finish();
    }



    private void reloadData(){
        categorias = loadData();
        categoriasAdapter = new CategoriasAdapter(context, categorias);
        categoriasAdapter.setClickListener(this);
        recyclerView.setAdapter(categoriasAdapter);
        recyclerView.showShimmerAdapter();
    }
}
