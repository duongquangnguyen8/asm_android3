package com.example.myapplication.screens;

import android.app.Activity;
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
import com.example.myapplication.models.Favorite;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.DELETE;

public class ProductDetail extends AppCompatActivity {

    private ImageView imgBack;
    private ImageView imgFavorite,imgProduct;
    private TextView tvNameProduct,tvPriceProduct,tvDescription,tvQuantity;
    private Button btnAddToCart,btnAddQuantity,btnRemoveQuantity;
    private List<Favorite> favoriteList=new ArrayList<>();
    private HttpRequest htpHttpRequest;
    private HttpRequest requestCart,requestCartDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgBack=findViewById(R.id.imgBack_product_detail);
        imgBack.setOnClickListener(view -> {
            finish();
        });
        initUi();
        Intent intent=getIntent();
        String nameProduct=intent.getStringExtra("name_product");
        String idFavorite=intent.getStringExtra("id_favorite");
        double priceProduct=intent.getDoubleExtra("price_product",0.0);
        String descriptionProduct=intent.getStringExtra("description_product");
        String imageProduct=intent.getStringExtra("image_product");
        boolean isFavorite=intent.getBooleanExtra("isFavorite",false);
        String idProduct=intent.getStringExtra("id_product");
        tvNameProduct.setText(nameProduct);
        tvPriceProduct.setText(String.valueOf(priceProduct+"đ"));
        tvDescription.setText(descriptionProduct);
        int resIdImage=getResources().getIdentifier(imageProduct,
                "drawable",getPackageName());
        imgProduct.setImageResource(resIdImage);

        requestCart=new HttpRequest(ApiService.URL_CART);
        requestCartDetail=new HttpRequest(ApiService.URL_CartDetail);

        int resIdFavorite=isFavorite?R.drawable.icon_favorite_2:R.drawable.icon_favorite_1;
        imgFavorite.setImageResource(resIdFavorite);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            boolean currentFavorite=isFavorite;
            @Override
            public void onClick(View view) {
                currentFavorite=!currentFavorite;
                int resIdFavorite=currentFavorite?R.drawable.icon_favorite_2:R.drawable.icon_favorite_1;
                imgFavorite.setImageResource(resIdFavorite);
                if(!currentFavorite){
                    if (idFavorite!=null){
                        clearFavorite(idFavorite);
                    }
                }else{
                    addFavorite(idProduct);
                }
            }
        });
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

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity=Integer.parseInt(tvQuantity.getText().toString());

                addCart(HomeActivity.userId,idProduct,priceProduct,currentQuantity);
            }
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
                       Toast.makeText(ProductDetail.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProductDetail.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ProductDetail.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<CartDetail>> call, Throwable t) {
                Toast.makeText(ProductDetail.this, "Yêu cầu thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void addFavorite(String idProduct){
        Favorite favorite=new Favorite(HomeActivity.userId,idProduct,true,null,null);
        Call<ResponseData<Favorite>> call=htpHttpRequest.getApiSevice().addFavorite(favorite);
        call.enqueue(new Callback<ResponseData<Favorite>>() {
            @Override
            public void onResponse(Call<ResponseData<Favorite>> call, Response<ResponseData<Favorite>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Favorite> responseData=response.body();
                    if ((responseData.getStatus().equals("200"))){
                        Log.d("favorite","success");

                        Intent resultIntent=new Intent();
                        resultIntent.putExtra("action","add");
                        setResult(Activity.RESULT_OK,resultIntent);

                        onLoadData();
                        Toast.makeText(ProductDetail.this, ""+favoriteList.size(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ProductDetail.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ProductDetail.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Favorite>> call, Throwable t) {

            }
        });

    }
    private void checkAndUpdateCartDetail(String idCart,String productId,Number price,Number quantity){
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
                        Toast.makeText(ProductDetail.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ProductDetail.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<CartDetail>> call, Throwable t) {
                Toast.makeText(ProductDetail.this, "Yêu cầu thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void clearFavorite(String idFavorite){
        if (idFavorite==null){
            Toast.makeText(this, "Không thể xoá", Toast.LENGTH_SHORT).show();
        }
       Call<ResponseData<Void>> call=htpHttpRequest.getApiSevice().deleteFavoriteCall(idFavorite);
       call.enqueue(new Callback<ResponseData<Void>>() {
           @Override
           public void onResponse(Call<ResponseData<Void>> call, Response<ResponseData<Void>> response) {
               if (response.isSuccessful()&&response.body()!=null){
                   ResponseData<Void> responseData=response.body();
                   if (responseData.getStatus().equals("200")){
                       Log.d("favorite","success");
                       Intent resultIntent=new Intent();
                       resultIntent.putExtra("action","delete");
                       setResult(Activity.RESULT_OK,resultIntent);
                       onLoadData();
                       Toast.makeText(ProductDetail.this, ""+favoriteList.size(), Toast.LENGTH_SHORT).show();
                   }else {
                       Toast.makeText(ProductDetail.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }else{
                   Toast.makeText(ProductDetail.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<ResponseData<Void>> call, Throwable t) {
               Log.d("favorite",t.getMessage().toString());

           }

       });
    }
    @Override
    protected void onResume() {
        super.onResume();
        onLoadData();
    }

    private void onLoadData(){
        htpHttpRequest=new HttpRequest(ApiService.URL_FAVORITE);
        htpHttpRequest.getApiSevice().getAllFavorite().enqueue(getFavorite);
    }
    Callback<ResponseData<List<Favorite>>> getFavorite=new Callback<ResponseData<List<Favorite>>>() {
        @Override
        public void onResponse(Call<ResponseData<List<Favorite>>> call, Response<ResponseData<List<Favorite>>> response) {
            if (response.isSuccessful()&&response.body()!=null){
                ResponseData<List<Favorite>> responseData=response.body();
                if (responseData.getStatus().equals("200")){
                    favoriteList=responseData.getData();

                }
                else {
                    Toast.makeText(ProductDetail.this, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(ProductDetail.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseData<List<Favorite>>> call, Throwable t) {
            Log.d("favorite",t.getMessage().toString());
        }
    };


    private void initUi(){
        imgBack=findViewById(R.id.imgBack_product_detail);
        imgFavorite=findViewById(R.id.imgFavorite_product_detail);
        imgProduct=findViewById(R.id.imgProduct_detail);
        tvNameProduct=findViewById(R.id.tvNameProduct_detail);
        tvPriceProduct=findViewById(R.id.tvPriceProduct_detail);
        tvDescription=findViewById(R.id.tvDescription_product_detail);
        btnAddToCart=findViewById(R.id.btnAddtoCart);
        btnAddQuantity=findViewById(R.id.btnAddQuantity_product_detail);
        btnRemoveQuantity=findViewById(R.id.btnRemoveQuantity_product_detail);
        tvQuantity=findViewById(R.id.tvQuantity_product_detail);
    }
}