package com.example.myapplication.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputLayout;

public class DeliveryAddressActivity extends AppCompatActivity {

    private ImageView imgBack;
    private EditText edtFullName, edtPhoneNumber, edtAddress;
    private TextInputLayout layoutFullName, layoutPhoneNumber, layoutAddress;
    private Button btnSaveAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.update), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        imgBack.setOnClickListener(view -> finish());
        btnSaveAddress.setOnClickListener(view -> {
            if (validateInputs()){
                saveAddress();
            }
        });
    }
    private boolean validateInputs() {
        boolean isValid = true; // Kiểm tra họ và tên
         String fullName = edtFullName.getText().toString().trim();
         if (TextUtils.isEmpty(fullName)) {
             layoutFullName.setError("Vui lòng nhập họ và tên");
             isValid = false;
         } else {
             layoutFullName.setError(null);
         } // Kiểm tra số điện thoại
         String phoneNumber = edtPhoneNumber.getText().toString().trim();
         if (TextUtils.isEmpty(phoneNumber)) {
             layoutPhoneNumber.setError("Vui lòng nhập số điện thoại");
             isValid = false;
         } else if (!phoneNumber.matches("^0\\d{9}$"))
         { layoutPhoneNumber.setError("Số điện thoại không hợp lệ, phải bắt đầu bằng số 0 và có 10 số");
             isValid = false;
         } else { layoutPhoneNumber.setError(null); }
         // Kiểm tra địa chỉ
        String address = edtAddress.getText().toString().trim();
         if (TextUtils.isEmpty(address)) {
             layoutAddress.setError("Vui lòng nhập địa chỉ nhận hàng");
             isValid = false;
         } else {
             layoutAddress.setError(null);
         }
         return isValid;
    }

    private void saveAddress() {
        String fullName = edtFullName.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra("fullName", fullName);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("address", address);
        setResult(RESULT_OK,intent);
        Toast.makeText(this, "Địa chỉ đã được lưu thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void initUi(){
        edtFullName = findViewById(R.id.edtFullName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtAddress = findViewById(R.id.edtAddress);
        layoutFullName = findViewById(R.id.layoutFullName);
        layoutPhoneNumber = findViewById(R.id.layoutPhoneNumber);
        layoutAddress = findViewById(R.id.layoutAddress);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
        imgBack =findViewById(R.id.imgBackUpdateAddress);

    }
}