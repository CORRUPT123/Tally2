package com.wkk.tally;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.wkk.tally.db.DBManager;

import java.util.logging.Handler;

public class dialogMore extends Dialog {
    Button moreHistory;
    Button moreInfo;

    public dialogMore(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_more);
        setDialogSize();
        moreHistory = findViewById(R.id.dialog_more_btn_record);
        moreInfo = findViewById(R.id.dialog_more_btn_info);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManager.deleteALLAccount();
                Toast.makeText(getContext(), "DELETE DATA", Toast.LENGTH_SHORT).show();
            }
        });
        moreHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                getContext().startActivity(intent);
            }
        });
    }


    public void setDialogSize()
    {
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = d.getWidth();
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.white);
        window.setAttributes(wlp);
    }


}
