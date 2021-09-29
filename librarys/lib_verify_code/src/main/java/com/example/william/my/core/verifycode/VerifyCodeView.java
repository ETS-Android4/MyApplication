package com.example.william.my.core.verifycode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
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

import com.example.william.my.core.verifycode.utils.SizeUtils;

public class VerifyCodeView extends RelativeLayout {

    private int mCodeNum;
    private Drawable mTextBgNormal, mTextBgFocus;
    private int mTextSize, mTextColor, mTextBgColor;
    private int mCodeWidth, mCodeHeight, mCodeMargin;

    private EditText mEditText;

    private TextView[] mTextViews;
    //当前选中的TextView位置，即光标所在位置
    private int currentFocusPosition = 0;

    private StringBuffer mStringBuffer;
    private String mInputContent;

    public VerifyCodeView(Context context) {
        this(context, null);
    }

    public VerifyCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeView);
            mCodeNum = a.getInteger(R.styleable.VerifyCodeView_code_number, 4);
            mCodeWidth = a.getDimensionPixelSize(R.styleable.VerifyCodeView_code_width, 48);
            mCodeHeight = a.getDimensionPixelSize(R.styleable.VerifyCodeView_code_height, 48);
            mCodeMargin = a.getDimensionPixelSize(R.styleable.VerifyCodeView_code_margin, 8);
            mTextSize = a.getDimensionPixelSize(R.styleable.VerifyCodeView_code_size, 14);
            mTextColor = a.getColor(R.styleable.VerifyCodeView_code_color, Color.WHITE);
            mTextBgColor = a.getColor(R.styleable.VerifyCodeView_code_bg_color, Color.GRAY);
            mTextBgNormal = a.getDrawable(R.styleable.VerifyCodeView_code_bg_normal);
            mTextBgFocus = a.getDrawable(R.styleable.VerifyCodeView_code_bg_focus);
            if (mTextBgFocus == null) {
                mTextBgFocus = mTextBgNormal;
            }
            a.recycle();
        }
    }

    private void initView(Context context) {
        LinearLayout mLinearLayout = new LinearLayout(context);
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTextViews = new TextView[mCodeNum];
        for (int i = 0; i < mCodeNum; i++) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(mTextSize);
            textView.setTextColor(mTextColor);
            textView.setBackgroundColor(mTextBgColor);
            LayoutParams params = new LayoutParams(SizeUtils.dp2px(mCodeWidth), SizeUtils.dp2px(mCodeHeight));
            params.setMarginStart(SizeUtils.dp2px(mCodeMargin));
            textView.setLayoutParams(params);
            mTextViews[i] = textView;
            mLinearLayout.addView(textView);
        }
        addView(mLinearLayout);
        mEditText = new EditText(context);
        mEditText.setBackground(null);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEditText.setCursorVisible(false);//将光标隐藏
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, SizeUtils.dp2px(mCodeHeight));
        mEditText.setLayoutParams(params);
        addView(mEditText);
        resetCursorPosition();
        setListener();
    }

    private void resetCursorPosition() {
//        for (int i = 0; i < mCodeNum; i++) {
//            TextView textView = mTextViews[i];
//            if (i == currentFocusPosition) {
//                //textView.setBackground(textDrawable);
//                textView.setBackgroundColor(mTextBgColor);
//            } else {
//                //textView.setBackground(textFocusedDrawable);
//                textView.setBackgroundColor(Color.BLUE);
//            }
//        }
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
                Log.e("TAG", "editable " + editable);
                //如果字符不为""时才进行操作
                if (!TextUtils.isEmpty(editable)) {
                    if (mStringBuffer.length() > mCodeNum-1) {
                        //当文本长度大于等于mCodeNum时editText置空
                        mEditText.setText("");
                        return;
                    } else {
                        //将文字添加到StringBuffer中
                        mStringBuffer.append(editable);
                        mEditText.setText("");//添加后将EditText置空

                        mInputContent = mStringBuffer.toString();
                        if (mStringBuffer.length() == 4) {
                            //文字长度位4  则调用完成输入的监听
                            if (inputCompleteListener != null) {
                                inputCompleteListener.inputComplete();
                            }
                        }
                    }

                    for (int i = 0; i < mStringBuffer.length(); i++) {
                        resetCursorPosition();
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
//        if (mStringBuffer.length() > 0) {
//            //删除相应位置的字符
//            mStringBuffer.delete((mCodeNum - 1), mCodeNum);
//            mCodeNum--;
//            mInputContent = mStringBuffer.toString();
//            mTextViews[mStringBuffer.length()].setText("");
//            if (inputCompleteListener != null) {
//                inputCompleteListener.deleteContent(true);//有删除就通知manger
//            }
//        }
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