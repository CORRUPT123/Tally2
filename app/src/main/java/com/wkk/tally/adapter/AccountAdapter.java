package com.wkk.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wkk.tally.R;
import com.wkk.tally.db.AccountBean;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {
    Context context;
    List<AccountBean> mDatas;
    LayoutInflater inflater;
    int year, month, day;
    public AccountAdapter(Context context, List<AccountBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
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
        ViewHolder holder = null;
        if (view == null)
        {
            view = inflater.inflate(R.layout.item_main_lv, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else
            holder = (ViewHolder) view.getTag();
        AccountBean accountBean = mDatas.get(i);
        holder.typeIv.setImageResource(accountBean.getsImageId());
        holder.typeTv.setText(accountBean.getTypename());
        holder.beizhuTv.setText(accountBean.getBeiZhu());
        holder.moneyTv.setText("￥ " + (accountBean.getMoney()));
        if (accountBean.getYear() == year && accountBean.getMonth() == month && accountBean.getDay() == day)
        {
            String time = accountBean.getTime().split(" ")[1];
            holder.timeTv.setText("今天 " + time);
        }
        else
            holder.timeTv.setText(accountBean.getTime());

        return view;
    }


    class ViewHolder{
        ImageView typeIv;
        TextView typeTv, beizhuTv, timeTv, moneyTv;
        public ViewHolder(View view)
        {
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_tv_title);
            beizhuTv = view.findViewById(R.id.item_mainlv_tv_beizhu);
            timeTv = view.findViewById(R.id.item_mainlv_tv_time);
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);
        }
    }


}
