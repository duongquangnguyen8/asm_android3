package com.example.myapplication.adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.BillDetail;
import com.example.myapplication.models.BillDetailsWrapperList;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusBillAdapter extends RecyclerView.Adapter<StatusBillAdapter.StatusBillViewHolder> {
    private List<BillDetailsWrapperList> billDetailsWrapperLists;
    private Context context;
    private Number totalPrice;
    private HttpRequest requestProduct;
    private String statusBill;
    public StatusBillAdapter(List<BillDetailsWrapperList> billDetailsWrapperLists, Context context) {
        this.billDetailsWrapperLists = billDetailsWrapperLists;
        this.context = context;
    }

    @NonNull
    @Override
    public StatusBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatusBillViewHolder(View.inflate(context, R.layout.layout_item_status_bill,null));

    }

    @Override
    public void onBindViewHolder(@NonNull StatusBillViewHolder holder, int position) {
        BillDetailsWrapperList billDetailsWrapperList=billDetailsWrapperLists.get(position);
        holder.tvQuantity.setText("x" + billDetailsWrapperList.getBillDetails().getQuantity());
        holder.tvTotalPrice.setText(billDetailsWrapperList.getBillDetails().getPrice() + "đ");
        if (billDetailsWrapperList.getStatusBill().equals("APPROVAL")){
            holder.tvStatus.setText("Chờ phê duyệt");
        }else if (billDetailsWrapperList.getStatusBill().equals("CANCELED")){
            holder.tvStatus.setText("Đã hủy");
        } else if (billDetailsWrapperList.getStatusBill().equals("DELIVERING")) {
                holder.tvStatus.setText("Đang giao");
        }else if (billDetailsWrapperList.getStatusBill().equals("PENDING")) {
            holder.tvStatus.setText("Đã giao");
        }
            
        // Lấy thông tin sản phẩm
        getProductById(billDetailsWrapperList.getBillDetails().getProductId(),billDetailsWrapperList.getBillDetails().getQuantity().intValue(),holder);
    }

    @Override
    public int getItemCount() {
        return billDetailsWrapperLists!=null?billDetailsWrapperLists.size():0;
    }


    public class StatusBillViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvNameProduct,tvPriceProduct,tvStatus,tvDetail,tvQuantity,tvTotalPrice;
        public StatusBillViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct=itemView.findViewById(R.id.imgProduct_status_order);
            tvNameProduct=itemView.findViewById(R.id.tvNameProduct_status_order);
            tvPriceProduct=itemView.findViewById(R.id.tvPriceProduct_status_order);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvDetail=itemView.findViewById(R.id.tvDetail);
            tvQuantity=itemView.findViewById(R.id.tvQuantity_status_order);
            tvTotalPrice=itemView.findViewById(R.id.tvTotalPrice_status_order);
        }
    }
    private void getProductById(String idPrduct,int quantity,StatusBillViewHolder holder){
        requestProduct=new HttpRequest(ApiService.URL_PRODUCT);
        requestProduct.getApiSevice().getProductById(idPrduct).enqueue(new Callback<ResponseData<Product>>() {
            @Override
            public void onResponse(Call<ResponseData<Product>> call, Response<ResponseData<Product>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Product> responseData=response.body();
                    if (responseData.getStatus().equals("200")){
                        Product product=responseData.getData();
                        holder.tvNameProduct.setText(product.getProductName());
                        holder.tvPriceProduct.setText(product.getPrice()+"đ");
                        int resourceId = context.getResources().getIdentifier(product.getImage(), "drawable", context.getPackageName());
                        holder.imgProduct.setImageResource(resourceId);
                        int totalPrice = product.getPrice().intValue() * quantity;
                        holder.tvTotalPrice.setText(totalPrice + "đ");
                    }else{
                        Toast.makeText(context, "loi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "loi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Product>> call, Throwable t) {
                Toast.makeText(context, "loi"+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
