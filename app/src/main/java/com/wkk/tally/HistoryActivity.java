package com.wkk.tally;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.wkk.tally.adapter.AccountAdapter;
import com.wkk.tally.db.AccountBean;
import com.wkk.tally.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    ListView historyLv;
    TextView timeTv;
    ImageView back, hist;
    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int year, month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);
        back = findViewById(R.id.history_back);
        hist = findViewById(R.id.history_rili);
        hist.setOnClickListener(this::Click);
        back.setOnClickListener(this::Click);
        timeTv.setOnClickListener(this::Click);
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        historyLv.setAdapter(adapter);
        init();
    }

    private void init() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        timeTv.setText(year + "年" + month + "月");
        getHistoryData(year, month);

    }

    private void getHistoryData(int year, int month) {
        Log.e("WKKK", "getHistoryData: " + year + "\\" + month);
        List<AccountBean> accountListOnMonthFromAccountTb = DBManager.getAccountListOnMonthFromAccountTb(year, month);
        mDatas.clear();
        mDatas.addAll(accountListOnMonthFromAccountTb);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "查询到 " + mDatas.size() + "条记录", Toast.LENGTH_SHORT ).show();
    }

    public void Click(View view)
    {
        if (view.getId() == R.id.history_lv)
        {

        }
        else if (view.getId() == R.id.history_tv_time)
        {

        }
        else if (view.getId() == R.id.history_back)
        {
            finish();
        }
        else if (view.getId() == R.id.history_rili)
        {
            CalendarDialog calendarDialog = new CalendarDialog(HistoryActivity.this);
            calendarDialog.show();
            calendarDialog.setDialogSize();
            calendarDialog.setOnRefreshListenser(new CalendarDialog.onRefreshListenser() {
                @Override
                public void OnRefesh(int year, int month) {
                    timeTv.setText(year + "年" + month + "月");
                    getHistoryData( year,  month);
                }
            });
        }
    }



}