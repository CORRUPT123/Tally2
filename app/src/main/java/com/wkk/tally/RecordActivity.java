package com.wkk.tally;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wkk.tally.adapter.RecordFragPager;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tableLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record);
        // 查找控件
        tableLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);

        initPager();
    }

    private void initPager() {
        // 初始化Viewpager的页面集合
        List<Fragment> fragmentList = new ArrayList<>();
        // 创建两个fragment并装入fragmentList
        in_frag in = new in_frag();
        out_frag out = new out_frag();
        fragmentList.add(in);
        fragmentList.add(out);
        // 创建适配器
        RecordFragPager recordFragPager = new RecordFragPager(getSupportFragmentManager(), fragmentList);
        // 设置适配器
        viewPager.setAdapter(recordFragPager);
        // 将TabLayout和ViewPager关联
        tableLayout.setupWithViewPager(viewPager);

    }

    public void onClick(View view) {

        if (view.getId() == R.id.record_iv_back)
        {
            finish();
        }
    }
}