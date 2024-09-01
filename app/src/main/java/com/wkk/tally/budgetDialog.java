package com.wkk.tally;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class budgetDialog extends Dialog implements View.OnClickListener {
    ImageView cancelIv;
    Button ensureBtn;
    EditText budgetEt;

    public interface OnClickListenser{
        public void enSure(float money);
    }

    OnClickListenser onClickListenser;

    public void setOnClickListenser (OnClickListenser onClickListenser)
    {
        this.onClickListenser = onClickListenser;
    }

    public budgetDialog(@NonNull Context context, Float currentBudget) {
        super(context);
        setContentView(R.layout.dialog_budeget);
        cancelIv = findViewById(R.id.dialog_budget_iv_error);
        ensureBtn = findViewById(R.id.dialog_budget_bt_ensure);
        budgetEt = findViewById(R.id.dialog_budget_et);
        cancelIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
        budgetEt.setText(String.valueOf(currentBudget));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_budget_bt_ensure)
        {
            String string = budgetEt.getText().toString();
            if (TextUtils.isEmpty(string))
            {
                Toast.makeText(getContext(), "输入数据不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            float v = Float.parseFloat(string);
            if (v < 0)
            {
                Toast.makeText(getContext(), "预算不能小于0！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (onClickListenser != null)
                onClickListenser.enSure(v);
            cancel();
            return;
        }
        else if (view.getId() == R.id.dialog_budget_iv_error)
        {
            cancel();
            return;
        }
    }
}
