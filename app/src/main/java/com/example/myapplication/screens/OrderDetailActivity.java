package com.example.myapplication.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.models.Bill;
import com.example.myapplication.models.BillDetail;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView imgBack,imgProduct;
    private TextView tvTotalPrice,tvShippingName,tvShippingPhone,tvShippingAddress,tvNameProduct_Order_detail
            ,tvPriceProduct_cart_detail,tvQuantity_order_detail,tvTotalPriceAll,tvTotalPrice_detail;
    private Button btnOrder;
    private RelativeLayout containerAddress;
    private HttpRequest requestBill,requestBillDetail,requestCartDetail;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==RESULT_OK){
            String fullName=data.getStringExtra("fullName");
            String phoneNumber=data.getStringExtra("phoneNumber");
            String address=data.getStringExtra("address");
            tvShippingName.setText(fullName);
            tvShippingPhone.setText(phoneNumber);
            tvShippingAddress.setText(address);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        tvShippingName.setText("Bạn cần điền địa chỉ nhận hàng");
        tvShippingPhone.setText("");
        tvShippingAddress.setText("");

        imgBack.setOnClickListener(v -> finish());

        //request
        requestBill=new HttpRequest(ApiService.URL_Bill);
        requestBillDetail=new HttpRequest(ApiService.URL_BillDetail);
        requestCartDetail=new HttpRequest(ApiService.URL_CartDetail);

        Intent intent=getIntent();
        String quantity=intent.getStringExtra("quantity");
        String nameProduct=intent.getStringExtra("nameProduct");
        String priceProduct=intent.getStringExtra("priceProduct");
        String image=intent.getStringExtra("image");
        String idProduct=intent.getStringExtra("idProduct");
        String idCartDetail=intent.getStringExtra("idCartDetail");
        String idCart=intent.getStringExtra("idCart");

        if (nameProduct != null) {
            tvNameProduct_Order_detail.setText(nameProduct);
        } else {
            Log.e("OrderDetailActivity", "nameProduct is null");
        } if (priceProduct != null) {
            tvPriceProduct_cart_detail.setText(priceProduct + "đ");
        } if (quantity != null) {
            tvQuantity_order_detail.setText("x" + quantity);
        } if (image != null) {
            int resourceId = getResources().getIdentifier(image, "drawable", getPackageName());
            imgProduct.setImageResource(resourceId);
        }
        int totalPrice=Integer.parseInt(priceProduct)*Integer.parseInt(quantity);
        tvTotalPrice.setText(totalPrice+"đ");
        tvTotalPriceAll.setText(totalPrice+"đ");
        tvTotalPrice_detail.setText(totalPrice+"đ");
        containerAddress.setOnClickListener(v -> {
            Intent updateAddressIntent = new Intent(OrderDetailActivity.this, DeliveryAddressActivity.class);
            startActivityForResult(updateAddressIntent,1);
        });
        btnOrder.setOnClickListener(v -> {
            if (tvShippingName.getText().toString().equals("Bạn cần điền địa chỉ nhận hàng")) {
                Toast.makeText(OrderDetailActivity.this, "Bạn cần điền địa chỉ nhận hàng", Toast.LENGTH_SHORT).show();
            }else{
                Bill bill=new Bill(HomeActivity.userId,idCart,totalPrice,"APPROVAL",tvShippingName.getText().toString(),tvShippingPhone.getText().toString(),tvShippingAddress.getText().toString());
                addBill(bill,idProduct,idCartDetail,quantity,priceProduct);
                finish();
            }
        });
    }

    private void addBill(Bill bill,String idProduct,String idCartDetail,String quantity,String priceProduct){
        requestBill.getApiSevice().addBill(bill).enqueue(new Callback<ResponseData<Bill>>() {
            @Override
            public void onResponse(Call<ResponseData<Bill>> call, Response<ResponseData<Bill>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Bill> responseData=response.body();
                    if (responseData.getStatus().equals("200")) {
                        Bill bill = responseData.getData();
                        addBillDetail(new BillDetail(bill.getId(),idProduct,Integer.parseInt(quantity),Integer.parseInt(priceProduct)));
                        deleteCartDetail(idCartDetail);
                        Toast.makeText(OrderDetailActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(OrderDetailActivity.this, "loi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(OrderDetailActivity.this, "loi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Bill>> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "loi"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addBillDetail(BillDetail billDetail){
        requestBillDetail.getApiSevice().addBillDetail(billDetail).enqueue(new Callback<ResponseData<BillDetail>>() {
            @Override
            public void onResponse(Call<ResponseData<BillDetail>> call, Response<ResponseData<BillDetail>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<BillDetail> responseData=response.body();
                    if (responseData.getStatus().equals("200")) {
                        BillDetail billDetail = responseData.getData();
                    }else{
                        Toast.makeText(OrderDetailActivity.this, "loi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(OrderDetailActivity.this, "loi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<BillDetail>> call, Throwable t) {

            }
        });
    }
    private void deleteCartDetail(String idCartDetail){
        requestCartDetail.getApiSevice().deleteCartDetailCall(idCartDetail).enqueue(new Callback<ResponseData<Void>>() {
            @Override
            public void onResponse(Call<ResponseData<Void>> call, Response<ResponseData<Void>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Void> responseData=response.body();
                    if (responseData.getStatus().equals("200")) {
                        Log.d("cartdetail","success");
                    }else{
                        Log.d("cartdetail","loi");
                    }
                }else{
                    Log.d("cartdetail","loi");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Void>> call, Throwable t) {
                Log.d("cartdetail","loi"+t.getMessage());

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initUi(){
        imgBack=findViewById(R.id.imgBack_Order_detail);
        tvTotalPrice=findViewById(R.id.tvTotalPrice_order_detail);
        tvShippingName=findViewById(R.id.tvShippingName);
        tvShippingPhone=findViewById(R.id.tvShippingPhone);
        tvShippingAddress=findViewById(R.id.tvShippingAddress);
        btnOrder=findViewById(R.id.btnOrderAll_detail);
        containerAddress=findViewById(R.id.containerAddress_detail);
        tvNameProduct_Order_detail=findViewById(R.id.tvNameProduct_Order_detail);
        tvPriceProduct_cart_detail=findViewById(R.id.tvPriceProduct_cart_detail);
        tvQuantity_order_detail=findViewById(R.id.tvQuantity_order_detail);
        imgProduct=findViewById(R.id.imgProduct_order_detail);
        tvTotalPriceAll=findViewById(R.id.tvTotalPriceAll_detail);
        tvTotalPrice_detail=findViewById(R.id.tvTotalPrice_detail);

    }
}