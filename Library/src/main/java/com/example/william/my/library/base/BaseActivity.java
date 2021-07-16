package com.example.william.my.library.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.william.my.library.R;

public class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Basics_WindowAnimTheme_Slide);
    }

    protected int setStatusBarColor() {
        return android.R.color.transparent;
    }

    //@Override
    //public void finish() {
    //    super.finish();
    //    overridePendingTransition(R.anim.basic_anim_bottom_in, R.anim.basic_anim_bottom_out);
    //}
}
