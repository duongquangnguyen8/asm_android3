package com.example.myapplication.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MangerBillDetailAdapter;
import com.example.myapplication.adapter.OrderAdapter;
import com.example.myapplication.models.Bill;
import com.example.myapplication.models.BillDetail;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillDetailManagerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvShippingName,tvShippingPhone,tvShippingAddress,tvTongTien;
    private ImageView imgback;
    private HttpRequest requestBillDetail,requestProduct,requestBill;
    private Spinner spnstatusBillManger;
    private String billId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_detail_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        requestBill=new HttpRequest(ApiService.URL_Bill);
        recyclerView=findViewById(R.id.rcvBillDetail_manager);
        tvShippingName=findViewById(R.id.tvShippingName_manager);
        tvShippingPhone=findViewById(R.id.tvShippingPhone_manager);
        tvShippingAddress=findViewById(R.id.tvShippingAddress);
        tvTongTien=findViewById(R.id.tvTongTien);
        spnstatusBillManger=findViewById(R.id.spnstatusBillManger);

        List<String> statusList=List.of("Chờ phê duyệt","Đã hủy","Đang giao","Đã giao");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,statusList);
        spnstatusBillManger.setAdapter(arrayAdapter);
        String status=getIntent().getStringExtra("status");
        if (status.equals("APPROVAL")){
            spnstatusBillManger.setSelection(0);
        }else if (status.equals("CANCELED")){
            spnstatusBillManger.setSelection(1);
        } else if (status.equals("DELIVERING")) {
            spnstatusBillManger.setSelection(2);
        }else if (status.equals("PENDING")) {
            spnstatusBillManger.setSelection(3);
        }


        imgback=findViewById(R.id.imgBackBillDetail_manager);

        requestBillDetail=new HttpRequest(ApiService.URL_BillDetail);
        imgback.setOnClickListener((view -> finish()));
        Intent intent=getIntent();
        tvTongTien.setText(intent.getStringExtra("total"));

        tvShippingName.setText(intent.getStringExtra("shippingName"));
        tvShippingPhone.setText(intent.getStringExtra("shippingPhone"));
        tvShippingAddress.setText(intent.getStringExtra("shippingAddress"));
        billId=intent.getStringExtra("billId");
        spnstatusBillManger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedStatus=statusList.get(i);
                Bill bill=new Bill(selectedStatus);
                //updateStatusBill(billId,bill);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getBillDetail(billId);

    }
    private void updateSpinnerSelection(String status) {
        switch (status) {
            case "APPROVAL": spnstatusBillManger.setSelection(0);
            break;
            case "CANCELED":
                spnstatusBillManger.setSelection(1);
                break;
                case "DELIVERING": spnstatusBillManger.setSelection(2);
                break;
                case "PENDING": spnstatusBillManger.setSelection(3);
                break;
                default: spnstatusBillManger.setSelection(0);
                break;
        }
    }
//    private void updateStatusBill(String idBill, Bill bill){
//        requestBill.getApiSevice().updateStatusBill(idBill,bill).enqueue(new Callback<ResponseData<Bill>>() {
//            @Override
//            public void onResponse(Call<ResponseData<Bill>> call, Response<ResponseData<Bill>> response) {
//                if (response.isSuccessful()&&response.body()!=null){
//                    Log.d("zzz","success update status");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData<Bill>> call, Throwable t) {
//
//            }
//        });
//    }
    @Override
    protected void onResume() {
        super.onResume();
        getBillDetail(billId);
    }

    private void getBillDetail(String billId){
        requestBillDetail.getApiSevice().getBillDetailByIdBill(billId).enqueue(new Callback<ResponseData<List<BillDetail>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<BillDetail>>> call, Response<ResponseData<List<BillDetail>>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    if (response.body().getStatus().equals("200")){
                        List<BillDetail> billDetailList=response.body().getData();
                        MangerBillDetailAdapter adapter=new MangerBillDetailAdapter(billDetailList,BillDetailManagerActivity.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(BillDetailManagerActivity.this));
                        recyclerView.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<BillDetail>>> call, Throwable t) {

            }
        });
    }

}