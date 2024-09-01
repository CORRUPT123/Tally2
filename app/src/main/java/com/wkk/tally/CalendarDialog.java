package com.wkk.tally;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wkk.tally.adapter.CaldendarAdapter;
import com.wkk.tally.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener {
    ImageView back;
    GridView gv;
    LinearLayout hsvLayout;
    List<TextView> hsvViewList;
    List<Integer> yearList;
    int selectPos = -1; // 当前选中年份再yearList中的位置
    public CalendarDialog(@NonNull Context context) {
        super(context);
    }
    int selectMonth = -1;
    CaldendarAdapter caldendarAdapter;

    public interface onRefreshListenser{
        public void OnRefesh(int year, int month);
    }
    onRefreshListenser onRefreshListenser;

    public void setOnRefreshListenser(CalendarDialog.onRefreshListenser onRefreshListenser) {
        this.onRefreshListenser = onRefreshListenser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        gv = findViewById(R.id.dialog_calendar_gv);
        hsvLayout = findViewById(R.id.dialog_calendar_layout);
        back = findViewById(R.id.dailog_calendar_iv);
        back.setOnClickListener(this::onClick);
        // 向横向的ScrollView中添加数据库中存在的年分
        addViewToScrollView();
        initGridView();
        setGVListenser();
    }

    private void setGVListenser() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                caldendarAdapter.setSelectMonth(i);
                caldendarAdapter.notifyDataSetInvalidated();
                onRefreshListenser.OnRefesh(yearList.get(selectPos), i + 1);
            }
        });
    }

    private void initGridView() {
        int selyear = yearList.get(selectPos);
        caldendarAdapter = new CaldendarAdapter(selyear, getContext());
        caldendarAdapter.selectMonth = selectMonth == -1 ? Calendar.getInstance().get(Calendar.MONTH) : selectMonth - 1;
        gv.setAdapter(caldendarAdapter);
    }

    private void addViewToScrollView() {
        hsvViewList = new ArrayList<>();
        yearList = DBManager.getYearListFromDB();
        if (yearList.size() == 0)  // 这个页面总得显示点东西，所以加入数据库中没有数据的话就把当年加进去
            yearList.add(Calendar.getInstance().get(Calendar.YEAR));
        for (int i = 0; i < yearList.size(); i++)
        {
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_dialog_calendar_hsv, null);
            hsvLayout.addView(view);
            TextView viewbyid = view.findViewById(R.id.item_dialogcal_hsv_tv);
            viewbyid.setText(year + "");
            hsvViewList.add(viewbyid);
        }
        selectPos = selectPos == -1 ? yearList.size() - 1 : selectPos;
        changeTvbg(selectPos);
        setHSVListener();
    }

    private void setHSVListener() {
        for (int i = 0; i < hsvViewList.size(); i++)
        {
            TextView textView = hsvViewList.get(i);
            final int pos = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTvbg(pos);
                    selectPos = pos;
                    Integer i1 = yearList.get(selectPos);
                    caldendarAdapter.setYear(i1);

                }
            });
        }

    }

    @SuppressLint("ResourceAsColor")
    private void changeTvbg(int selectPos) {
        for (int i = 0; i < hsvViewList.size(); i++)
        {
            TextView textView = hsvViewList.get(i);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(18);
        }
        TextView textView = hsvViewList.get(selectPos);
        textView.setTextColor(R.color.green_006400);
        textView.setTextSize(19);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dailog_calendar_iv)
        {
            cancel();
            return;
        }
    }
    public void setDialogSize()
    {
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = d.getWidth();
        wlp.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.white);
        window.setAttributes(wlp);
    }

}
