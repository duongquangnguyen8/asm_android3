package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.BillDetail;
import com.example.myapplication.models.CartDetail;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.screens.OrderDetailActivity;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>  {
    private List<CartDetail> cartDetailList;
    private Context context;
    private ArrayList<Integer> quantities;
    private ArrayList<String> listIdProduct;
    private HttpRequest requestProduct;

    public OrderAdapter(List<CartDetail> cartDetailList, Context context, ArrayList<Integer> quantities) {
        this.cartDetailList = cartDetailList;
        this.context = context;
        this.quantities=quantities;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        CartDetail cartDetail=cartDetailList.get(position);
        getProductById(cartDetail.getProductId(),holder,position);
    }

    @Override
    public int getItemCount() {
        return cartDetailList!=null?cartDetailList.size():0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameProduct_Order, tvPriceProduct_cart, tvQuantity_order,tvTotalPrice,tvTotalPriceAll;
        ImageView imgProduct_order;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct_Order = itemView.findViewById(R.id.tvNameProduct_Order);
            tvPriceProduct_cart = itemView.findViewById(R.id.tvPriceProduct_cart);
            tvQuantity_order=itemView.findViewById(R.id.tvQuantity_order);
            tvTotalPrice=itemView.findViewById(R.id.tvTotalPrice);
            tvTotalPriceAll=itemView.findViewById(R.id.tvTotalPriceAll);
            imgProduct_order = itemView.findViewById(R.id.imgProduct_order);

        }
    }
    private void getProductById(String idProduct,OrderViewHolder holder,int postion){
        requestProduct=new HttpRequest(ApiService.URL_PRODUCT);
        requestProduct.getApiSevice().getProductById(idProduct).enqueue(new Callback<ResponseData<Product>>() {
            @Override
            public void onResponse(Call<ResponseData<Product>> call, Response<ResponseData<Product>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Product> responseData=response.body();
                    if (responseData.getStatus().equals("200")) {

                        Product product = responseData.getData();
                        holder.tvNameProduct_Order.setText(product.getProductName());
                        holder.tvPriceProduct_cart.setText(product.getPrice()+"đ");
                        holder.tvQuantity_order.setText("x"+quantities.get(postion)+"");
                        int totalPrice=product.getPrice().intValue()*quantities.get(postion);
                        holder.tvTotalPrice.setText(totalPrice+"đ");
                        holder.tvTotalPriceAll.setText(totalPrice+"đ");
                        int resId=context.getResources().getIdentifier(product.getImage(),"drawable",context.getPackageName());
                        holder.imgProduct_order.setImageResource(resId);
                    }else{
                        Log.d("loi","loi");
                    }
                }else{
                    Log.d("loi","loi");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Product>> call, Throwable t) {
                Log.d("loi","loi"+t.getMessage());
            }
        });
    }
}
