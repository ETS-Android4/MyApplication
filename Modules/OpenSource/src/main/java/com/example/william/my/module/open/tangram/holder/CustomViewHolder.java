package com.example.william.my.module.open.tangram.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Keep;

import com.example.william.my.module.open.R;
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;

/**
 * 自定义 ViewHolder
 */
@Keep
public class CustomViewHolder extends ViewHolderCreator.ViewHolder {

    public TextView mTextView;

    public CustomViewHolder(Context context) {
        super(context);
    }

    @Override
    protected void onRootViewCreated(View view) {
        mTextView = view.findViewById(R.id.open_item_test_title);
    }
}
