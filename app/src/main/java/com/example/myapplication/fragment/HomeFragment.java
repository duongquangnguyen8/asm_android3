package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CategoryAdapter;
import com.example.myapplication.adapter.ProductAdapter;
import com.example.myapplication.models.Category;
import com.example.myapplication.models.Favorite;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.screens.HomeActivity;
import com.example.myapplication.screens.ProductDetail;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewCategory;
    private List<Category> listCategory;
    private List<Product> productList;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerViewProduct;
    private HttpRequest httpRequest;
    private List<Favorite> favoriteList=new ArrayList<>();
    private HttpRequest requestFavorite;
    private EditText edtSearch;
    private Button btnSearch;
    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        recyclerViewCategory=view.findViewById(R.id.recycleView_category);
        edtSearch=view.findViewById(R.id.edtSearch);
        listCategory=new ArrayList<>();
        listCategory.add(new Category(R.drawable.all,"Tất cả"));
        listCategory.add(new Category(R.drawable.coffee,"Cà phê"));
        listCategory.add(new Category(R.drawable.icon_milktea2,"Trà sữa"));
        categoryAdapter=new CategoryAdapter(listCategory,getContext());
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewCategory.setAdapter(categoryAdapter);
        recyclerViewProduct=view.findViewById(R.id.recycleView_product);
        httpRequest=new HttpRequest(ApiService.URL_PRODUCT);
        httpRequest.getApiSevice().getAllProduct().enqueue(getProduct);
//        btnSearch.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               searchProducts(edtSearch.getText().toString());
//           }
//       });
        return view;
    }
    private void searchProducts(String query){
        httpRequest.getApiSevice().searchProducts(query).enqueue(new Callback<ResponseData<List<Product>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<Product>>> call, Response<ResponseData<List<Product>>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<List<Product>> responseData=response.body();
                    if (responseData.getStatus().equals("200")){
                        productList=responseData.getData();
                        productAdapter.updateproduct(productList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<Product>>> call, Throwable t) {

            }
        });
    }
    Callback<ResponseData<List<Product>>> getProduct=new Callback<ResponseData<List<Product>>>() {
        @Override
        public void onResponse(Call<ResponseData<List<Product>>> call, Response<ResponseData<List<Product>>> response) {
            if (response.isSuccessful()&&response.body()!=null){
                ResponseData<List<Product>> responseData=response.body();
                if (responseData.getStatus().equals("200")){
                    productList=response.body().getData();
                    productAdapter=new ProductAdapter(getContext(),productList);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
                    recyclerViewProduct.setLayoutManager(gridLayoutManager);
                    recyclerViewProduct.setAdapter(productAdapter);
                }else{
                    Toast.makeText(getContext(), "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getContext(), "Lỗi API", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseData<List<Product>>> call, Throwable t) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        onLoadData();
    }
    private void onLoadData(){
        requestFavorite=new HttpRequest(ApiService.URL_FAVORITE);
        requestFavorite.getApiSevice().getAllFavorite().enqueue(getFavorite);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==getActivity().RESULT_OK){
            if (data!=null&&data.hasExtra("action")){
                String action=data.getStringExtra("action");
                if ("delete".equals(action)||"add".equals(action)){
                    onLoadData();
                    productAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    Callback<ResponseData<List<Favorite>>> getFavorite= new Callback<ResponseData<List<Favorite>>>() {
        @Override
        public void onResponse(Call<ResponseData<List<Favorite>>> call, Response<ResponseData<List<Favorite>>> response) {
            if (response.isSuccessful()&&response.body()!=null){
                ResponseData<List<Favorite>> responseData=response.body();
                if (responseData.getStatus().equals("200")){
                    favoriteList=responseData.getData();
                    Toast.makeText(getContext(), ""+favoriteList.size(), Toast.LENGTH_SHORT).show();
                    if (productAdapter!=null){
                        productAdapter.updateFavorites(favoriteList);
                    }
                }else{
                    Toast.makeText(getContext(), ""+response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseData<List<Favorite>>> call, Throwable t) {
            Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}