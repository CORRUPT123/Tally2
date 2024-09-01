package com.wkk.tally.adapter;

import static com.wkk.tally.R.color.green_006400;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wkk.tally.R;

import java.util.ArrayList;
import java.util.List;

// 点击日历图标，弹出对话框后，对话框里的月份选择GridView的适配器
public class CaldendarAdapter extends BaseAdapter {
    public int year;
    Context mcontext;
    List<String> mDatas;
    public int selectMonth = -1;
    public CaldendarAdapter(int year, Context mcontext) {
        this.year = year;
        this.mcontext = mcontext;
        mDatas = new ArrayList<>();
        loadData(year);
    }

    private void loadData(int year)
    {
        for (int i = 0; i < 12; i++) {
            String string = year + "/" + (i + 1);
            mDatas.add(string);
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        mDatas.clear();
        loadData(year);
        notifyDataSetChanged();
    }

    public int getSelectMonth() {
        return selectMonth;
    }

    public void setSelectMonth(int selectMonth) {
        this.selectMonth = selectMonth;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mcontext).inflate(R.layout.item_dialogcal_gv, viewGroup, false);
        TextView textView = view.findViewById(R.id.item_dialogcal_gv_tv);
        textView.setText(mDatas.get(i));
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(16);
        if (selectMonth == i) {
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(19);
        }
        return view;
    }
}
