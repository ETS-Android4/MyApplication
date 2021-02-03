package com.example.william.my.module.open.tangram.cell;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.example.william.my.module.open.R;
import com.tmall.wireless.tangram.structure.BaseCell;

import java.util.Locale;

@Keep
public class CustomHolderCell extends BaseCell<LinearLayout> {

    @Override
    public void bindView(@NonNull LinearLayout view) {
        super.bindView(view);
        if (pos % 2 == 0) {
            view.setBackgroundColor(0xff6200EE);
        } else {
            view.setBackgroundColor(0xff03DAC5);
        }
        TextView textView = view.findViewById(R.id.open_item_test_title);
        textView.setText(String.format(Locale.CHINA, "%s %d: %s", getClass().getSimpleName(), pos, optParam("text")));
    }
}
