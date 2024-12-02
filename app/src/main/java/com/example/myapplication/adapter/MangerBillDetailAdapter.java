package com.example.myapplication.adapter;

import android.content.Context;
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
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangerBillDetailAdapter extends RecyclerView.Adapter<MangerBillDetailAdapter.MangerBillDetailViewHolder> {

    private List<BillDetail> billDetailsList;
    private Context context;
    private HttpRequest requestProduct;
    public MangerBillDetailAdapter(List<BillDetail> billDetailsList, Context context) {
        this.billDetailsList = billDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MangerBillDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MangerBillDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MangerBillDetailViewHolder holder, int position) {
        BillDetail billDetail=billDetailsList.get(position);
        holder.tvPriceProduct.setText(billDetail.getPrice()+"đ");
        holder.tvQuantity.setText("x"+billDetail.getQuantity());
        int totalPrice=billDetail.getPrice().intValue()*billDetail.getQuantity().intValue();
        holder.tvTotalPrice.setText(totalPrice+"đ");
        holder.tvTotalPriceAll.setText(totalPrice+"đ");
        getProductById(billDetail.getProductId(),holder,position);
    }

    @Override
    public int getItemCount() {
        return billDetailsList!=null?billDetailsList.size():0;
    }

    public class MangerBillDetailViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduc;

        TextView tvNameProduct,tvPriceProduct,tvQuantity,tvTotalPrice,tvTotalPriceAll;
        public MangerBillDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduc=itemView.findViewById(R.id.imgProduct_order);
            tvNameProduct=itemView.findViewById(R.id.tvNameProduct_Order);
            tvPriceProduct=itemView.findViewById(R.id.tvPriceProduct_cart);
            tvQuantity=itemView.findViewById(R.id.tvQuantity_order);
            tvTotalPrice=itemView.findViewById(R.id.tvTotalPrice);
            tvTotalPriceAll=itemView.findViewById(R.id.tvTotalPriceAll);
        }
    }
    private void getProductById(String idProduct, MangerBillDetailViewHolder holder, int postion){
        requestProduct=new HttpRequest(ApiService.URL_PRODUCT);
        requestProduct.getApiSevice().getProductById(idProduct).enqueue(new Callback<ResponseData<Product>>() {
            @Override
            public void onResponse(Call<ResponseData<Product>> call, Response<ResponseData<Product>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Product> responseData=response.body();
                    if (responseData.getStatus().equals("200")) {
                        Product product = responseData.getData();
                        holder.tvNameProduct.setText(product.getProductName());
                        holder.tvPriceProduct.setText(product.getPrice()+"đ");
                        holder.tvQuantity.setText("x"+billDetailsList.get(postion).getQuantity());
                        int totalPrice=product.getPrice().intValue()*billDetailsList.get(postion).getQuantity().intValue();
                        holder.tvTotalPrice.setText(totalPrice+"đ");
                        int resId=context.getResources().getIdentifier(product.getImage(),"drawable",context.getPackageName());
                        holder.imgProduc.setImageResource(resId);
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
