package com.example.myapplication.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.models.Account;
import com.example.myapplication.models.Bill;
import com.example.myapplication.models.BillDetail;
import com.example.myapplication.models.CartDetail;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    private ImageView imgBack;
    private ArrayList<Integer> quantities,prices;
    private ArrayList<String> listIdProduct,listCartDetail;
    private String idCart;
    private TextView tvTotalPrice,tvShippingName,tvShippingPhone,tvShippingAddress;
    private Button btnOrder;
    private RecyclerView recyclerView;
    private RelativeLayout containerAddress;
    private OrderAdapter adapter;
    private List<CartDetail> cartDetailList;
    private HttpRequest requestBill,requestBillDetail,requestAccount,requestCartDetail;
    static final String statusBillApproval="APPROVAL"; //chờ phê duyệt

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
        setContentView(R.layout.activity_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        imgBack.setOnClickListener((view -> finish()));
        Intent intent=getIntent();
        String totalPrice=intent.getStringExtra("totalPrice");
        //list lieen quan
        //request
        requestCartDetail=new HttpRequest(ApiService.URL_CartDetail);
        requestBill=new HttpRequest(ApiService.URL_Bill);
        requestBillDetail=new HttpRequest(ApiService.URL_BillDetail);
        requestAccount=new HttpRequest(ApiService.Url_post_account);
        requestAccount.getApiSevice().getAccountById(HomeActivity.userId).enqueue(getAccount);

        quantities = getIntent().getIntegerArrayListExtra("quantities");
        prices = getIntent().getIntegerArrayListExtra("prices");
        idCart = getIntent().getStringExtra("idcart");
        listIdProduct = getIntent().getStringArrayListExtra("listIdProduct");
        listCartDetail=getIntent().getStringArrayListExtra("cartDetailId");

        tvTotalPrice.setText(totalPrice);
        cartDetailList=new ArrayList<>();
        tvShippingName.setText("Bạn cần điền địa chỉ nhận hàng");
        tvShippingPhone.setText("");
        tvShippingAddress.setText("");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        for (int i=0;i<listIdProduct.size();i++){
               cartDetailList.add(new CartDetail(idCart,listIdProduct.get(i),quantities.get(i),prices.get(i)));
        }
        adapter=new OrderAdapter(cartDetailList,this,quantities);
        recyclerView.setAdapter(adapter);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvShippingName.getText().equals("Bạn cần điền địa chỉ nhận hàng")){
                    Toast.makeText(OrderActivity.this, "Bạn cần điền địa chỉ nhận hàng", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Bill bill = new Bill(HomeActivity.userId, idCart, Integer.parseInt(totalPrice), statusBillApproval,tvShippingName.getText().toString(), tvShippingPhone.getText().toString(), tvShippingAddress.getText().toString());
                    addBill(bill);
                    finish();
                }


            }
        });
        containerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateAddressIntent = new Intent(OrderActivity.this, DeliveryAddressActivity.class);
                startActivityForResult(updateAddressIntent,1);
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
                        Toast.makeText(OrderActivity.this, "loi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(OrderActivity.this, "loi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<BillDetail>> call, Throwable t) {

            }
        });
    }
    private void addBill(Bill bill){
        requestBill.getApiSevice().addBill(bill).enqueue(new Callback<ResponseData<Bill>>() {
            @Override
            public void onResponse(Call<ResponseData<Bill>> call, Response<ResponseData<Bill>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Bill> responseData=response.body();
                    if (responseData.getStatus().equals("200")) {
                        Bill bill = responseData.getData();
                        for (int i=0;i<listIdProduct.size();i++){
                            addBillDetail(new BillDetail(bill.getId(),listIdProduct.get(i),quantities.get(i),prices.get(i)));
                            deleteCartDetail(listCartDetail.get(i));
                        }
                        Toast.makeText(OrderActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(OrderActivity.this, "loi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(OrderActivity.this, "loi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Bill>> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "loi"+t.getMessage(), Toast.LENGTH_SHORT).show();
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
    Callback<ResponseData<Account>> getAccount=new Callback<ResponseData<Account>>() {
        @Override
        public void onResponse(Call<ResponseData<Account>> call, Response<ResponseData<Account>> response) {
            if (response.isSuccessful()&&response.body()!=null){
                ResponseData<Account> responseData=response.body();
                if (responseData.getStatus().equals("200")) {
//                    Account account = responseData.getData();
//                    if (!account.getFullName().equals("")){
//                        tvShippingName.setText(account.getFullName());
//                        tvShippingPhone.setText(account.getPhoneNumber());
//                        tvShippingAddress.setText(account.getAddress());
//                    }else{
//                        tvShippingName.setText("Bạn cần điền địa chỉ nhận hàng");
//                        tvShippingPhone.setText("");
//                        tvShippingAddress.setText("");
//                    }


                }else{
                    Toast.makeText(OrderActivity.this, "loi", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(OrderActivity.this, "loi", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseData<Account>> call, Throwable t) {

        }
    };
    private void initUi(){
        imgBack=findViewById(R.id.imgBack_Order_cart);
        tvTotalPrice=findViewById(R.id.tvTotalPrice_orderAll);
        btnOrder=findViewById(R.id.btnOrderAll);
        recyclerView=findViewById(R.id.rcvProduct_OrderAll);
        tvShippingName=findViewById(R.id.tvShippingName);
        tvShippingPhone=findViewById(R.id.tvShippingPhone);
        tvShippingAddress=findViewById(R.id.tvShippingAddress);
        containerAddress=findViewById(R.id.containerAddress);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}