package me.opens.password_manager.listener;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;

import me.opens.password_manager.R;

public class KeyboardView extends FrameLayout implements View.OnClickListener {

    private EditText mPasswordField;

    public KeyboardView(Context context) {
        super(context);
        init();
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.keyboard, this);
        initViews();
    }

    private void initViews() {
        mPasswordField = $(R.id.password_field);
        $(R.id.t9_key_0).setOnClickListener(this);
        $(R.id.t9_key_1).setOnClickListener(this);
        $(R.id.t9_key_2).setOnClickListener(this);
        $(R.id.t9_key_3).setOnClickListener(this);
        $(R.id.t9_key_4).setOnClickListener(this);
        $(R.id.t9_key_5).setOnClickListener(this);
        $(R.id.t9_key_6).setOnClickListener(this);
        $(R.id.t9_key_7).setOnClickListener(this);
        $(R.id.t9_key_8).setOnClickListener(this);
        $(R.id.t9_key_9).setOnClickListener(this);
        $(R.id.t9_key_clear).setOnClickListener(this);
        $(R.id.t9_key_reveal).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // handle number button click
        if (v.getTag() != null && "number_button".equals(v.getTag())) {
            mPasswordField.append(((TextView) v).getText());
            return;
        }
        switch (v.getId()) {
            case R.id.t9_key_clear: { // handle clear button
                mPasswordField.setText(null);
            }
            break;
            case R.id.t9_key_reveal: { // handle backspace button
                // delete one character
                Editable editable = mPasswordField.getText();
                int charCount = editable.length();
                if (charCount > 0) {
                    editable.delete(charCount - 1, charCount);
                }
            }
            break;
        }
    }

    public String getInputText() {
        return mPasswordField.getText().toString();
    }

    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }
}
