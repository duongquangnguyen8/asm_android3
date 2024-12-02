package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MangerUserAdapter;
import com.example.myapplication.models.Account;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserManegerFragment extends Fragment {

    private MangerUserAdapter adapter;
    private RecyclerView recyclerView;
    private EditText edtSearch;
    private List<Account> accountList;
    private HttpRequest requestAccount;

    public UserManegerFragment() {
        // Required empty public constructor
    }


    public static UserManegerFragment newInstance(String param1, String param2) {
        UserManegerFragment fragment = new UserManegerFragment();
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
        View view= inflater.inflate(R.layout.fragment_user_maneger, container, false);
        edtSearch=view.findViewById(R.id.edtSearch_user_manger);
        recyclerView=view.findViewById(R.id.rcvManagerUser);

        //request
        requestAccount=new HttpRequest(ApiService.Url_post_account);
        getAccountbyIdrole("673c12eb6f7eb5f4f0df32ee");

        return view;
    }
    private void getAccountbyIdrole(String idrole){
        requestAccount.getApiSevice().getAccountByIdRole(idrole).enqueue(new Callback<ResponseData<List<Account>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<Account>>> call, Response<ResponseData<List<Account>>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<List<Account>> responseData=response.body();
                    if (responseData.getStatus().equals("200")&&responseData.getData()!=null){
                        accountList=responseData.getData();
                        adapter=new MangerUserAdapter(accountList,getContext());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else{
                        Log.e("TAG", "onResponse: "+responseData.getMessage());
                    }
                }else{
                    Log.e("TAG", "onResponse: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<Account>>> call, Throwable t) {
                Log.e("TAG", "onResponse: "+t.getMessage());

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getAccountbyIdrole("673c12eb6f7eb5f4f0df32ee");

    }
}