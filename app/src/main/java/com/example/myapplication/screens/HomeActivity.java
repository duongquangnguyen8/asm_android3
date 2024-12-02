package com.example.myapplication.screens;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ViewPagerAdapter;
import com.example.myapplication.fragment.CartFragment;
import com.example.myapplication.fragment.FavoriteFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.MangerOrderFragment_user;
import com.example.myapplication.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    public static String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activty_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottom_navigation), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, 0, systemBars.bottom);  // Chỉ thêm padding cho phần bottom
            return insets;
        });
        initUi();
        setupViewPager();
        SharedPreferences sharedPreferences=getSharedPreferences("USER_ID",MODE_PRIVATE);
        userId=sharedPreferences.getString("id","");
        bottomNavigationView.setOnItemSelectedListener(item -> {
           if (item.getItemId()==R.id.menu_home){
               viewPager2.setCurrentItem(0);
               return true;
           }else if (item.getItemId()==R.id.favorite){
               viewPager2.setCurrentItem(1);
               return true;
           }else if (item.getItemId()==R.id.cart){
               viewPager2.setCurrentItem(2);
               return true;
           }else if (item.getItemId()==R.id.menu_manger){
               viewPager2.setCurrentItem(3);
               return true;
           }
           else if (item.getItemId()==R.id.user){
               viewPager2.setCurrentItem(4);
               return true;
           }
           return false;
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.menu_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.favorite);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.cart);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.menu_manger);
                        break;
                    case 4:
                        bottomNavigationView.setSelectedItemId(R.id.user);
                        break;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Log.d("back","no back");
    }

    private void initUi(){
        viewPager2=findViewById(R.id.viewPager_home);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
    }
    private void setupViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new FavoriteFragment());
        fragments.add(new CartFragment());
        fragments.add(new MangerOrderFragment_user());
        fragments.add(new UserFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, fragments);
        viewPager2.setAdapter(adapter);
        viewPager2.setUserInputEnabled(false);
    }
}