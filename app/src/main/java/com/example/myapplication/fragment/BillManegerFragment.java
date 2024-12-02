package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ManagerBillAdapter;
import com.example.myapplication.models.Bill;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillManegerFragment extends Fragment {

    private RecyclerView recyclerView;
    private ManagerBillAdapter adapter;
    private List<Bill> billList;
    private HttpRequest requestBill;
    public static BillManegerFragment newInstance(String param1, String param2) {
        BillManegerFragment fragment = new BillManegerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_bill_maneger, container, false);
        recyclerView=view.findViewById(R.id.rcvMangetBill);
        requestBill=new HttpRequest(ApiService.URL_Bill);
        requestBill.getApiSevice().getAllBill().enqueue(getAllBill);
        return view;
    }
    Callback<ResponseData<List<Bill>>> getAllBill=new Callback<ResponseData<List<Bill>>>() {
        @Override
        public void onResponse(Call<ResponseData<List<Bill>>> call, Response<ResponseData<List<Bill>>> response) {
            if (response.isSuccessful()&&response.body()!=null){
                ResponseData<List<Bill>> responseData=response.body();
                if (responseData.getStatus().equals("200")){
                    billList=responseData.getData();
                    adapter=new ManagerBillAdapter(billList,getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseData<List<Bill>>> call, Throwable t) {

        }
    };
}