package com.example.myapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.CartFragment;
import com.example.myapplication.models.Cart;
import com.example.myapplication.models.CartDetail;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.screens.OrderDetailActivity;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartDetail> cartList;
    private Context context;
    private CartDetail cartDetail;
    private CartFragment cartFragment;
    private HttpRequest requestCartDetail;

    public CartAdapter(List<CartDetail> cartList, Context context,CartFragment cartFragment) {
        this.cartList = cartList;
        this.context = context;
        this.cartFragment=cartFragment; //tham chiếu nhé
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(View.inflate(context, R.layout.layout_item_cart,null));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        cartDetail=cartList.get(position);
        holder.tvQuantity.setText(String.valueOf(cartDetail.getQuantity()));
        getProductById(cartDetail.getProductId(),holder.tvName,holder.tvPrice,holder.imgProduct,holder);
        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity=Integer.parseInt(holder.tvQuantity.getText().toString());
                currentQuantity++;
                holder.tvQuantity.setText(String.valueOf(currentQuantity));
                cartList.get(position).setQuantity(currentQuantity);
                cartFragment.updateTotalPrice();
            }
        });
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity=Integer.parseInt(holder.tvQuantity.getText().toString());
                if(currentQuantity>1){
                    currentQuantity--;
                    holder.tvQuantity.setText(String.valueOf(currentQuantity));
                    cartList.get(position).setQuantity(currentQuantity);
                    cartFragment.updateTotalPrice();
                }
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không?");
                builder.setPositiveButton("Dồng ý",(dialogInterface, i) -> {
                    deleteCartDetail(cartDetail.getId());
                    cartList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,cartList.size());
                    cartFragment.updateTotalPrice();
                });
                builder.setNegativeButton("Hủy",(dialogInterface, i) -> {
                    dialogInterface.cancel();
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList!=null?cartList.size():0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct,imgAdd,imgRemove,imgDelete;
        TextView tvName,tvPrice,tvQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct=itemView.findViewById(R.id.imgProduct_cart);
            imgAdd=itemView.findViewById(R.id.imgAddQuantity_cart);
            imgRemove=itemView.findViewById(R.id.imgRemoveQuantity_cart);
            imgDelete=itemView.findViewById(R.id.imgDelete_cart);
            tvName=itemView.findViewById(R.id.tvNameProduct_cart);
            tvPrice=itemView.findViewById(R.id.tvPriceProduct_cart);
            tvQuantity=itemView.findViewById(R.id.tvQuantity_cart);
        }
    }
    private void deleteCartDetail(String idCartDetail){
        requestCartDetail=new HttpRequest(ApiService.URL_CartDetail);
        requestCartDetail.getApiSevice().deleteCartDetailCall(idCartDetail).enqueue(new Callback<ResponseData<Void>>() {
            @Override
            public void onResponse(Call<ResponseData<Void>> call, Response<ResponseData<Void>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Void> responseData=response.body();
                    if (responseData.getStatus().equals("200")) {
                        Log.d("cartdetail","success");
                    }else{
                        Log.d("cartdetail","loi");
                    }
                }else{
                    Log.d("cartdetail","loi");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Void>> call, Throwable t) {
                Log.d("cartdetail","loi"+t.getMessage());

            }
        });
    }
    private void getProductById(String idProduct,TextView tvName,TextView tvPrice,ImageView imgProduct,CartViewHolder holder){
        HttpRequest requestproduct=new HttpRequest(ApiService.URL_PRODUCT);
        requestproduct.getApiSevice().getProductById(idProduct).enqueue(new Callback<ResponseData<Product>>() {
            @Override
            public void onResponse(Call<ResponseData<Product>> call, Response<ResponseData<Product>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Product> responseData=response.body();
                    if (responseData.getStatus().equals("200")){
                        Product product=responseData.getData();
                        tvName.setText(product.getProductName());
                        tvPrice.setText(String.valueOf(product.getPrice())+"đ");
                        int resid=context.getResources().getIdentifier(product.getImage(),"drawable",context.getPackageName());
                        imgProduct.setImageResource(resid);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, OrderDetailActivity.class);
                                intent.putExtra("nameProduct", product.getProductName());
                                intent.putExtra("priceProduct", String.valueOf(product.getPrice()));
                                intent.putExtra("quantity", holder.tvQuantity.getText().toString());
                                intent.putExtra("idProduct", cartDetail.getProductId());
                                intent.putExtra("image", product.getImage());
                                intent.putExtra("idCartDetail", cartDetail.getId());
                                intent.putExtra("idCart",cartDetail.getCartId());
                                context.startActivity(intent);
                            }
                        });
                    }else {
                        Log.d("cart","Lỗi API"+responseData.getMessage());
                    }
                }else{
                    Log.d("cart","Lỗi kết nối");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Product>> call, Throwable t) {
                Log.d("cart","Lỗi kết nối"+t.getMessage());

            }
        });
    }
}
