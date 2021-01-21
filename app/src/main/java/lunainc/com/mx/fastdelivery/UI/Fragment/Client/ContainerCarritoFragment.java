package lunainc.com.mx.fastdelivery.UI.Fragment.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import lunainc.com.mx.fastdelivery.R;
import lunainc.com.mx.fastdelivery.Adapter.TabHomepageAdapter;

public class ContainerCarritoFragment extends Fragment {

    private View view;
    private ViewPager viewPager1;
    private TabLayout tabLayout1;
    private TabHomepageAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.container_carrito_fragment, container, false);


        initViews();

        return view;
    }

    public void initViews() {

        viewPager1 = view.findViewById(R.id.viewpager1);

        tabLayout1 = view.findViewById(R.id.tablayout1);

        tabLayout1.addTab(tabLayout1.newTab().setText("Carrito"));

        adapter = new TabHomepageAdapter(getActivity().getSupportFragmentManager(), tabLayout1.getTabCount());
        viewPager1.setAdapter(adapter);


        viewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout1));
        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager1.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

}
