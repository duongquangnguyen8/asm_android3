package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ProductMangerAdapter;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductMangerFragment extends Fragment {


    private RecyclerView recyclerView;
    private FloatingActionButton fabAddProduct;
    private HttpRequest requestProduct;
    private ProductMangerAdapter adapter;
    private List<Product> productList;
    public static ProductMangerFragment newInstance(String param1, String param2) {
        ProductMangerFragment fragment = new ProductMangerFragment();
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
        View view= inflater.inflate(R.layout.fragment_product_manger, container, false);
        recyclerView=view.findViewById(R.id.rcvMangerProduct);
        fabAddProduct=view.findViewById(R.id.fab_add_product);

        //request
        requestProduct=new HttpRequest(ApiService.URL_PRODUCT);
        requestProduct.getApiSevice().getAllProduct().enqueue(getProduct);

        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddProductDialog();
            }
        });
        return view;
    }
    private void showAddProductDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater inflater=getLayoutInflater();
        View dialogView=inflater.inflate(R.layout.custom_dialog_product_manager,null);
        builder.setView(dialogView);

        EditText edtProductName=dialogView.findViewById(R.id.et_product_name);
        EditText edtProductPrice=dialogView.findViewById(R.id.et_product_price);
        EditText edtProductDescription=dialogView.findViewById(R.id.et_product_description);
        EditText edtProductImageUrl=dialogView.findViewById(R.id.et_product_image_url);
        Button btnAddProduct=dialogView.findViewById(R.id.btn_add_product);
        Spinner spnProductCategory=dialogView.findViewById(R.id.spinner_product_category);
        List<String> categories=new ArrayList<>();
        categories.add("Trà sữa");
        categories.add("Cà phê");

        ArrayAdapter<String> adapter1=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,categories);
        spnProductCategory.setAdapter(adapter1);
        AlertDialog dialog=builder.create();
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName=edtProductName.getText().toString();
                String strPrice=edtProductPrice.getText().toString();
                String strDescription=edtProductDescription.getText().toString();
                String strImageUrl=edtProductImageUrl.getText().toString();
                String strCategory=spnProductCategory.getSelectedItem().toString();
                if (strName.isEmpty()||strPrice.isEmpty()||strDescription.isEmpty()||strImageUrl.isEmpty()||strCategory.isEmpty()){
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if (spnProductCategory.getSelectedItemPosition()==0){
                        Product product=new Product(strName,strDescription,Integer.parseInt(strPrice),strImageUrl,"673e0dab817e7fcd27bdf2f3");
                        setFabAddProduct(product);
                    }else{
                        Product product=new Product(strName,strDescription,Integer.parseInt(strPrice),strImageUrl,"673e0d92817e7fcd27bdf2f1");
                        setFabAddProduct(product);
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    private void setFabAddProduct(Product product){
        requestProduct.getApiSevice().addProduct(product).enqueue(new Callback<ResponseData<Product>>() {
            @Override
            public void onResponse(Call<ResponseData<Product>> call, Response<ResponseData<Product>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Product> responseData=response.body();
                    if (responseData.getStatus().equals("200")&&responseData.getData()!=null){
                        Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        productList.add(product);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    Callback<ResponseData<List<Product>>> getProduct=new Callback<ResponseData<List<Product>>>() {
        @Override
        public void onResponse(Call<ResponseData<List<Product>>> call, Response<ResponseData<List<Product>>> response) {
            if (response.isSuccessful()&&response.body()!=null){
                ResponseData<List<Product>> responseData=response.body();
                if(responseData.getStatus().equals("200")&&responseData.getData()!=null){
                    productList=responseData.getData();
                    adapter=new ProductMangerAdapter(productList,getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseData<List<Product>>> call, Throwable t) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        requestProduct=new HttpRequest(ApiService.URL_PRODUCT);
        requestProduct.getApiSevice().getAllProduct().enqueue(getProduct);
    }
}