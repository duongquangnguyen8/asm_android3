package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
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
import retrofit2.http.DELETE;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> listProducts;
    private List<Favorite> listFavorite=new ArrayList<>();
    private HttpRequest httpRequest;
    public ProductAdapter(Context context, List<Product> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
        fetchFavorite(); //hàm cập nhật danh sách favorite
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_products,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product=listProducts.get(position);
        boolean isFavorite=false;
        String favoriteId=null;
        holder.tvNameProduct.setText(product.getProductName());
        holder.tvPriceProduct.setText(product.getPrice().toString()+"đ");
        int resIdImage=holder.itemView.getContext().getResources().getIdentifier(product.getImage(),
                "drawable",holder.itemView.getContext().getPackageName());
        //getResources() là lấy tài nguyên getIdentifier này là tìm id tài nguyên
        // ví dụ icon_delete thì getIdentifier trả về R.drawable.icon_delete
        holder.imgProduct.setImageResource(resIdImage);
        for (Favorite favorite: listFavorite){
            if (favorite.getProductId().equals(product.getId())&&
                    favorite.getAccountId().equals(HomeActivity.userId)){
                isFavorite=true;
                favoriteId=favorite.getId();
                break;
            }
        }
        if (isFavorite){
            holder.imgFavoriteProduct.setImageResource(R.drawable.icon_favorite_2);
        }else {
            holder.imgFavoriteProduct.setImageResource(R.drawable.icon_favorite_1);
        }
        boolean finalIsFavorite = isFavorite;
        String finalFroriteId=favoriteId;

        holder.imgFavoriteProduct.setOnClickListener(new View.OnClickListener() {
            boolean currentFavorite= finalIsFavorite;
            @Override
            public void onClick(View view) {
                currentFavorite=!currentFavorite;
                if (currentFavorite){
                    holder.imgFavoriteProduct.setImageResource(R.drawable.icon_favorite_2);
                    Favorite favorite=new Favorite(HomeActivity.userId,product.getId(),true,null,null);
                    postFavorite(favorite);
                }else {
                    holder.imgFavoriteProduct.setImageResource(R.drawable.icon_favorite_1);
                    deleteFavorite(finalFroriteId);
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            boolean isFavoriteIntent=finalIsFavorite;
            String idFavor=finalFroriteId;
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetail.class);
                intent.putExtra("name_product",product.getProductName());
                intent.putExtra("price_product",product.getPrice().doubleValue());
                intent.putExtra("description_product",product.getDescription());
                intent.putExtra("image_product",product.getImage());
                intent.putExtra("id_product",product.getId());
                intent.putExtra("isFavorite",isFavoriteIntent);
                if (idFavor!=null){
                 intent.putExtra("id_favorite",idFavor);
                }
                if(context instanceof Activity){
                    ((Activity)context).startActivityForResult(intent,1);
                }
                Toast.makeText(context, ""+listFavorite.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return listProducts!=null?listProducts.size():0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct,imgFavoriteProduct;
        TextView tvNameProduct,tvPriceProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct_item);
            imgFavoriteProduct=itemView.findViewById(R.id.imgFavoriteProduct_item);
            tvNameProduct=itemView.findViewById(R.id.tvNameProduct_item);
            tvPriceProduct=itemView.findViewById(R.id.tvPriceProduct_item);
        }
    }
    private void deleteFavorite(String id){
        httpRequest=new HttpRequest(ApiService.URL_FAVORITE);
        httpRequest.getApiSevice().deleteFavoriteCall(id).enqueue(new Callback<ResponseData<Void>>() {

            @Override
            public void onResponse(Call<ResponseData<Void>> call, Response<ResponseData<Void>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Void> responseData=response.body();
                    if (responseData.getStatus().equals("200")){
                        Log.d("delete","success");
                        fetchFavorite(); // cập nhật lại list
                    }else {
                        Toast.makeText(context, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Void>> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void postFavorite(Favorite favorite){
        httpRequest=new HttpRequest(ApiService.URL_FAVORITE);
        httpRequest.getApiSevice().addFavorite(favorite).enqueue(new Callback<ResponseData<Favorite>>() {
            @Override
            public void onResponse(Call<ResponseData<Favorite>> call, Response<ResponseData<Favorite>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Favorite> responseData=response.body();
                    if (response.body().getStatus().equals("200")){
                        Log.d("post","success");
                        fetchFavorite();
                    }else{
                        Toast.makeText(context, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Favorite>> call, Throwable t) {

            }
        });

    }
    public void fetchFavorite(){
        HttpRequest httpRequest=new HttpRequest(ApiService.URL_FAVORITE);
        httpRequest.getApiSevice().getAllFavorite().enqueue(getFavorite);
    }
    Callback<ResponseData<List<Favorite>>> getFavorite= new Callback<ResponseData<List<Favorite>>>() {

        @Override
        public void onResponse(Call<ResponseData<List<Favorite>>> call, Response<ResponseData<List<Favorite>>> response) {
            if (response.isSuccessful()&&response.body()!=null){
                ResponseData<List<Favorite>> responseData=response.body();
                if (responseData.getStatus().equals("200")){
                    listFavorite=responseData.getData();
                    Log.d("abc",listFavorite.size()+"");
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(context, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseData<List<Favorite>>> call, Throwable t) {
            Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
        }
    };
    public void updateFavorites(List<Favorite> newFavorites) {
        this.listFavorite = newFavorites; // Cập nhật danh sách yêu thích
        notifyDataSetChanged(); // Làm mới giao diện
    }
    public void updateproduct(List<Product> newProduct) {
        this.listProducts = newProduct; // Cập nhật danh sách yêu thích
        notifyDataSetChanged(); // Làm mới giao diện
    }
}
