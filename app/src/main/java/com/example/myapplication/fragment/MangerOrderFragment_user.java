package com.example.myapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class MangerOrderFragment_user extends Fragment {


    public MangerOrderFragment_user() {
        // Required empty public constructor
    }


    public static MangerOrderFragment_user newInstance(String param1, String param2) {
        MangerOrderFragment_user fragment = new MangerOrderFragment_user();
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
        View view= inflater.inflate(R.layout.fragment_manger_order, container, false);
        TabLayout tabLayout=view.findViewById(R.id.tab_layout_manger_order);
        ViewPager2 viewPager2=view.findViewById(R.id.view_pager_manger_order);
        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(new PheDuytFragment());
        fragmentList.add(new DangGiaoFragment());
        fragmentList.add(new DaGiaoFragment());
        fragmentList.add(new HuyFragment());
        ViewPagerAdapter adapter=new ViewPagerAdapter(getActivity(),fragmentList);
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout,viewPager2,(tab,position)->{
           switch (position){
               case 0:
                   tab.setText("Chờ phê duyệt");
                   break;
               case 1:
                   tab.setText("Đang giao");
                   break;
               case 2:
                   tab.setText("Đã giao");
                   break;
               case 3:
                   tab.setText("Đã huỷ");
           }
        }).attach();
        return view;
    }
}