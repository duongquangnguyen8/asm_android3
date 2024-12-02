package com.example.myapplication.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.fragment.BillManegerFragment;
import com.example.myapplication.fragment.ProductMangerFragment;
import com.example.myapplication.fragment.UserManegerFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeActivityAdmin extends AppCompatActivity {

    private FrameLayout frameLayout;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private MenuItem prevMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerlayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUi();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quản lí người dùng");
        toggle=new ActionBarDrawerToggle(this,drawerLayout,
                toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new UserManegerFragment()).commit();
            navigationView.setCheckedItem(R.id.item_manger_user);
            navigationView.getMenu().performIdentifierAction(R.id.item_manger_user, 0); // Kích hoạt mục menu
        }
        if(navigationView!=null){
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                Fragment fragmentSelected=null;
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(item.getItemId()==R.id.item_manger_user){
                        fragmentSelected=new UserManegerFragment();
                        getSupportActionBar().setTitle("Quản lí người dùng");
                    } else if (item.getItemId()==R.id.item_manger_product) {
                        fragmentSelected=new ProductMangerFragment();
                        getSupportActionBar().setTitle("Quản lí sản phẩm");
                    }else if (item.getItemId()==R.id.item_manger_order) {
                        fragmentSelected=new BillManegerFragment();
                        getSupportActionBar().setTitle("Quản lí hoá đơn");
                    } else if (item.getItemId()==R.id.item_logout) {
                        SharedPreferences sharedPreferences= getSharedPreferences("USER_ID", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(HomeActivityAdmin.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Đặt cờ để xóa lịch sử hoạt động và nó khởi tạo các activity mới
                        startActivity(intent);
                    }
                    if (fragmentSelected!=null){
                        if (prevMenuItem != null) {
                            prevMenuItem.setChecked(false); // Bỏ chọn mục trước đó
                        }
                        item.setChecked(true); // Đánh dấu mục hiện tại
                        prevMenuItem = item;
                        FragmentManager manager=getSupportFragmentManager();
                        FragmentTransaction transaction=manager.beginTransaction();
                        transaction.replace(R.id.frameLayout,fragmentSelected);
                        transaction.commit();
                    }
                    drawerLayout.closeDrawers();
                    return true;
                }
            });
        }else { Log.e("HomeActivityAdmin", "NavigationView is null"); }

    }
    private void initUi(){
        frameLayout=findViewById(R.id.frameLayout);
        toolbar=findViewById(R.id.toolbarAdmin);
        drawerLayout=findViewById(R.id.drawerlayout);
        navigationView=findViewById(R.id.navigation_view);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle != null && toggle.onOptionsItemSelected(item)) {
            return true; // Đảm bảo toggle hoạt động đúng
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView); // Đóng DrawerLayout nếu đang mở
        } else {
            // Không cho phép quay lại, hiển thị cảnh báo
            Toast.makeText(this, "Bạn không thể quay lại từ đây!", Toast.LENGTH_SHORT).show();
        }
    }
}