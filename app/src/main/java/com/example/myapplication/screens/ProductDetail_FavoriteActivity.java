package com.example.myapplication.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.models.Cart;
import com.example.myapplication.models.CartDetail;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetail_FavoriteActivity extends AppCompatActivity {

    private TextView tvNameProduct,tvPriceProduct,tvDescription,tvQuantity;
    private ImageView imgBack,imgProduct;
    private Button btnAddToCart,btnAddQuantity,btnRemoveQuantity;
    private HttpRequest requestCart,requestCartDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail_favorite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        imgBack.setOnClickListener(view -> {
            finish();
        });
        Intent intent=getIntent();
        String nameProduct=intent.getStringExtra("name_product_favorite");
        double priceProduct=intent.getDoubleExtra("price_product_favorite",0.0);
        String descriptionProduct=intent.getStringExtra("description_product_favorite");
        String imageProduct=intent.getStringExtra("image_product_favorite");
        String productId=intent.getStringExtra("id_product_favorite");
        tvNameProduct.setText(nameProduct);
        tvPriceProduct.setText(String.valueOf(priceProduct));
        tvDescription.setText(descriptionProduct);
        int resIdImage=getResources().getIdentifier(imageProduct,"drawable",getPackageName());
        imgProduct.setImageResource(resIdImage);
        requestCart=new HttpRequest(ApiService.URL_CART);
        requestCartDetail=new HttpRequest(ApiService.URL_CartDetail);
        btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity=Integer.parseInt(tvQuantity.getText().toString());
                currentQuantity++;
                tvQuantity.setText(String.valueOf(currentQuantity));
            }
        });
        btnRemoveQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity=Integer.parseInt(tvQuantity.getText().toString());
                if(currentQuantity>1){
                    currentQuantity--;
                    tvQuantity.setText(String.valueOf(currentQuantity));
                }
            }
        });

        btnAddToCart.setOnClickListener(view -> {
            int currentQuantity=Integer.parseInt(tvQuantity.getText().toString());
            addCart(HomeActivity.userId,productId,priceProduct,currentQuantity);
        });
    }
    private void addCart(String idUser,String idProduct,Number price,Number quantity){
        requestCart=new HttpRequest(ApiService.URL_CART);
        Call<ResponseData<Cart>> call=requestCart.getApiSevice().getCartByidAccount(idUser);
        call.enqueue(new Callback<ResponseData<Cart>>() {
            @Override
            public void onResponse(Call<ResponseData<Cart>> call, Response<ResponseData<Cart>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Cart> responseData=response.body();
                    if(responseData.getStatus().equals(("200"))&&responseData.getData()!=null){
                        String idCart=responseData.getData().getId();
                        checkAndUpdateCartDetail(idCart,idProduct,price,quantity);
                    }else {
                        addCartAndCartDetail(idUser,idProduct,price,quantity);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Cart>> call, Throwable t) {

            }
        });
    }
    private void addCartAndCartDetail(String idUser,String idProduct,Number price,Number quantity){
        Cart cart=new Cart(idUser);
        Call<ResponseData<Cart>> call=requestCart.getApiSevice().addCart(cart);
        call.enqueue(new Callback<ResponseData<Cart>>() {
            @Override
            public void onResponse(Call<ResponseData<Cart>> call, Response<ResponseData<Cart>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Cart> responseData=response.body();
                    if ((responseData.getStatus().equals("200"))){
                        Log.d("cart","success");
                        String idCart=responseData.getData().getId();
                        addCartDetail(idCart,idProduct,quantity,price);

                    }
                    else{
                        Toast.makeText(ProductDetail_FavoriteActivity.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d("cart","Lỗi kết nối"+response.code()+response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Cart>> call, Throwable t) {
                Log.d("cart",t.getMessage().toString());
            }
        });
    }
    private void addCartDetail(String idCart,String productId,Number quantity,Number price){
        requestCartDetail=new HttpRequest(ApiService.URL_CartDetail);
        CartDetail cartDetail=new CartDetail(idCart,productId,quantity,price);
        Call<ResponseData<CartDetail>> call=requestCartDetail.getApiSevice().addCartDetail(cartDetail);
        call.enqueue(new Callback<ResponseData<CartDetail>>() {
            @Override
            public void onResponse(Call<ResponseData<CartDetail>> call, Response<ResponseData<CartDetail>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<CartDetail> responseData=response.body();
                    if (responseData.getStatus().equals("200")){
                        Log.d("cartDetail","success");
                    }else {
                        Toast.makeText(ProductDetail_FavoriteActivity.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ProductDetail_FavoriteActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<CartDetail>> call, Throwable t) {
                Toast.makeText(ProductDetail_FavoriteActivity.this, "Yêu cầu thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }private void checkAndUpdateCartDetail(String idCart,String productId,Number price,Number quantity){
        requestCartDetail.getApiSevice().getCartDetailByidCartAndidProduct(idCart,productId).enqueue(new Callback<ResponseData<CartDetail>>() {
            @Override
            public void onResponse(Call<ResponseData<CartDetail>> call, Response<ResponseData<CartDetail>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<CartDetail> responseData=response.body();
                    if(responseData.getStatus().equals("200")){
                        CartDetail cartDetail=responseData.getData();
                        if (cartDetail!=null){
                            Number currentQuantity=cartDetail.getQuantity();
                            cartDetail.setQuantity(currentQuantity.intValue()+quantity.intValue());
                            upDateCartDetail(cartDetail.getId(),cartDetail);
                        }else{
                            addCartDetail(idCart,productId,1,price);
                        }
                    }else{
                        addCartDetail(idCart,productId,1,price);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData<CartDetail>> call, Throwable t) {

            }
        });
    }
    private void upDateCartDetail(String id,CartDetail cartDetail){
        requestCartDetail.getApiSevice().updateCartDetail(id,cartDetail).enqueue(new Callback<ResponseData<CartDetail>>() {
            @Override
            public void onResponse(Call<ResponseData<CartDetail>> call, Response<ResponseData<CartDetail>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<CartDetail> responseData=response.body();
                    if(responseData.getStatus().equals("200")){
                        Log.d("cartDetail","success");
                    }else{
                        Toast.makeText(ProductDetail_FavoriteActivity.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ProductDetail_FavoriteActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<CartDetail>> call, Throwable t) {
                Toast.makeText(ProductDetail_FavoriteActivity.this, "Yêu cầu thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void initUi(){
        tvNameProduct=findViewById(R.id.tvNameProduct_detail_favorite);
        tvPriceProduct=findViewById(R.id.tvPriceProduct_detail_favorite);
        tvDescription=findViewById(R.id.tvDescription_product_detail_favorite);
        imgProduct=findViewById(R.id.imgProduct_detail_favorite);
        imgBack=findViewById(R.id.imgBack_product_detail_favorite);
        btnAddToCart=findViewById(R.id.btnAddtoCart_favorite);
        btnAddQuantity=findViewById(R.id.btnAddQuantity_product_detail);
        btnRemoveQuantity=findViewById(R.id.btnRemoveQuantity_product_detail);
        tvQuantity=findViewById(R.id.tvQuantity_product_detail);

    }
}