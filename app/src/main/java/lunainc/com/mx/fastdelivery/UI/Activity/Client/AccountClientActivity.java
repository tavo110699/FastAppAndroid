package lunainc.com.mx.fastdelivery.UI.Activity.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import lunainc.com.mx.fastdelivery.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import lunainc.com.mx.fastdelivery.Connect.APIService;
import lunainc.com.mx.fastdelivery.Connect.ApiUtils;
import lunainc.com.mx.fastdelivery.Model.ResponseDefault;
import lunainc.com.mx.fastdelivery.Model.User;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountClientActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.previewImage)
    CircleImageView previewImage;

    @BindView(R.id.btnActionImage)
    FloatingActionButton btnActionImage;

    @BindView(R.id.nameClient)
    TextInputEditText nameClient;

    @BindView(R.id.lastNamePClient)
    TextInputEditText lastNamePClient;

    @BindView(R.id.lastNameMClient)
    TextInputEditText lastNameMClient;

    @BindView(R.id.emailClient)
    TextInputEditText emailClient;

    @BindView(R.id.phoneClient)
    TextInputEditText phoneClient;

    @BindView(R.id.actionUpdate)
    Button actionUpdate;

    private StorageReference mImageStorage;
    private ProgressDialog mProgress;
    private static final int GALLERY_PICK = 1;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    private User user;
    private APIService apiService;
    private String token = "";
    private Context context;
    private String downloadULRImage = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_client);
        ButterKnife.bind(this);
        configVars();
        configViews();
        permisos(this);

    }

    private void configVars(){
        apiService = ApiUtils.getAPIService();
        context = this;
        token = new Constants().getToken(context);
        mImageStorage = FirebaseStorage.getInstance().getReference();

        user = (User) getIntent().getSerializableExtra("user");
    }

    private void configViews(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        setDataInView();
        events();
    }

    private void setDataInView() {
        if (user.getImage().equals("avatar.png")){
            Glide.with(context).load(new Constants().CURRENT_URL+"/avatars/"+user.getId()+"/avatar.png")
                    .into(previewImage);
        }else{
            Glide.with(context).load(user.getImage())
                    .into(previewImage);
        }

        nameClient.setText(user.getName());
        lastNamePClient.setText(user.getLast_name_p());
        lastNameMClient.setText(user.getLast_name_m());
        emailClient.setText(user.getEmail());
        phoneClient.setText(user.getPhone());

    }

    private void events(){

        btnActionImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, "ELEGIR IMAGEN"), GALLERY_PICK);
        });

        actionUpdate.setOnClickListener(v -> {

            String name = nameClient.getText().toString();
            String lastNameP = lastNamePClient.getText().toString();
            String lastNameM = lastNameMClient.getText().toString();

            if ( (validateSize(nameClient, name)) &&
                    (validateSize(lastNamePClient, lastNameP)) &&
                    (validateSize(lastNameMClient, lastNameM))){


                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("name", name)
                        .addFormDataPart("last_name_p", lastNameP)
                        .addFormDataPart("last_name_m", lastNameM)
                        .build();


                apiService.updateData("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {
                        if (response.isSuccessful()){

                            if (response.body().getStatus().equals("success")){
                                Toasty.success(context, response.body().getMessage()).show();
                            }else {
                                Toasty.error(context, response.body().getMessage()).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                        Log.e("Error", Objects.requireNonNull(t.getMessage()));
                    }
                });


            }


        });
    }


    private boolean validateSize(TextInputEditText textInputEditText, String value){

        if (value.trim().length() >= 4){
            return true;
        }else {
            textInputEditText.setError("Este campo es requerido");
            return false;
        }



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            new Constants().goTo(this, HomeActivity.class);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new Constants().goTo(this, HomeActivity.class);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(AccountClientActivity.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgress = new ProgressDialog(AccountClientActivity.this);
                mProgress.setTitle("Cargando imagen");
                mProgress.setMessage("Espere un momento por favor");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();


                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                //comprimir imagen de perfil
                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                Glide.with(context).load(thumb_bitmap)
                        .into(previewImage);


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                int leftLimit = 97; // letter 'a'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 10;
                Random random = new Random();
                StringBuilder buffer = new StringBuilder(targetStringLength);
                for (int i = 0; i < targetStringLength; i++) {
                    int randomLimitedInt = leftLimit + (int)
                            (random.nextFloat() * (rightLimit - leftLimit + 1));
                    buffer.append((char) randomLimitedInt);
                }
                String randomStringBuilder = buffer.toString();

                final StorageReference filepath = mImageStorage.child("profiles").child(randomStringBuilder + ".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(task -> {


                    if (task.isSuccessful()) {


                        filepath.getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadULRImage = uri.toString();
                            updatePhoto(downloadULRImage);
                            Glide.with(context).load(downloadULRImage)
                                    .into(previewImage);
                        });


                    } else {
                        mProgress.dismiss();
                        Toasty.warning(AccountClientActivity.this, "Opss ocurrio un error :(", Toasty.LENGTH_LONG).show();
                    }


                });


            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }



    }


    private void updatePhoto(String image){

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", image)
                .build();

        apiService.updatePhoto("Accept", token, body).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDefault> call, @NotNull Response<ResponseDefault> response) {

                if (response.isSuccessful()){

                    if (response.body().getStatus().equals("success")){
                        Toasty.success(context, response.body().getMessage()).show();

                    }else{
                        Toasty.error(context, response.body().getMessage()).show();

                    }
                    mProgress.dismiss();
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseDefault> call, @NotNull Throwable t) {
                Log.e("Error", Objects.requireNonNull(t.getMessage()));
            }
        });







    }



    public void permisos(Activity activity) {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }


        }


    }

}
