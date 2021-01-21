package lunainc.com.mx.fastdelivery.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import lunainc.com.mx.fastdelivery.UI.Fragment.Client.CarritoFragment;
import lunainc.com.mx.fastdelivery.UI.Fragment.Client.HistorialOrdersFragment;
import lunainc.com.mx.fastdelivery.UI.Fragment.Client.HistorialTaxiFragment;

public class TabHomepageAdapter extends FragmentStatePagerAdapter {
        private int numoftabs;

    public TabHomepageAdapter(FragmentManager fm, int  mnumoftabs ) {
            super(fm);
            this.numoftabs = mnumoftabs;
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HistorialOrdersFragment();
                case 1:
                    return new HistorialTaxiFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return numoftabs;
        }
}
