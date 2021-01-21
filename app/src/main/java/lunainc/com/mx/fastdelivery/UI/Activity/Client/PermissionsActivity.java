package lunainc.com.mx.fastdelivery.UI.Activity.Client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import lunainc.com.mx.fastdelivery.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.UI.Activity.Client.Directions.GetCurrentLocationActivity;

public class PermissionsActivity extends AppCompatActivity {


    @BindView(R.id.btn_grant)
    Button btnGrant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        ButterKnife.bind(this);

        if(ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(PermissionsActivity.this, GetCurrentLocationActivity.class));
            finish();
            return;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        events();
    }

    private void events() {

        btnGrant.setOnClickListener(v -> Dexter.withActivity(PermissionsActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startActivity(new Intent(PermissionsActivity.this, GetCurrentLocationActivity.class));
                        finish();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
                            builder.setTitle("Permiso denegado")
                                    .setMessage("El permiso para acceder a la ubicación del dispositivo fue denegado permanentemente. debe ir a la configuración para permitir el permiso.")
                                    .setNegativeButton("Cancelar", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(PermissionsActivity.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check());

    }
}
