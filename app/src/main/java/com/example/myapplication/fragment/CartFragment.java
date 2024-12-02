package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.models.Cart;
import com.example.myapplication.models.CartDetail;
import com.example.myapplication.models.ResponseData;
import com.example.myapplication.screens.HomeActivity;
import com.example.myapplication.screens.OrderActivity;
import com.example.myapplication.service.ApiService;
import com.example.myapplication.service.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private List<CartDetail> cartDetailList;
    private CartAdapter adapter;
    private RecyclerView recyclerView;
    private HttpRequest requestCartDetail,requestCart;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    public CartFragment() {
        // Required empty public constructor
    }


    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView=view.findViewById(R.id.rcvCart);
        requestCart=new HttpRequest(ApiService.URL_CART);
        requestCartDetail=new HttpRequest(ApiService.URL_CartDetail);
        tvTotalPrice=view.findViewById(R.id.tvTotalPrice_cart);
        btnCheckout=view.findViewById(R.id.btnCheckOut_cart);

        getCartByUserId();
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), OrderActivity.class);
                String newTotalPrice=tvTotalPrice.getText().toString().replace("đ","");
                intent.putExtra("totalPrice",newTotalPrice);
                ArrayList<Integer> quantities=new ArrayList<>();
                ArrayList<Integer> prices=new ArrayList<>();
                ArrayList<String> listIdProduct=new ArrayList<>();
                ArrayList<String> listcartDetails=new ArrayList<>();
                for (CartDetail cartDetail:cartDetailList){
                    quantities.add(cartDetail.getQuantity().intValue());
                    prices.add(cartDetail.getPrice().intValue());
                    listIdProduct.add(cartDetail.getProductId());
                    listcartDetails.add(cartDetail.getId());
                }
                intent.putIntegerArrayListExtra("quantities",quantities);
                intent.putIntegerArrayListExtra("prices",prices);
                intent.putStringArrayListExtra("listIdProduct",listIdProduct);
                intent.putExtra("idcart",cartDetailList.get(0).getCartId());
                intent.putStringArrayListExtra("cartDetailId",listcartDetails);
                startActivity(intent);
            }
        });
        return view;
    }
    private void getCartByUserId(){
        requestCart.getApiSevice().getCartByidAccount(HomeActivity.userId).enqueue(new Callback<ResponseData<Cart>>() {
            @Override
            public void onResponse(Call<ResponseData<Cart>> call, Response<ResponseData<Cart>> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    ResponseData<Cart> responseData=response.body();
                    if (responseData.getStatus().equals("200")){
                        Cart cart=responseData.getData();
                        if(cart!=null){
                            getCartDetailByidCart(cart.getId());
                        }
                    }else{
                        Log.d("cart","Lỗi API"+responseData.getMessage());
                    }
                }else{
                    Log.d("cart","Lỗi kết nối");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Cart>> call, Throwable t) {
                Log.d("cart","Lỗi kết nối"+t.getMessage());
            }
        });
    }
    private void getCartDetailByidCart(String idCart){
        requestCartDetail.getApiSevice().getCartDetailByIdCart(idCart).enqueue(new Callback<ResponseData<List<CartDetail>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<CartDetail>>> call, Response<ResponseData<List<CartDetail>>> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    ResponseData<List<CartDetail>> responseData=response.body();
                    if("200".equals(responseData.getStatus())){
                        cartDetailList=responseData.getData();
                        adapter=new CartAdapter(cartDetailList,getContext(),CartFragment.this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                        updateTotalPrice();
                        Log.d("cartdetail",cartDetailList.size()+"");
                    }else{
                        Log.d("cartdetail",responseData.getMessage());
                    }
                }else{
                    Log.d("cartdetail","Lỗi kết nối");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<CartDetail>>> call, Throwable t) {
                Log.d("cartdetail","Lỗi kết nối"+t.getMessage());
            }
        });
    }
    public void updateTotalPrice() {
        int totalPrice = 0;
        for (CartDetail cartDetail : cartDetailList) {
            totalPrice += cartDetail.getQuantity().intValue() * cartDetail.getPrice().intValue();
        }
        tvTotalPrice.setText(String.format("%sđ", totalPrice));
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        super.onResume();
        getCartByUserId();
    }
}