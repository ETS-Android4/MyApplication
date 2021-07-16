package com.netease.audioroom.demo.dialog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.SizeUtils;
import com.example.william.my.library.base.BaseDialogFragment;
import com.netease.audioroom.demo.R;

public class TopTipsDialogFragment extends BaseDialogFragment {

    private Style style;

    private View view;
    private TextView content;

    public interface IClickListener {

        void onClick();
    }

    private IClickListener clickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.request_dialog_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            style = getArguments().getParcelable(getTag());
        } else {
            dismiss();
        }
        view = inflater.inflate(R.layout.dialog_top_tips, container, false);
        // 设置宽度为屏宽、靠近屏幕底部。
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.TOP;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        content = view.findViewById(R.id.content);
        LinearLayout root = view.findViewById(R.id.root);
        if (!TextUtils.isEmpty(style.getTips())) {
            content.setText(Html.fromHtml(style.getTips()));
        }
        if (style.getTipIcon() != 0 && getContext() != null) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), style.tipIcon);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                content.setCompoundDrawables(drawable, null, null, null);
                content.setCompoundDrawablePadding(SizeUtils.dp2px(4));
            }
        }
        if (style.getBackground() != 0) {
            root.setBackgroundColor(getResources().getColor(style.getBackground()));
        }
        if (style.getTextColor() != 0) {
            content.setTextColor(getResources().getColor(style.getTextColor()));
        }
        if (getDialog() != null) {
            getDialog().setOnKeyListener((dialog, keyCode, event) ->
                    keyCode == KeyEvent.KEYCODE_BACK
            );
        }
        content.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick();
            }
        });
    }


    public void setClickListener(IClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public TextView getContent() {
        return content;
    }


    public static class Style implements Parcelable {

        private final String tips;

        @ColorInt
        private final int background;

        @DrawableRes
        private final int tipIcon;

        @ColorInt
        private final int textColor;

        public Style(String tips, int background, int tipIcon, int textColor) {
            this.tips = tips;
            this.background = background;
            this.tipIcon = tipIcon;
            this.textColor = textColor;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.tips);
            dest.writeInt(this.background);
            dest.writeInt(this.tipIcon);
            dest.writeInt(this.textColor);
        }

        protected Style(Parcel in) {
            this.tips = in.readString();
            this.background = in.readInt();
            this.tipIcon = in.readInt();
            this.textColor = in.readInt();
        }

        public final Creator<Style> CREATOR = new Creator<Style>() {

            @Override
            public Style createFromParcel(Parcel source) {
                return new Style(source);
            }

            @Override
            public Style[] newArray(int size) {
                return new Style[size];
            }
        };

        public String getTips() {
            return tips;
        }

        public int getBackground() {
            return background;
        }

        public int getTipIcon() {
            return tipIcon;
        }

        public int getTextColor() {
            return textColor;
        }
    }
}
