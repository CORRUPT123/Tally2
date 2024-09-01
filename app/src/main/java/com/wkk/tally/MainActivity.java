package com.wkk.tally;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.wkk.tally.adapter.AccountAdapter;
import com.wkk.tally.db.AccountBean;
import com.wkk.tally.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.wkk.tally.searchActivity;
public class MainActivity extends AppCompatActivity {
    final  String TAG = "WKK";
    ListView todayLv;
    ImageButton moreBtn;
    List<AccountBean> mDatas;
    public AccountAdapter accountAdapter;
    int year, month, day;
    // 头布局相关控件
    private View headerView;
    TextView topOutTv, topInTv, toBudgetTv, topConTv;
    ImageView topShowIv, searchIv;
    Boolean hide_flag = true;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        todayLv = findViewById(R.id.main_lv);
        addLVViewHeader();
        mDatas = new ArrayList<>() ;
        accountAdapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(accountAdapter);
        year  = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        day   = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        topShowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hide_flag == true)
                {
                    hide_flag = false;
                    topShowIv.setImageResource(R.mipmap.ih_hide);
                }
                else
                {
                    hide_flag = true;
                    topShowIv.setImageResource(R.mipmap.ih_show);
                }
                onResume();
            }
        });
        toBudgetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = getSharedPreferences("budget", MODE_PRIVATE);
                float budget = preferences.getFloat("Budget", 1000);
                budgetDialog budgetDialog = new budgetDialog(MainActivity.this, budget);
                budgetDialog.show();
                budgetDialog.setOnClickListenser(new budgetDialog.OnClickListenser() {
                    @Override
                    public void enSure(float money) {
                        preferences = getSharedPreferences("budget", MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putFloat("Budget", money);
                        edit.commit();
                        onResume();
                    }
                });
            }
        });
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) // 如果点击的是头布局直接返回不用处理。
                {
                    return false;
                }
                AccountBean clickBean = mDatas.get(i - 1);
                showDeleteDialog(clickBean);
                Log.e(TAG, "onItemLongClick: "  + "item长按点击事件" );
                return false;
            }
        });
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, searchActivity.class);
                startActivity(intent);
            }
        });
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMore dialogmore = new dialogMore(MainActivity.this);
                dialogmore.show();
            }
        });
    }

    private void showDeleteDialog(AccountBean accountBean)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息")
                .setMessage("确定删除这条记录吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 调用数据库管理类中执行删除的操作。
                        DBManager.deleteItemFromAccountTbByID(accountBean.getId());
                        onResume();
                    }
                });
        builder.show();
    }
    private void addLVViewHeader() {
        headerView = getLayoutInflater().inflate(R.layout.item_main_lv_top, null);
        todayLv.addHeaderView(headerView);
        topOutTv = headerView.findViewById(R.id.item_main_lv_top_out);
        topInTv = headerView.findViewById(R.id.item_main_lv_top_in);
        toBudgetTv = headerView.findViewById(R.id.item_main_lv_top_budget);
        topConTv = headerView.findViewById(R.id.item_main_lv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_main_lv_top_hide);
        searchIv = findViewById(R.id.main_iv_search);
        moreBtn = findViewById(R.id.main_btn_more);
    }

    @Override
    public void onResume() {  // 比如添加完一条记录回到主界面时，主界面的内容需要重新刷新一下，onResume函数就当前界面重新获得焦点时会执行一下。
        super.onResume();
        List<AccountBean> list = DBManager.getAccountListOnDayFromAccountTb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        accountAdapter.notifyDataSetChanged();
        reSetTopViewData();
    }

    private void reSetTopViewData() {
        if (hide_flag == true)  // 显示数字
        {
            float inComeOneDay = DBManager.getTotalOnDay(year, month, day, 1);
            float outComeOneDay = DBManager.getTotalOnDay(year, month, day, 0);
            String infoOneDay = "今日支出 ￥ " + outComeOneDay + " 收入 ￥ " + inComeOneDay;
            topConTv.setText(infoOneDay);
            float inComeOneMonth = DBManager.getTotalOneMonth(year, month, 1);
            float outComeOneMonth = DBManager.getTotalOneMonth(year, month, 0);
            topInTv.setText("￥ " + inComeOneMonth);
            topOutTv.setText("￥ " + outComeOneMonth);
            preferences = getSharedPreferences("budget", MODE_PRIVATE);
            float budget = preferences.getFloat("Budget", 1000);
            toBudgetTv.setText("￥ " + (budget - outComeOneMonth));
        }
        else  // 不显示数字
        {
            String infoOneDay = "今日支出 ￥ " + "***" + " 收入 ￥ " + "***";
            topInTv.setText("￥ " + "***");
            topOutTv.setText("￥ " + "***");
         //   toBudgetTv.setText("￥ " + "***");
        }

    }

    public void onClick(View view) {
          Intent intent = new Intent(MainActivity.this, RecordActivity.class);
          startActivity(intent);
    }
}