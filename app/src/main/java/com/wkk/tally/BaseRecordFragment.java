package com.wkk.tally;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wkk.tally.db.DBManager;
import com.wkk.tally.db.TypeBean;
import com.wkk.tally.db.AccountBean;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class  BaseRecordFragment extends Fragment implements View.OnClickListener{
    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv, beizhuTv, timeTv;
    GridView typeGv;
    List<TypeBean> typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean; // 将当前页面需要保存的数据包装成一个类

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);
    }

    public BaseRecordFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_out_frag, container, false);
        init(view);
        setInitTime();
        loadDataToGV();
        setGVLister();
        return view;
    }

    private void setInitTime() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        LocalDateTime now = LocalDateTime.now();
        String time = sdf.format(date) + " " + String.valueOf(now.getHour() + 8) + ":" + String.valueOf(now.getMinute());
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar instance = Calendar.getInstance();
        accountBean.setYear(instance.get(Calendar.YEAR));
        accountBean.setMonth(instance.get(Calendar.MONTH));
        accountBean.setDay(instance.get(Calendar.DAY_OF_MONTH));

    }

    private void setGVLister() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated();
                typeTv.setText(typeList.get(position).getTypename());
                typeIv.setImageResource(typeList.get(position).getSimageId());

                accountBean.setTypename(typeList.get(position).getTypename());  // 把当前选中的类型名称和图片赋值给AcountBean类
                accountBean.setsImageId(typeList.get(position).getSimageId());
            }
        });
    }

    public void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
        List<TypeBean> outList = DBManager.getTypeList(0);
        typeList.addAll(outList);
        adapter.notifyDataSetChanged();
    }

    private void init(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);

        timeTv.setOnClickListener(this);
        beizhuTv.setOnClickListener(this);
        UtilsKeyBoard utilsKeyBoard = new UtilsKeyBoard(keyboardView, moneyEt);
        // 显示键盘
        utilsKeyBoard.showKeyboard();
        //  设置键盘的接口，然后设置键盘点击确认时的操作
        utilsKeyBoard.setOnEnsureListener(new UtilsKeyBoard.OnEnsureListener() {
            @Override
            public void onEnsure() {
                // 点击确认按钮
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0"))
                {
                    getActivity().finish();
                    return;
                }

                float v = Float.parseFloat(moneyStr);
                accountBean.setMoney(v);
                accountBean.setBeiZhu(beizhuTv.getText().toString());
                saveAcountToDB();
                getActivity().finish();
                // 数据库记录当前信息
                // 返回上一级
            }
        });
    }

    public abstract void saveAcountToDB();

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.frag_record_tv_time)
        {
            showTimeDialog();
        }
        else if (view.getId() == R.id.frag_record_tv_beizhu)
        {
            showBeiZhuDialog();
            return;
        }
    }

    private void showTimeDialog() {
        timeSelectDialog timeSelectDialog = new timeSelectDialog(getContext());
        timeSelectDialog.show();
        timeSelectDialog.setOnEnsureListenser(new timeSelectDialog.OnEnsureListenser() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    public void showBeiZhuDialog() {
        BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getETtext();
                if (!TextUtils.isEmpty(msg))
                {
                    beizhuTv.setText(msg);
                    accountBean.setBeiZhu(msg);
                }
                dialog.cancel();
            }
        });
    }
}