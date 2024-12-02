package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Bill;
import com.example.myapplication.screens.BillDetailManagerActivity;

import java.util.List;

public class ManagerBillAdapter extends RecyclerView.Adapter<ManagerBillAdapter.ManagerBillViewHolder> {
    private List<Bill> billList;
    private Context context;

    public ManagerBillAdapter(List<Bill> billList, Context context) {
        this.billList = billList;
        this.context = context;
    }

    @NonNull
    @Override
    public ManagerBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ManagerBillViewHolder(View.inflate(context, R.layout.layout_item_manger_bill,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerBillViewHolder holder, int position) {
        Bill bill=billList.get(position);
        holder.tvAccountOrder.setText("Tài khoản đặt hàng: "+bill.getAccountId());
        holder.tvTotalPrice_manger_bill.setText("Thành tiên: "+bill.getTotalPrice()+"đ");
        holder.tvDateOrder_bill.setText("Ngày đặt hàng: "+bill.getCreatedAt());

        if (bill.getStatusBill().equals("APPROVAL")){
            holder.tvStatus_manger_bill.setText("Chờ phê duyệt");
        }else if (bill.getStatusBill().equals("CANCELED")){
            holder.tvStatus_manger_bill.setText("Đã hủy");
        } else if (bill.getStatusBill().equals("DELIVERING")) {
            holder.tvStatus_manger_bill.setText("Đang giao");
        }else if (bill.getStatusBill().equals("PENDING")) {
            holder.tvStatus_manger_bill.setText("Đã giao");
        }

        holder.tvDetail_manger_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, BillDetailManagerActivity.class);
                intent.putExtra("billId",bill.getId());
                intent.putExtra("total",holder.tvTotalPrice_manger_bill.getText().toString());
                Log.d("zzz",bill.getTotalPrice()+"");
                intent.putExtra("shippingName",bill.getShippingName());
                intent.putExtra("shippingPhone",bill.getShippingPhone());
                intent.putExtra("shippingAddress",bill.getShippingAddress());
                intent.putExtra("status",bill.getStatusBill());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return billList!=null?billList.size():0;
    }


    public class ManagerBillViewHolder extends RecyclerView.ViewHolder {
        TextView tvAccountOrder,tvTotalPrice_manger_bill,tvStatus_manger_bill,tvDateOrder_bill,tvDetail_manger_bill;

        public ManagerBillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAccountOrder=itemView.findViewById(R.id.tvAccountOrder);
            tvTotalPrice_manger_bill=itemView.findViewById(R.id.tvTotalPrice_manger_bill);
            tvStatus_manger_bill=itemView.findViewById(R.id.tvStatus_manger_bill);
            tvDateOrder_bill=itemView.findViewById(R.id.tvDateOrder_bill);
            tvDetail_manger_bill=itemView.findViewById(R.id.tvDetail_manger_bill);
        }
    }
}