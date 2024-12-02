package com.example.myapplication.service;

import com.example.myapplication.models.Account;
import com.example.myapplication.models.Bill;
import com.example.myapplication.models.BillDetail;
import com.example.myapplication.models.Cart;
import com.example.myapplication.models.CartDetail;
import com.example.myapplication.models.Favorite;
import com.example.myapplication.models.Product;
import com.example.myapplication.models.ResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    //account
    static String Url_post_account = "http://10.0.3.2:3000/account/";
    @POST("add_account")
    Call<ResponseData<Account>> registerAccount(@Body Account account);
    @GET("get_all_account")
    Call<ResponseData<List<Account>>> getAllAccount();
    @GET("getAccountById/{id}")
    Call<ResponseData<Account>> getAccountById(@Path("id") String id);
    @PUT("updateAccountById/{id}")
    Call<ResponseData<Account>> updateAccount(@Path("id") String id, @Body Account account);
    @GET("getAccountByIdRole/{id}")
    Call<ResponseData<List<Account>>> getAccountByIdRole(@Path("id") String id);



    //product
    static String URL_PRODUCT = "http://10.0.3.2:3000/product/";
    @GET("get_all_product")
    Call<ResponseData<List<Product>>> getAllProduct();
    @GET("get_productById/{id}")
    Call<ResponseData<Product>> getProductById(@Path("id") String id);
    @POST("add_product")
    Call<ResponseData<Product>> addProduct(@Body Product product);
    @PUT("updateProductById/{id}")
    Call<ResponseData<Product>> updateProduct(@Path("id") String id, @Body Product product);
    @DELETE("delete_productById/{id}")
    Call<ResponseData<Void>> deleteProductCall(@Path("id") String id);

    //favorite
    static String URL_FAVORITE = "http://10.0.3.2:3000/favorite/";
    @GET("get_all_favorite")
    Call<ResponseData<List<Favorite>>> getAllFavorite();
    @POST("add_favorite")
    Call<ResponseData<Favorite>> addFavorite(@Body Favorite favorite);
    @DELETE("delete_favorite_byId/{id}")
    Call<ResponseData<Void>> deleteFavoriteCall(@Path("id") String id);
    @DELETE("deleteFavoriteByIdProduct/:idProduct")
    Call<ResponseData<Void>> deleteFavoriteByIdProduct(@Path("idProduct") String idProduct);

    //Cart
    static String URL_CART = "http://10.0.3.2:3000/cart/";
    @POST("add_cart")
    Call<ResponseData<Cart>> addCart(@Body Cart cart);
    @GET("getAllCart")
    Call<ResponseData<List<Cart>>> getAllCart();
    @GET("getCartByidAccount/{idAccount}")
    Call<ResponseData<Cart>> getCartByidAccount(@Path("idAccount") String idAccount);

    //CartDetail
    static String URL_CartDetail = "http://10.0.3.2:3000/cartDetail/";
    @POST("add_cartDetail")
    Call<ResponseData<CartDetail>> addCartDetail(@Body CartDetail cartDetail);
    @PUT("updateCartDetailById/{id}")
    Call<ResponseData<CartDetail>> updateCartDetail(@Path("id") String id, @Body CartDetail cartDetail);
    @GET("getCartDetailByidCartAndidProduct/{idCart}/{idProduct}")
    Call<ResponseData<CartDetail>> getCartDetailByidCartAndidProduct(@Path("idCart") String idCart, @Path("idProduct") String idProduct);
    @GET("getCartDetailByIdCart/{idCart}")
    Call<ResponseData<List<CartDetail>>> getCartDetailByIdCart(@Path("idCart") String idCart);
    @DELETE("deteCartDetailById/{id}")
    Call<ResponseData<Void>> deleteCartDetailCall(@Path("id") String id);
    @DELETE("deleteCartDetailByIdProduct/{idProduct}")
    Call<ResponseData<Void>> deleteCartDetailByIdProduct(@Path("idProduct") String idProduct);
    @GET("search")
    Call<ResponseData<List<Product>>> searchProducts(@Query("name") String name);

    //Bill
    static String URL_Bill = "http://10.0.3.2:3000/bill/";
    @POST("add_bill")
    Call<ResponseData<Bill>> addBill(@Body Bill bill);
    @GET("getBillByIdAccount/{idAccount}")
    Call<ResponseData<List<Bill>>> getBillByIdAccount(@Path("idAccount") String idAccount);
    @GET("getAllBill")
    Call<ResponseData<List<Bill>>> getAllBill();
    @PUT("updateBillById/{id}")
    Call<ResponseData<Bill>> updateStatusBill(@Path("id") String id,@Body Bill bill);

    //BillDetail
    static String URL_BillDetail = "http://10.0.3.2:3000/billDetail/";
    @POST("add_billDetail")
    Call<ResponseData<BillDetail>> addBillDetail(@Body BillDetail billDetail);
    @GET("getBillDetailByIdBill/{idBill}")
    Call<ResponseData<List<BillDetail>>> getBillDetailByIdBill(@Path("idBill") String idBill);

}
