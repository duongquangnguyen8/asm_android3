package com.example.myapplication.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Category;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> listCategory;
    private Context context;

    public CategoryAdapter(List<Category> listCategory, Context context) {
        this.listCategory = listCategory;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_item_category,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category=listCategory.get(position);
        if(category!=null) {
            holder.image.setImageResource(category.getImage());
            holder.type.setText(category.getType());
        }
    }

    @Override
    public int getItemCount() {
        if(listCategory!=null){
            return listCategory.size();
        };
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView type;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.imgCategory);
            type=itemView.findViewById(R.id.tvTypeCategory);
        }
    }
}
