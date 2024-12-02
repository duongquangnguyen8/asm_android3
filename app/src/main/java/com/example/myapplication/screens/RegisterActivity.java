package com.example.myapplication.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegister;
    private TextView tvLogin;
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private TextInputLayout emailInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    private Toolbar toolbar;
    private HttpRequest httpRequest;
    private List<Account> listAccount=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Thiết lập lắng nghe sự kiện cho ViewCompat
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();

        // Thiết lập sự kiện click cho btnRegister và tvLogin
        btnRegister.setOnClickListener(v -> validateInput());
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Thiết lập Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Đăng kí");
        }
        httpRequest=new HttpRequest(ApiService.Url_post_account);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoadData();
    }
    private void onLoadData(){
        httpRequest.getApiSevice().getAllAccount().enqueue(getAccount);
    }
    //callback là gọi về hiểu đơn giản là xử lí việc api nó trveefveef response
    Callback<ResponseData<List<Account>>> getAccount= new Callback<ResponseData<List<Account>>>() {
        @Override
        public void onResponse(Call<ResponseData<List<Account>>> call, Response<ResponseData<List<Account>>> response) {
            Toast.makeText(RegisterActivity.this, "haha", Toast.LENGTH_SHORT).show();
            if(response.isSuccessful()&&response.body()!=null){
                ResponseData<List<Account>> responseData=response.body();

                if("200".equals(responseData.getStatus())){
                    listAccount=response.body().getData();
                    Toast.makeText(RegisterActivity.this, ""+listAccount.size(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("API_RESPONSE", "Status khác 200: " + responseData.getStatus());
                    Toast.makeText(RegisterActivity.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseData<List<Account>>> call, Throwable t) {
            Log.d("loi",t.getMessage().toString());
        }
    };


    private void initUi() {
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        toolbar = findViewById(R.id.toolbar_register);
        edtEmail = findViewById(R.id.edtEmail_Register);
        edtPassword = findViewById(R.id.edtPassword_Register);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword_Register);

        emailInputLayout = (TextInputLayout) edtEmail.getParent().getParent();
        passwordInputLayout = (TextInputLayout) edtPassword.getParent().getParent();
        confirmPasswordInputLayout = (TextInputLayout) edtConfirmPassword.getParent().getParent();
    }

    private void validateInput() {
        boolean isValid = true;

        String strEmail = edtEmail.getText().toString().trim();
        String strPass = edtPassword.getText().toString().trim();
        String strConfirmPass = edtConfirmPassword.getText().toString().trim();

        // Kiểm tra email
        if (TextUtils.isEmpty(strEmail)) {
            emailInputLayout.setError("Vui lòng nhập email");
            isValid = false;
        } else if (!isValidEmail(strEmail)) {
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

        // Kiểm tra xác nhận mật khẩu
        if (TextUtils.isEmpty(strConfirmPass)) {
            confirmPasswordInputLayout.setError("Vui lòng nhập lại mật khẩu");
            isValid = false;
        } else if (!strPass.equals(strConfirmPass)) {
            confirmPasswordInputLayout.setError("Mật khẩu không khớp");
            isValid = false;
        } else {
            confirmPasswordInputLayout.setError(null);
        }

        if (isValid) {
            Role role =new Role(0,null,null);
            Account account=new Account(strEmail,strPass,"","","",null,"673c12eb6f7eb5f4f0df32ee",null,null);
            boolean checkEmail=false;
            for (Account item: listAccount){
                if (item.getEmail().equals(strEmail)){
                    checkEmail=true;
                    emailInputLayout.setError("Email đã tồn tại");
                    return;
                }
            }
            if (!checkEmail){
                registerAccount(account);
                Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void registerAccount(Account account){

        Call<ResponseData<Account>> call=httpRequest.getApiSevice().registerAccount(account);
        call.enqueue(new Callback<ResponseData<Account>>() {
            @Override
            public void onResponse(Call<ResponseData<Account>> call, Response<ResponseData<Account>> response) {
                Log.d("API_CALL", "Response: " +response);
                if (response.isSuccessful() && response.body() != null) {
                    ResponseData<Account> responseData = response.body();
                    if ("200".equals(responseData.getStatus())) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Toast.makeText(RegisterActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace(); }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Account>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Yêu cầu thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("zzz",t.getMessage().toString());

            }
        });
    }
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (email == null || email.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
