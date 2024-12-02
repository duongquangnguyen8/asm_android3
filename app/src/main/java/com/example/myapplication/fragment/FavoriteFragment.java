package com.example.myapplication.fragment;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FavoriteAdapter;
import com.example.myapplication.models.Favorite;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.screens.HomeActivity;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment {

    private List<Favorite> favoriteList=new ArrayList<>();
    private HttpRequest httpRequest;
    private FavoriteAdapter adapter;
    private RecyclerView recyclerView;
    public FavoriteFragment() {
        // Required empty public constructor
    }


    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_favorite,container,false);
        recyclerView=view.findViewById(R.id.rcv_favorite);
        httpRequest=new HttpRequest(ApiService.URL_FAVORITE);
        httpRequest.getApiSevice().getAllFavorite().enqueue(getFavorite);
        return view;
    }
    private void getDataProductById() {
        HttpRequest requestProduct = new HttpRequest(ApiService.URL_PRODUCT);
        int totalFavorites = favoriteList.size();
        int[] loadedCount = {0};

        for (Favorite favorite : favoriteList) {
            requestProduct.getApiSevice().getProductById(favorite.getProductId()).enqueue(new Callback<ResponseData<Product>>() {
                @Override
                public void onResponse(Call<ResponseData<Product>> call, Response<ResponseData<Product>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseData<Product> responseData = response.body();
                        if ("200".equals(responseData.getStatus())) {
                            Product product = responseData.getData();
                            favorite.setProduct(product);
                            Log.d("zzzz", favorite.getProduct().getProductName());
                        }
                    }

                    loadedCount[0]++;
                    if (loadedCount[0] == totalFavorites) {
                        filterFavoritesByUser();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData<Product>> call, Throwable t) {
                    Log.d("FavoriteFragment", "Error fetching product: " + t.getMessage());
                    // Tăng biến đếm ngay cả khi có lỗi
                    loadedCount[0]++;
                    if (loadedCount[0] == totalFavorites) {
                        filterFavoritesByUser();
                    }
                }
            });
        }
    }
    private void filterFavoritesByUser() {
        List<Favorite> userFavorites = new ArrayList<>();
        for (Favorite favorite : favoriteList) {
            if (favorite.getAccountId().equals(HomeActivity.userId)) {
                userFavorites.add(favorite);

            }
        } if (!userFavorites.isEmpty()) {
            favoriteList = userFavorites;
            setupAdapter();
        }else{
            favoriteList.clear();
            setupAdapter();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        httpRequest.getApiSevice().getAllFavorite().enqueue(getFavorite);
    }

    Callback<ResponseData<List<Favorite>>> getFavorite= new Callback<ResponseData<List<Favorite>>>() {

        @Override
        public void onResponse(Call<ResponseData<List<Favorite>>> call, Response<ResponseData<List<Favorite>>> response) {
            if (response.isSuccessful()&&response.body()!=null){
                ResponseData<List<Favorite>> responseData=response.body();
                if ("200".equals(responseData.getStatus())) {
                    favoriteList = responseData.getData();
                    Log.d("zzz", favoriteList.size() + "");
                    getDataProductById();
                } else {
                    Log.d("FavoriteFragment", "Error: " + responseData.getMessage());
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseData<List<Favorite>>> call, Throwable t) {
            Log.d("FavoriteFragment", "error"+t.getMessage());

        }
    };
    private void setupAdapter() {
        adapter = new FavoriteAdapter(favoriteList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}