package com.wkk.tally;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class timeSelectDialog extends Dialog implements View.OnClickListener {
    EditText hourEt, minEt;
    DatePicker datePicker;
    Button ensureBtn, cancelBtn;
    public interface OnEnsureListenser{
        public void onEnsure(String time, int year, int month, int day);
    }
    OnEnsureListenser onEnsureListenser;
    public void setOnEnsureListenser(OnEnsureListenser onEnsureListenser)
    {
        this.onEnsureListenser = onEnsureListenser;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public timeSelectDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.time_select);
        minEt = findViewById(R.id.dialog_time_et_min);
        hourEt = findViewById(R.id.dialog_time_et_hour);
        datePicker = findViewById(R.id.dialog_time_dp);
        ensureBtn = findViewById(R.id.dialog_time_btn_ensure);
        cancelBtn = findViewById(R.id.dialog_time_btn_cancel);
        ensureBtn.setOnClickListener(this::onClick);
        cancelBtn.setOnClickListener(this::onClick);
        hideDatePickerHeader();
        LocalDateTime now = LocalDateTime.now();
        hourEt.setText(String.valueOf(now.getHour() + 8));
        minEt.setText(String.valueOf(now.getMinute()));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_time_btn_ensure)
        {
            if (onEnsureListenser != null)
            {
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int dayOfMonth = datePicker.getDayOfMonth();
                String monthStr = String.valueOf(month);
                String dayStr = String.valueOf(dayOfMonth);
                monthStr = month >= 10 ? monthStr : "0" + monthStr;
                dayStr   = dayOfMonth >= 10 ? dayStr : "0" + dayStr;

                String hourStr = hourEt.getText().toString();
                String minStr = minEt.getText().toString();
                int hour = 0;
                if (!TextUtils.isEmpty(hourStr))
                {
                    hour = Integer.parseInt(hourStr);
                    hour = hour % 24;
                }
                int mintue = 0;

                if (!TextUtils.isEmpty(minStr))
                {
                    mintue = Integer.parseInt(minStr);
                    mintue = mintue % 60;
                }
                hourStr = String.valueOf(hour);
                minStr = String.valueOf(mintue);
                hourStr = hour >= 10 ? hourStr : "0" + hourStr;
                minStr   = mintue >= 10 ? minStr : "0" + minStr;
                String timeFormat = year + "年" + monthStr + "月" + dayStr + "日 " + hourStr + ":" + minStr;
                onEnsureListenser.onEnsure(timeFormat, year, month, dayOfMonth);
              //  Log.e("WKK", "onClick: " + timeFormat);
            }
            cancel();
        }
        else if (view.getId() == R.id.dialog_time_btn_cancel)
            cancel();
    }


    private void hideDatePickerHeader() {  // 隐藏日历选择器的头布局
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView == null) {
            return;
        }
         // 删除datepicker自带的一个头布局
        int headerId = getContext().getResources().getIdentifier("date_picker_header","id","android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
        return;
    }
}
