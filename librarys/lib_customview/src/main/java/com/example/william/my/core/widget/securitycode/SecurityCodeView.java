package com.example.william.my.core.widget.securitycode;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.william.my.core.widget.R;
import com.example.william.my.core.widget.utils.SizeUtils;

public class SecurityCodeView extends RelativeLayout {

    private int count = 4;

    private EditText mEditText;
    private TextView[] mTextViews;
    private StringBuffer mStringBuffer;
    private String mInputContent;

    public SecurityCodeView(Context context) {
        this(context, null);
    }

    public SecurityCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecurityCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextViews = new TextView[count];
        RelativeLayout mRelativeLayout = new RelativeLayout(context);
        LinearLayout mLinearLayout = new LinearLayout(context);
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < count; i++) {
            TextView mTextView = new TextView(context);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            mTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            LayoutParams params = new LayoutParams(SizeUtils.dp2px(48), SizeUtils.dp2px(48));
            params.setMarginStart(SizeUtils.dp2px(8));
            mTextView.setLayoutParams(params);
            mTextViews[i] = mTextView;
            mLinearLayout.addView(mTextView);
        }
        mEditText = new EditText(context);
        mEditText.setBackground(null);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEditText.setCursorVisible(false);//将光标隐藏
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48));
        mEditText.setLayoutParams(params);
        mRelativeLayout.addView(mLinearLayout);
        mRelativeLayout.addView(mEditText);
        addView(mRelativeLayout);
        setListener();
    }

    private void setListener() {
        mStringBuffer = new StringBuffer();
        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //如果字符不为""时才进行操作
                if (!editable.toString().equals("")) {
                    if (mStringBuffer.length() > 3) {
                        //当文本长度大于3位时editText置空
                        mEditText.setText("");
                        return;
                    } else {
                        //将文字添加到StringBuffer中
                        mStringBuffer.append(editable);
                        mEditText.setText("");//添加后将EditText置空
                        count = mStringBuffer.length();
                        mInputContent = mStringBuffer.toString();
                        if (mStringBuffer.length() == 4) {
                            //文字长度位4  则调用完成输入的监听
                            if (inputCompleteListener != null) {
                                inputCompleteListener.inputComplete();
                            }
                        }
                    }

                    for (int i = 0; i < mStringBuffer.length(); i++) {
                        mTextViews[i].setText(String.valueOf(mInputContent.charAt(i)));
                    }
                }
            }
        });

        mEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    return onKeyDelete();
                }
                return false;
            }
        });
    }

    public boolean onKeyDelete() {
        if (count == 0) {
            count = 4;
            return true;
        }
        if (mStringBuffer.length() > 0) {
            //删除相应位置的字符
            mStringBuffer.delete((count - 1), count);
            count--;
            mInputContent = mStringBuffer.toString();
            mTextViews[mStringBuffer.length()].setText("");
            if (inputCompleteListener != null)
                inputCompleteListener.deleteContent(true);//有删除就通知manger

        }
        return false;
    }

    /**
     * 清空输入内容
     */
    public void clearEditText() {
        mStringBuffer.delete(0, mStringBuffer.length());
        mInputContent = mStringBuffer.toString();
        for (TextView textView : mTextViews) {
            textView.setText("");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener {
        void inputComplete();

        void deleteContent(boolean isDelete);
    }

    public String getEditContent() {
        return mInputContent;
    }

    public void setEditContent(String content) {
        mEditText.setText(content);
    }
}