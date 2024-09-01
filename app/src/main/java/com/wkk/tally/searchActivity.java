package com.wkk.tally;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.util.List;
public class searchActivity extends AppCompatActivity {
    final  String TAG = "WKK";
    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;
    ImageView backIv, searchIv;
    List<AccountBean> mDatas;
    AccountAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        searchLv.setAdapter(adapter);
        searchLv.setEmptyView(emptyTv); // 如果此时的ListView显示为空的话就将其控件显示为emptyTv
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = searchEt.getText().toString().trim();
                if (TextUtils.isEmpty(string))
                {
                    Toast.makeText(searchActivity.this, "请输出查询条件", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<AccountBean> list = DBManager.queryFromAccountTbByString(string);
                mDatas.clear();
                mDatas.addAll(list);
                adapter.notifyDataSetChanged();
            }
        });
        searchLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AccountBean clickBean = mDatas.get(i);
                showDeleteDialog(clickBean);
                Log.e(TAG, "onItemLongClick: "  + "item长按点击事件" );
                return false;
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
                        searchIv.callOnClick();   // 删除记录后应该刷新，但是onResume不行，所以调用重新搜索的操作来刷新界面
                    }
                });
        builder.show();
    }
    private void initView() {
        searchEt = findViewById(R.id.search_et);
        searchLv = findViewById(R.id.search_lv);
        emptyTv = findViewById(R.id.search_tv_empty);
        backIv = findViewById(R.id.search_iv_back);
        searchIv = findViewById(R.id.search_iv_sh);
    }
}