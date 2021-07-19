package com.example.william.my.core.widget.securitycode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.example.william.my.core.widget.R;
import com.example.william.my.core.widget.utils.SizeUtils;

/**
 * Android验证码View
 */
public class VerificationCodeView extends RelativeLayout {

    private Context context;
    //当前验证码View用来展示验证码的TextView数组，数组个数由codeNum决定
    private TextView[] textViews;
    //用来输入验证码的EditText输入框，输入框会跟随输入个数移动，显示或者隐藏光标等
    private WiseEditText editText;
    //当前验证码View展示验证码个数
    private int codeNum;
    //每个TextView的宽度
    private float codeWidth;
    //每个TextView的高度
    private float codeHeight;
    //字体颜色
    private int textColor;
    //字体大小
    private int textSize;
    //每个单独验证码的背景
    private Drawable textDrawable;
    //验证码选中时的背景
    private Drawable textFocusedDrawable;
    //验证码之间间隔
    private float dividerWidth = 0;
    //对EditText输入进行监听
    private TextWatcher watcher;
    //监听删除键和enter键
    private OnKeyListener onKeyListener;
    // typeface
    private Typeface mTypeface;

    private OnInputFinishedListener onInputFinishedListener;

    //当前选中的TextView位置，即光标所在位置
    private int currentFocusPosition = 0;

    public VerificationCodeView(Context context) {
        this(context, null);
    }

    public VerificationCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化View
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView, defStyleAttr, 0);
        codeNum = array.getInteger(R.styleable.VerificationCodeView_vcv_code_number, 4);
        codeWidth = array.getDimensionPixelSize(R.styleable.VerificationCodeView_vcv_code_width, SizeUtils.dp2px(50));
        codeHeight = array.getDimensionPixelSize(R.styleable.VerificationCodeView_vcv_code_height, SizeUtils.dp2px(50));
        textColor = array.getColor(R.styleable.VerificationCodeView_vcv_code_color, getResources().getColor(R.color.colorPrimary));
        textSize = array.getDimensionPixelSize(R.styleable.VerificationCodeView_vcv_code_size, SizeUtils.dp2px(20));
        textDrawable = array.getDrawable(R.styleable.VerificationCodeView_vcv_code_bg_normal);
        textFocusedDrawable = array.getDrawable(R.styleable.VerificationCodeView_vcv_code_bg_focus);
        array.recycle();
        if (textDrawable == null) {
            //textDrawable = getResources().getDrawable(R.drawable.shape_empty_cccccc_point5);
        }
        if (textFocusedDrawable == null) {
            textFocusedDrawable = textDrawable;
        }
        this.context = context;
        initView(context);
        initListener();
        resetCursorPosition();
    }

    /**
     * 初始化各View并加入当前View中
     */
    private void initView(Context context) {
        //初始化TextView数组
        textViews = new TextView[codeNum];
        for (int i = 0; i < codeNum; i++) {
            //循环加入数组中，设置TextView字体大小和颜色，并将TextView依次加入LinearLayout
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(textColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            if (mTypeface != null) {
                textView.setTypeface(mTypeface);
            }
            textViews[i] = textView;
            this.addView(textView);
            LayoutParams params = (LayoutParams) textView.getLayoutParams();
            params.addRule(CENTER_VERTICAL);
            params.width = (int) codeWidth;
            params.height = (int) codeHeight;
        }
        //初始化EditText，设置背景色为透明，获取焦点，设置光标颜色，设置输入类型等
        editText = new WiseEditText(context);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//        setCursorRes(R.drawable.cursor);
        addView(editText, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * @param type {@link InputType}
     */
    public void setInputType(int type) {
        if (editText != null) {
            editText.setInputType(type);
        }
        if (textViews != null) {
            for (TextView textView : textViews) {
                textView.setInputType(type);
            }
        }
    }

    public void requestEditTextFocus() {
        if (editText != null) {
            editText.requestFocus();
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setSelection(0);
            editText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        //UtilsManager.showKeyboard(editText);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 200);

        }
    }

    /**
     * 监听EditText输入字符，监听键盘删除键
     */
    private void initListener() {
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                if (!TextUtils.isEmpty(content)) {
                    if (content.length() == 1) {
                        setText(content);
                    }
                    editText.setText("");
                }
            }
        };
        editText.addTextChangedListener(watcher);

        onKeyListener = new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    deleteCode();
                    return true;
                }
                return false;
            }
        };
        editText.setSoftKeyListener(onKeyListener);
    }

    /**
     * 重设EditText的光标位置，以及选中TextView的边框颜色
     */
    private void resetCursorPosition() {
        for (int i = 0; i < codeNum; i++) {
            TextView textView = textViews[i];
            if (i == currentFocusPosition) {
                textView.setBackground(textDrawable);
            } else {
                textView.setBackground(textFocusedDrawable);
            }
        }
        if (codeNum > 1) {
            if (onInputFinishedListener != null) {
                onInputFinishedListener.onInput(currentFocusPosition);
            }
            if (currentFocusPosition < codeNum) {
                //字数小于总数，设置EditText的leftPadding，造成光标移动的错觉
                editText.setCursorVisible(true);
                float leftPadding = codeWidth / 2 + currentFocusPosition * codeWidth + currentFocusPosition * dividerWidth;
                editText.setPadding((int) leftPadding, 0, 0, 0);
            } else {
                //字数大于总数，隐藏光标
                editText.setCursorVisible(false);
                if (onInputFinishedListener != null) {
                    onInputFinishedListener.onFinished(getContent());
                }
            }
        }
    }

    public void setInputFinishedListener(OnInputFinishedListener onInputFinishedListener) {
        this.onInputFinishedListener = onInputFinishedListener;
    }

    /**
     * 删除键按下
     */
    private void deleteCode() {
        if (currentFocusPosition == 0) {
            //当前光标位置在0，直接返回
            return;
        } else {
            //当前光标不为0，当前光标位置-1，将当前光标位置TextView置为""，重设光标位置
            currentFocusPosition--;
            textViews[currentFocusPosition].setText("");
            resetCursorPosition();
        }
    }

    /**
     * onMeasure后获取到测量的控件宽度，计算出每个Code之间的间隔
     */
    private void layoutTextView() {
        if (dividerWidth != 0) {
            return;
        }
        //获取控件剩余宽度
        float availableWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        if (codeNum > 1) {
            //计算每个Code之间的间距
            dividerWidth = (availableWidth - codeWidth * codeNum) / (codeNum - 1);
            for (int i = 1; i < codeNum; i++) {
                float leftMargin = codeWidth * i + dividerWidth * i;
                LayoutParams params1 = (LayoutParams) textViews[i].getLayoutParams();
                params1.leftMargin = (int) leftMargin;
            }
        }

        //设置EditText宽度从第一个Code左侧到最后一个Code右侧，设置高度为Code高度
        //设置EditText为纵向居中
        editText.setWidth((int) availableWidth);
        editText.setHeight((int) codeHeight);
        LayoutParams params = (LayoutParams) editText.getLayoutParams();
        params.addRule(CENTER_VERTICAL);
    }

    /**
     * 拦截到EditText输入字符，发送给该方法进行处理
     *
     * @param s
     */
    private void setText(String s) {
        if (currentFocusPosition >= codeNum) {
            //光标已经隐藏，直接返回
            return;
        }
        //设置字符给当前光标位置的TextView，光标位置后移，重设光标状态
        textViews[currentFocusPosition].setText(s);
        currentFocusPosition++;
        resetCursorPosition();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            //若高度是wrap_content，则设置为50dp
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((SizeUtils.dp2px(80)), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //获取到宽高，可以对TextView进行摆放
        layoutTextView();
    }

    /**
     * 暴露公共方法，设置光标颜色
     *
     * @param drawableRes
     */
    public void setCursorRes(@DrawableRes int drawableRes) {
        try {
            java.lang.reflect.Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, drawableRes);
        } catch (Exception e) {
        }
    }

    public void setTypeface(Typeface typeface) {
        mTypeface = typeface;
        if (textViews != null && textViews.length > 0) {
            for (TextView textView : textViews) {
                textView.setTypeface(mTypeface);
            }
            if (editText != null) {
                editText.setTypeface(mTypeface);
            }
        }
    }

    /**
     * 获取输入的验证码
     *
     * @return
     */
    public String getContent() {
        StringBuilder builder = new StringBuilder();
        for (TextView tv : textViews) {
            builder.append(tv.getText());
        }
        return builder.toString();
    }

    /**
     * 判断是否验证码输入完毕
     *
     * @return
     */
    public boolean isFinish() {
        for (TextView tv : textViews) {
            if (TextUtils.isEmpty(tv.getText())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 清除已输入验证码
     */
    public void clear() {
        for (TextView tv : textViews) {
            tv.setText("");
        }
        currentFocusPosition = 0;
        resetCursorPosition();
    }

    public interface OnInputFinishedListener {
        void onFinished(String content);

        void onInput(int currentPos);
    }
}