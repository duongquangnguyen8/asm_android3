package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.StatusBillAdapter;
import com.example.myapplication.models.Bill;
import com.example.myapplication.models.BillDetail;
import com.example.myapplication.models.BillDetailsWrapperList;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.screens.HomeActivity;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PheDuytFragment extends Fragment {

    private RecyclerView recyclerView;
    private StatusBillAdapter adapter;
    private HttpRequest requestBill, requestBillDetail;
    private List<BillDetailsWrapperList> allBillDetailsWithExtras = new ArrayList<>();
    private int pendingRequests = 0; // Đếm số request đang thực hiện

    public PheDuytFragment() {
        // Required empty public constructor
    }

    public static PheDuytFragment newInstance() {
        return new PheDuytFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cancel_ordered, container, false);
        recyclerView = view.findViewById(R.id.rcvOrerStatus_PheDuyet);

        // Request
        requestBill = new HttpRequest(ApiService.URL_Bill);
        requestBillDetail = new HttpRequest(ApiService.URL_BillDetail);

        // Initialize adapter and set it to recyclerView
        adapter = new StatusBillAdapter(allBillDetailsWithExtras, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        fetchData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    private void fetchData() {
        // Xóa dữ liệu cũ và thông báo adapter
        allBillDetailsWithExtras.clear();
        adapter.notifyDataSetChanged();

        // Đặt lại số request đang chạy
        pendingRequests = 0;

        // Lấy dữ liệu mới
        getBillByIdUser(HomeActivity.userId);
    }

    private void getBillByIdUser(String idUser) {
        requestBill.getApiSevice().getBillByIdAccount(idUser).enqueue(new Callback<ResponseData<List<Bill>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<Bill>>> call, Response<ResponseData<List<Bill>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseData<List<Bill>> responseData = response.body();
                    if (responseData.getStatus().equals("200") && responseData.getData() != null) {
                        List<Bill> listBill = responseData.getData();
                        for (Bill bill : listBill) {
                                getBillDetailByIdBill(bill.getId(), bill.getTotalPrice(), bill.getStatusBill());
                        }
                    } else {
                        Toast.makeText(getContext(), "Lỗi: Không có dữ liệu hóa đơn", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi: Không thể lấy dữ liệu hóa đơn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<Bill>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBillDetailByIdBill(String idBill, Number totalPrice, String statusBill) {
        pendingRequests++;
        requestBillDetail.getApiSevice().getBillDetailByIdBill(idBill).enqueue(new Callback<ResponseData<List<BillDetail>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<BillDetail>>> call, Response<ResponseData<List<BillDetail>>> response) {
                pendingRequests--;
                if (response.isSuccessful() && response.body() != null) {
                    ResponseData<List<BillDetail>> responseData = response.body();
                    if (responseData.getStatus().equals("200") && responseData.getData() != null) {
                        List<BillDetail> listBillDetail = responseData.getData();
                        for (BillDetail billDetail : listBillDetail) {
                            BillDetailsWrapperList newItem = new BillDetailsWrapperList(billDetail, totalPrice, statusBill);
                            if (!isDuplicate(newItem)) {
                                allBillDetailsWithExtras.add(newItem);
                            }
                        }
                    }
                }
                checkAndUpdateAdapter();
            }

            @Override
            public void onFailure(Call<ResponseData<List<BillDetail>>> call, Throwable t) {
                pendingRequests--;
                checkAndUpdateAdapter();
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAndUpdateAdapter() {
        if (pendingRequests == 0) {
            adapter.notifyDataSetChanged(); // Cập nhật adapter khi tất cả request đã hoàn tất
        }
    }

    private boolean isDuplicate(BillDetailsWrapperList newItem) {
        for (BillDetailsWrapperList item : allBillDetailsWithExtras) {
            if (item.getBillDetails().getId().equals(newItem.getBillDetails().getId())) {
                return true;
            }
        }
        return false;
    }
}
