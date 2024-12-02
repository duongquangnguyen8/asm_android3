package com.example.myapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Favorite;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.screens.ProductDetail;
import com.example.myapplication.screens.ProductDetail_FavoriteActivity;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<Favorite> favoriteList;
    private List<Product> productList=new ArrayList<>();
    private Context context;
    private HttpRequest httpRequest;

    public FavoriteAdapter(List<Favorite> favoriteList, Context context) {
        this.favoriteList = favoriteList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_favorite,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite favorite=favoriteList.get(position);
        Product product=favorite.getProduct();
        if (product != null) {
            holder.tvNameProduct.setText(product.getProductName());
            holder.tvPriceProduct.setText(product.getPrice() + "đ");

            int resIdImage = holder.itemView.getContext().getResources().getIdentifier(
                    product.getImage(), "drawable", holder.itemView.getContext().getPackageName());
            holder.imgProduct.setImageResource(resIdImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ProductDetail_FavoriteActivity.class);
                intent.putExtra("name_product_favorite",product.getProductName());
                intent.putExtra("price_product_favorite",product.getPrice().doubleValue());
                intent.putExtra("description_product_favorite",product.getDescription());
                intent.putExtra("image_product_favorite",product.getImage());
                intent.putExtra("id_product_favorite",product.getId());
                context.startActivity(intent);

            }
        });
        holder.imgDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialogDelete(favorite.getId(),position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return favoriteList!=null?favoriteList.size():0;
    }

    private void dialogDelete(String id,int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi danh sách yêu thích?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteFavorite(id);
                favoriteList.remove(position);
                notifyItemRemoved(position);
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
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
                    }else{
                        Toast.makeText(context, "Lỗi API: " + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Void>> call, Throwable t) {
                Log.d("delete","Lỗi"+t.getMessage());
            }
        });
    }
    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameProduct,tvPriceProduct;
        ImageView imgProduct,imgDeleteProduct;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct=itemView.findViewById(R.id.tvNameProduct_item_favorite);
            tvPriceProduct=itemView.findViewById(R.id.tvPriceProduct_item_favorite);
            imgProduct=itemView.findViewById(R.id.imgProduct_item_favorite);
            imgDeleteProduct=itemView.findViewById(R.id.imgDeleteProduct_item_favorite);
        }
    }
//
}
