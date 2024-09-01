package com.wkk.tally;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BeiZhuDialog extends Dialog implements View.OnClickListener {
    EditText et;
    Button ensure, cancle;
    OnEnsureListener onEnsureListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beizhu_layout);
        et = findViewById(R.id.dialog_beizhu_et);
        cancle = findViewById(R.id.cancleBtn);
        ensure = findViewById(R.id.ensureBtn);
        cancle.setOnClickListener(this);
        ensure.setOnClickListener(this);
    }

    public BeiZhuDialog(@NonNull Context context) {
        super(context);
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public interface OnEnsureListener{
        public void onEnsure();
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cancleBtn)
            cancel();
        else if (view.getId() == R.id.ensureBtn)
        {
            if (onEnsureListener != null)
                onEnsureListener.onEnsure();
        }
    }


    public String getETtext()
    {
        return et.getText().toString().trim();
    }

    public void setDialogSize() // 设置dialog与屏幕尺寸一致
    {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        Display defaultDisplay = window.getWindowManager().getDefaultDisplay();
        attributes.width = (int) (defaultDisplay.getWidth());
        attributes.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
        handler.sendEmptyMessageDelayed(1, 100);
    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
