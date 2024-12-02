package com.example.myapplication.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.Account;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.models.Role;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Button btnSignIn;
    private TextView tvCreateAccount;
    private HttpRequest httpRequest;
    private List<Account> listAccount;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private CheckBox cbRemember;
    private SharedPreferences sharedPreferences,userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        sharedPreferences=getSharedPreferences("LOGIN_REMEMBER",MODE_PRIVATE);
        userPreferences=getSharedPreferences("USER_ID",MODE_PRIVATE);
        loadPreferences();
        btnSignIn.setOnClickListener(v -> {
            validateInput();
        });
        tvCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    private void validateInput() {
        boolean isValid = true;

        String strEmail = edtEmail.getText().toString().trim();
        String strPass = edtPassword.getText().toString().trim();

        // Kiểm tra email
        if (TextUtils.isEmpty(strEmail)) {
            emailInputLayout.setError("Vui lòng nhập email");
            isValid = false;
        } else if (!RegisterActivity.isValidEmail(strEmail)) {
            emailInputLayout.setError("Email không hợp lệ");
            isValid = false;
        } else {
            emailInputLayout.setError(null);
        }

        // Kiểm tra mật khẩu
        if (TextUtils.isEmpty(strPass)) {
            passwordInputLayout.setError("Vui lòng nhập mật khẩu");
            isValid = false;
        } else {
            passwordInputLayout.setError(null);
        }

        if (isValid) {
            boolean check=false;
            for (Account item: listAccount){
                if (item.getEmail().equals(strEmail)&&item.getPass().equals(strPass)){
                    if (cbRemember.isChecked()){
                        savePreferences();
                    }else {
                        clearPreferences();
                    }

                    if ("67440159e0819fbb68559a4b".equals(item.getRoleId())) {
                        // Chuyển hướng đến HomeActivityAdmin nếu là admin
                        Intent intent1 = new Intent(LoginActivity.this, HomeActivityAdmin.class);
                        startActivity(intent1);
                    } else {
                        // Chuyển hướng đến HomeActivity nếu không phải là admin
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        SharedPreferences.Editor editor=userPreferences.edit();
                        editor.putString("id",item.getId());
                        editor.apply();
                    }
                    return;
                }else{
                    check=true;
                }
            }
            if (check){
                Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        onLoadData();
    }
    private void onLoadData(){
        httpRequest.getApiSevice().getAllAccount().enqueue(getAccount);
    }
    Callback<ResponseData<List<Account>>> getAccount= new Callback<ResponseData<List<Account>>>() {
        @Override
        public void onResponse(Call<ResponseData<List<Account>>> call, Response<ResponseData<List<Account>>> response) {
            if(response.isSuccessful()&&response.body()!=null){
                ResponseData<List<Account>> responseData=response.body();
                if("200".equals(responseData.getStatus())){
                    listAccount=response.body().getData();
                }
                else {
                    Log.e("API_RESPONSE", "Status khác 200: " + responseData.getStatus());
                    Toast.makeText(LoginActivity.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseData<List<Account>>> call, Throwable t) {
            Log.d("loi",t.getMessage().toString());
        }
    };
    private void savePreferences(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("remember",cbRemember.isChecked());
        editor.putString("email",edtEmail.getText().toString());
        editor.putString("password",edtPassword.getText().toString());
        editor.apply();
    }
    private void clearPreferences(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    private void loadPreferences(){
        if (sharedPreferences.getBoolean("remember",false)){
            edtEmail.setText(sharedPreferences.getString("email",""));
            edtPassword.setText(sharedPreferences.getString("password", ""));
            cbRemember.setChecked(true);
        }
    }
    private void initUi(){
        btnSignIn=findViewById(R.id.btnSignIn_Login);
        tvCreateAccount=findViewById(R.id.tvCreateAccount);
        httpRequest=new HttpRequest(ApiService.Url_post_account);
        edtEmail=findViewById(R.id.edtEmail_Login);
        edtPassword=findViewById(R.id.edtPassword_Login);
        emailInputLayout = (TextInputLayout) edtEmail.getParent().getParent();
        passwordInputLayout = (TextInputLayout) edtPassword.getParent().getParent();
        cbRemember=findViewById(R.id.cbRemember);
    }

}