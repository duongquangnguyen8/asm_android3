package com.example.myapplication.screens;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.models.Account;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAccount extends AppCompatActivity {

    private ImageView imgBack;
    private EditText edtName,edtAddress,edtDate,edtPhoneNumber;
    private HttpRequest httpRequest;
    private Button btnUpdateUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_update), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        httpRequest=new HttpRequest(ApiService.Url_post_account);
        getAccountId(HomeActivity.userId);
        btnUpdateUser.setOnClickListener(view -> {
            updateAccount(HomeActivity.userId);
            finish();
        });
        edtDate.setOnClickListener(view -> {
            showDatePickerDialog();
        });

    }
    private void updateAccount(String id){
        if(edtName.getText().toString().isEmpty()||edtAddress.getText().toString().isEmpty()||
                edtDate.getText().toString().isEmpty()||edtPhoneNumber.getText().toString().isEmpty()){

                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else if ((edtPhoneNumber.getText().toString().length() != 10 || edtPhoneNumber.getText().toString().charAt(0) != '0')) {
            Toast.makeText(this, "Số điện thoại phải bắt đầu bằng 0 và có 10 số", Toast.LENGTH_SHORT).show();
        }else{
            Account account=new Account();
            account.setFullName(edtName.getText().toString());
            account.setAddress(edtAddress.getText().toString());
            account.setBirth(convertStringToDate(edtDate.getText().toString()));
            account.setPhoneNumber(edtPhoneNumber.getText().toString());
            updateAccountInfor(id,account);
        }
    }
    private void showDatePickerDialog(){
        final Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog=new DatePickerDialog(this,(view,year1,month1,dayoffmonth)->{
            String date = dayoffmonth + "/" + (month1+1) + "/" + year1;
            edtDate.setText(date);
        },year,month,day);
        datePickerDialog.show();
    }
    private Date convertStringToDate(String dateString){
        try{
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            return simpleDateFormat.parse(dateString);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private void updateAccountInfor(String id, Account account){
        Call<ResponseData<Account>> call=httpRequest.getApiSevice().updateAccount(id,account);
        call.enqueue(new Callback<ResponseData<Account>>() {
            @Override
            public void onResponse(Call<ResponseData<Account>> call, Response<ResponseData<Account>> response) {
                if (response.isSuccessful()&& response.body()!=null){
                    ResponseData<Account> responseData=response.body();
                    if (responseData.getStatus().equals("200")){
                        Toast.makeText(UpdateAccount.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UpdateAccount.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(UpdateAccount.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Account>> call, Throwable t) {
                Toast.makeText(UpdateAccount.this, "Lỗi kết nối"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getAccountId(String id){
        Call<ResponseData<Account>> call=httpRequest.getApiSevice().getAccountById(id);
        call.enqueue(new Callback<ResponseData<Account>>() {
            @Override
            public void onResponse(Call<ResponseData<Account>> call, Response<ResponseData<Account>> response) {
                if (response.isSuccessful()&& response.body()!=null){
                    ResponseData<Account> responseData=response.body();
                    if (responseData.getStatus().equals("200")){
                        Account account=responseData.getData();
                        setAccountInfor(account);
                    }else{
                        Toast.makeText(UpdateAccount.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdateAccount.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Account>> call, Throwable t) {

            }
        });
    }
    private void setAccountInfor(Account account){
        if (account.getFullName().equals("")){
            edtDate.setText("");
        }
        else{
            edtName.setText(account.getFullName());
            edtAddress.setText(account.getAddress());
            Date date=new Date(account.getBirth().getTime());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            edtDate.setText(simpleDateFormat.format(date));
            edtPhoneNumber.setText(account.getPhoneNumber());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initUi(){
        imgBack=findViewById(R.id.imgBackUpdate);
        imgBack.setOnClickListener(view -> {
            finish();
        });
        edtName=findViewById(R.id.edtName_user);
        edtAddress=findViewById(R.id.edtAddress_user);
        edtDate=findViewById(R.id.edtDate_user);
        edtPhoneNumber=findViewById(R.id.edtPhoneNumber);
        btnUpdateUser=findViewById(R.id.btnUpdateUser);
    }
}