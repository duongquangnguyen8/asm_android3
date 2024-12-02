package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Account;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.screens.ChangePassActivity;
import com.example.myapplication.screens.HomeActivity;
import com.example.myapplication.screens.LoginActivity;
import com.example.myapplication.screens.UpdateAccount;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment {

    private TextView tvChangePass,tvNameUser,tAddressUser,tvDateUser,tvPhoneNumber,tvUpdateAccount;
    private Button btnLogout;
    private List<Account> accountList=new ArrayList<>();
    private HttpRequest httpRequest;
    public UserFragment() {
        // Required empty public constructor
    }


    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        View view= inflater.inflate(R.layout.fragment_user, container, false);

        httpRequest=new HttpRequest(ApiService.Url_post_account);
        httpRequest.getApiSevice().getAllAccount().enqueue(getUser);

        tvNameUser=view.findViewById(R.id.tvNameUser);
        tAddressUser=view.findViewById(R.id.tAddressUser);
        tvDateUser=view.findViewById(R.id.tvDateUser);
        tvUpdateAccount=view.findViewById(R.id.tvUpdateAccount);
        btnLogout=view.findViewById(R.id.btnLogOut);
        tvPhoneNumber=view.findViewById(R.id.tvPhoneNumber);
        tvChangePass=view.findViewById(R.id.tvChangePass);
        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangePassActivity.class));
            }
        });
        tvUpdateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UpdateAccount.class));
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences= getActivity().getSharedPreferences("USER_ID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Đặt cờ để xóa lịch sử hoạt động và nó khởi tạo các activity mới
                startActivity(intent);

            }
        });
        return view;
    }
    private void initUi(){
        Log.d("check",accountList.size()+"");
        boolean check=false;
        Account accountCurrent=new Account();
        for (Account account: accountList){
            if (account.getId().equals(HomeActivity.userId)){
                if (account.getFullName().equals("")){
                    check=true;
                    break;
                }else{
                    accountCurrent=account;
                    break;
                }
            }
        }
        if (check){
            tvNameUser.setText("Họ tên: cập nhật họ tên");
            tAddressUser.setText("Địa chỉ: cập nhật địa chỉ");
            tvDateUser.setText("Ngày sinh: cập nhật ngày sinh");
            tvPhoneNumber.setText("Số điện thoại: cập nhật SĐT");
        }else{
            tvNameUser.setText("Họ tên: "+accountCurrent.getFullName());
            tAddressUser.setText("Địa chỉ: "+accountCurrent.getAddress());
            Date date=new Date(accountCurrent.getBirth().getTime());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            tvDateUser.setText("Ngày sinh: "+simpleDateFormat.format(date));
            tvPhoneNumber.setText("Số điện thoại: "+accountCurrent.getPhoneNumber());
        }
    }
    Callback<ResponseData<List<Account>>> getUser=new Callback<ResponseData<List<Account>>>() {
        @Override
        public void onResponse(Call<ResponseData<List<Account>>> call, Response<ResponseData<List<Account>>> response) {
            if (response.isSuccessful()&&response.body()!=null){
                ResponseData<List<Account>> responseData=response.body();
                if (responseData.getStatus().equals("200")){
                    accountList=responseData.getData();
                    Log.d("user",accountList.get(0).getEmail());
                    initUi();
                }else{
                    Log.d("user",responseData.getMessage());
                }
            }else {
                Log.d("user","error");
            }
        }

        @Override
        public void onFailure(Call<ResponseData<List<Account>>> call, Throwable t) {
            Log.d("user",t.getMessage());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        httpRequest.getApiSevice().getAllAccount().enqueue(getUser);
    }
}