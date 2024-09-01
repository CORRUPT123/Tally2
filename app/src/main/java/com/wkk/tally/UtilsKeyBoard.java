package com.wkk.tally;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.wkk.tally.R;


public class UtilsKeyBoard {

    private final Keyboard k1;  // 自定义的键盘
    private KeyboardView keyboardView;
    private EditText editText;
    public  interface OnEnsureListener
    {
        public void onEnsure();
    }
    OnEnsureListener onEnsure;
    public void setOnEnsureListener(OnEnsureListener onEnsure)
    {
        this.onEnsure = onEnsure;
    }
    public UtilsKeyBoard(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);  // 取消系统自动弹出键盘
        k1 = new Keyboard(this.editText.getContext(), R.xml.key);

        this.keyboardView.setKeyboard(k1);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener);
    }

    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();

            switch (primaryCode)
            {
                case Keyboard.KEYCODE_DELETE:
                    if (editable != null && editable.length() > 0)
                    {
                        if (start > 0 )
                        {
                            editable.delete(start - 1, start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL:
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE:
                    onEnsure.onEnsure();
                    break;
                default:
                    editable.insert(start, Character.toString((char)(primaryCode)));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };
    public void showKeyboard()
    {
        int vis = keyboardView.getVisibility();
        if (vis == View.INVISIBLE || vis == View.GONE)
            keyboardView.setVisibility(View.VISIBLE);
    }
}
