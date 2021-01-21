package lunainc.com.mx.fastdelivery.UI.Fragment.Client;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lunainc.com.mx.fastdelivery.R;


import butterknife.ButterKnife;

public class HomeFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View  view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


}
