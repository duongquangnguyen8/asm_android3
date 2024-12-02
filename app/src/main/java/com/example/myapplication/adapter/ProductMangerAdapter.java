package com.example.myapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.screens.ProductDetail_mangerActivity;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductMangerAdapter extends RecyclerView.Adapter<ProductMangerAdapter.ProductMangerViewHolder> {


    private List<Product> productList;
    private Context context;
    private HttpRequest requestProduct,requestCartDetail,requestFavorite;

    public ProductMangerAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductMangerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductMangerViewHolder(View.inflate(context, R.layout.layout_item_manger_product,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductMangerViewHolder holder, int position) {

        Product product=productList.get(position);
        holder.tvNameProduct.setText(product.getProductName());
        holder.tvPriceProduct.setText(product.getPrice()+"đ");
        int resID=context.getResources().getIdentifier(product.getImage(),"drawable",context.getPackageName());
        holder.imgProduct.setImageResource(resID);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDetail_mangerActivity.class);
                intent.putExtra("name_product_manger",product.getProductName());
                intent.putExtra("price_product_manger",holder.tvPriceProduct.getText().toString());
                intent.putExtra("description_product_manger",product.getDescription());
                intent.putExtra("image_product_manger",product.getImage());
                context.startActivity(intent);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelteProductDialog(product.getId(),position);
            }
        });
        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddProductDialog(product.getId(),product.getImage(),product.
                        getProductName(),product.getDescription(),product.getPrice().intValue(),
                        product.getCategoryId().equals("673e0dab817e7fcd27bdf2f3")?"673e0dab817e7fcd27bdf2f3":"673e0d92817e7fcd27bdf2f1",position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList!=null?productList.size():0;
    }


    public class ProductMangerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct,imgUpdate,imgDelete;
        TextView tvNameProduct,tvPriceProduct;

        public ProductMangerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct=itemView.findViewById(R.id.imgPrduct_mager);
            tvNameProduct=itemView.findViewById(R.id.tvNameProduct_manger);
            tvPriceProduct=itemView.findViewById(R.id.tvPriceProduct_manger);
            imgUpdate=itemView.findViewById(R.id.imgUpdateProduct_manger);
            imgDelete=itemView.findViewById(R.id.imgDeleteProduct_manger);

        }
    }
    private  void showDelteProductDialog(String idProduct,int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có muốn xóa sản phẩm này không?");
        builder.setPositiveButton("Đồng ý", (dialogInterface, i) ->{
//            deleteProduct(idProduct, position);
//            deleteCartDetail(idProduct);
//            deleteFavorite(idProduct);
        });
        builder.setNegativeButton("Không",(dialogInterface, i) ->{
            dialogInterface.dismiss();
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    private void deleteProduct(String idProduct,int position){
        requestProduct=new HttpRequest(ApiService.URL_PRODUCT);
        requestProduct.getApiSevice().deleteProductCall(idProduct).enqueue(new Callback<ResponseData<Void>>() {
            @Override
            public void onResponse(Call<ResponseData<Void>> call, Response<ResponseData<Void>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Void> responseData=response.body();
                    if (responseData.getStatus().equals("200")&&responseData.getData()!=null){
                        productList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Void>> call, Throwable t) {
                Toast.makeText(context, "Lỗi"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteCartDetail(String idProduct){
        requestCartDetail=new HttpRequest(ApiService.URL_CartDetail);
        requestCartDetail.getApiSevice().deleteCartDetailByIdProduct(idProduct).enqueue(new Callback<ResponseData<Void>>() {
            @Override
            public void onResponse(Call<ResponseData<Void>> call, Response<ResponseData<Void>> response) {
                ResponseData<Void> responseData=response.body();
                if (responseData.getStatus().equals("200")&&responseData.getData()!=null){
                    Log.d("zzz","deletecartdetail success");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Void>> call, Throwable t) {

            }
        });
    }
    private void deleteFavorite(String idProduct){
        requestFavorite=new HttpRequest(ApiService.URL_FAVORITE);
        requestFavorite.getApiSevice().deleteFavoriteByIdProduct(idProduct).enqueue(new Callback<ResponseData<Void>>() {
            @Override
            public void onResponse(Call<ResponseData<Void>> call, Response<ResponseData<Void>> response) {
                ResponseData<Void> responseData=response.body();
                if (responseData.getStatus().equals("200")&&responseData.getData()!=null){
                    Log.d("zzz","deletefavorite success");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Void>> call, Throwable t) {

            }
        });
    }
    private void showAddProductDialog(String idProduct,String imageUrl,String nameProduct,String description,Integer price,String category,int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View dialogView=inflater.inflate(R.layout.custom_dialog_product_manager,null);
        builder.setView(dialogView);

        EditText edtProductName=dialogView.findViewById(R.id.et_product_name);
        EditText edtProductPrice=dialogView.findViewById(R.id.et_product_price);
        EditText edtProductDescription=dialogView.findViewById(R.id.et_product_description);
        EditText edtProductImageUrl=dialogView.findViewById(R.id.et_product_image_url);
        Button btnAddProduct=dialogView.findViewById(R.id.btn_add_product);
        Spinner spnProductCategory=dialogView.findViewById(R.id.spinner_product_category);

        List<String> categories=new ArrayList<>();
        categories.add("Trà sữa");
        categories.add("Cà phê");

        ArrayAdapter<String> adapter1=new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,categories);
        spnProductCategory.setAdapter(adapter1);
        edtProductName.setText(nameProduct);
        edtProductPrice.setText(price.toString());
        edtProductDescription.setText(description);
        edtProductImageUrl.setText(imageUrl);
        if (category.equals("673e0d92817e7fcd27bdf2f1")){
            spnProductCategory.setSelection(1);
        }else{
            spnProductCategory.setSelection(0);
        }

        AlertDialog dialog=builder.create();
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName=edtProductName.getText().toString();
                String strPrice=edtProductPrice.getText().toString();
                String strDescription=edtProductDescription.getText().toString();
                String strImageUrl=edtProductImageUrl.getText().toString();
                String strCategory=spnProductCategory.getSelectedItem().toString();
                if (strName.isEmpty()||strPrice.isEmpty()||strDescription.isEmpty()||strImageUrl.isEmpty()||strCategory.isEmpty()){
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if (spnProductCategory.getSelectedItemPosition()==0){
                        Product product=new Product(strName,strDescription,Integer.parseInt(strPrice),strImageUrl,"673e0dab817e7fcd27bdf2f3");
                        updateProduct(idProduct,product,position);
                    }else{
                        Product product=new Product(strName,strDescription,Integer.parseInt(strPrice),strImageUrl,"673e0d92817e7fcd27bdf2f1");
                        updateProduct(idProduct,product,position);
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    private void updateProduct(String idProduct,Product product,int position){
        requestProduct=new HttpRequest(ApiService.URL_PRODUCT);
        requestProduct.getApiSevice().updateProduct(idProduct,product).enqueue(new Callback<ResponseData<Product>>() {
            @Override
            public void onResponse(Call<ResponseData<Product>> call, Response<ResponseData<Product>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Product> responseData=response.body();
                    if (responseData.getStatus().equals("200")&&responseData.getData()!=null){
                        productList.set(position, responseData.getData());
                        notifyItemChanged(position);

                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Product>> call, Throwable t) {

            }
        });
    }
}
