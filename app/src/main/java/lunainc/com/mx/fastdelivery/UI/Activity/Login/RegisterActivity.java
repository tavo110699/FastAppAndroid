package lunainc.com.mx.fastdelivery.UI.Activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import lunainc.com.mx.fastdelivery.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.com.mx.fastdelivery.Model.User;
import lunainc.com.mx.fastdelivery.Utils.Constants;
import lunainc.com.mx.fastdelivery.customfonts.EditText_Roboto_Regular;
import lunainc.com.mx.fastdelivery.customfonts.MyTextView_Roboto_Regular;

public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.nameClient)
    EditText_Roboto_Regular nameClient;

    @BindView(R.id.lastNamePClient)
    EditText_Roboto_Regular lastNamePClient;

    @BindView(R.id.lastNameMClient)
    EditText_Roboto_Regular lastNameMClient;

    @BindView(R.id.phoneClient)
    EditText_Roboto_Regular phoneClient;

    @BindView(R.id.emailClient)
    EditText_Roboto_Regular emailClient;

    @BindView(R.id.passwordClient)
    EditText_Roboto_Regular passwordClient;

    @BindView(R.id.btnRegister)
    Button btnRegister;



    @BindView(R.id.alreadyAccount)
    Button alreadyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configViews();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    private void configViews(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    protected void onStart() {
        super.onStart();
        events();
    }


    private void events(){

        btnRegister.setOnClickListener(v -> {

            String name = nameClient.getText().toString();
            String lastNameP = lastNamePClient.getText().toString();
            String lastNameM = lastNameMClient.getText().toString();
            String email = emailClient.getText().toString();
            String phone = phoneClient.getText().toString();
            String password = phoneClient.getText().toString();


            if ( (validateSize(nameClient, name)) &&
                    (validateSize(lastNamePClient, lastNameP)) &&
                    (validateSize(lastNameMClient, lastNameM)) &&
                    (validateSize(emailClient, email)) &&
                    (validateSizePhone(phoneClient, phone)) &&
                    validateSizePass(passwordClient, password)){

                User user = new User();
                user.setName(name);
                user.setLast_name_p(lastNameP);
                user.setLast_name_m(lastNameM);
                user.setEmail(email);
                user.setPhone(phone);
                user.setPassword(password);

                Intent intent = new Intent(this, VerifyPhoneActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                finish();

            }

        });

        alreadyAccount.setOnClickListener(v -> new Constants().goTo(this, LoginActivity.class));

    }


    private boolean validateSizePhone(EditText editText, String value){

        if (value.trim().length() <= 12 || value.trim().length() == 10){
            return true;
        }else {
            editText.setError("Este campo es requerido");
            return false;
        }



    }

    private boolean validateSizePass(EditText editText, String value){

        if (value.trim().length() >= 8){
            return true;
        }else {
            editText.setError("Este campo es requerido");
            return false;
        }



    }


    private boolean validateSize(EditText editText, String value){

        if (value.trim().length() >= 4){
            return true;
        }else {
            editText.setError("Este campo es requerido");
            return false;
        }



    }

}
