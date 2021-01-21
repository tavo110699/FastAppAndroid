package lunainc.com.mx.fastdelivery.UI.Activity.Socio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import lunainc.com.mx.fastdelivery.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsPartnerActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_partner);
        ButterKnife.bind(this);
    }
}
