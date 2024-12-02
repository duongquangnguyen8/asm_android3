package com.example.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Account;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MangerUserAdapter extends RecyclerView.Adapter<MangerUserAdapter.MangerUserViewHolder> {

    private List<Account> accountList;
    private Context context;
    public MangerUserAdapter(List<Account> accountList, Context context) {
        this.accountList = accountList;
        this.context = context;
    }

    @NonNull
    @Override
    public MangerUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MangerUserViewHolder(View.inflate(context, R.layout.layout_item_manger_user,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MangerUserViewHolder holder, int position) {
        Account account=accountList.get(position);
        holder.tvIdUser.setText("Id: "+account.getId());
        holder.tvEmailUser.setText("Email: "+account.getEmail());
        holder.tvNameUser.setText("Tên: "+account.getFullName());
        holder.tvAddressUser.setText("Địa chỉ: "+account.getAddress());
        holder.tvPhoneNumberUser.setText("Số điện thoại: "+account.getPhoneNumber());

        holder.tvBirthUser.setText("Ngày sinh: "+account.getBirth());
        holder.tvCreatedAtUser.setText("Ngày tạo: "+account.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return accountList!=null?accountList.size():0;
    }


    public class MangerUserViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdUser,tvEmailUser,tvNameUser,tvAddressUser,tvPhoneNumberUser,tvBirthUser,tvRoleUser,tvCreatedAtUser,tvUpdateAtUser;

        public MangerUserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdUser=itemView.findViewById(R.id.tviduser);
            tvEmailUser=itemView.findViewById(R.id.tvEmailUser);
            tvNameUser=itemView.findViewById(R.id.tvNameUser);
            tvAddressUser=itemView.findViewById(R.id.tvAddressUser);
            tvPhoneNumberUser=itemView.findViewById(R.id.tvPhoneNumberUser);
            tvBirthUser=itemView.findViewById(R.id.tvBirthUser);
            tvRoleUser=itemView.findViewById(R.id.tvRoleUser);
            tvCreatedAtUser=itemView.findViewById(R.id.tvCreatedAtUser);

        }
    }
}