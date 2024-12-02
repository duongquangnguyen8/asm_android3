package com.example.myapplication.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class ProductDetail_mangerActivity extends AppCompatActivity {

    private TextView tvNameProduct,tvPriceProduct,tvDescription;
    private ImageView imgBack,imgProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail_manger);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initui();
        imgBack.setOnClickListener(view -> {
            finish();
        });
        Intent intent=getIntent();
        String nameProduct=intent.getStringExtra("name_product_manger");
        String priceProduct=intent.getStringExtra("price_product_manger");
        String descriptionProduct=intent.getStringExtra("description_product_manger");
        String imageProduct=intent.getStringExtra("image_product_manger");

        tvNameProduct.setText(nameProduct);
        tvPriceProduct.setText(priceProduct);
        tvDescription.setText(descriptionProduct);
        int resID=getResources().getIdentifier(imageProduct,"drawable",getPackageName());
        imgProduct.setImageResource(resID);
    }
    private void initui(){
        tvNameProduct=findViewById(R.id.tvNameProduct_detail_manager);
        tvPriceProduct=findViewById(R.id.tvPriceProduct_detail_manager);
        tvDescription=findViewById(R.id.tvDescription_product_detail_manager);
        imgBack=findViewById(R.id.imgBack_product_detail_manager);
        imgProduct=findViewById(R.id.imgProduct_detail_manager);

    }
}