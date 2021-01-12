package com.example.william.my.jet.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.william.my.jet.databinding.JetActivityBindBinding;
import com.example.william.my.jet.databinding.JetLayoutMergeBinding;

public class BindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.jet_activity_bind);

        JetActivityBindBinding bind = JetActivityBindBinding.inflate(getLayoutInflater());

        setContentView(bind.getRoot());

        bind.bindTextView.setText("ViewBinding");

        // 带 merge 标签的include不能使用ID，否则会找不到View报空指针异常
        JetLayoutMergeBinding mergeBind = JetLayoutMergeBinding.bind(bind.getRoot());

        mergeBind.mergeTextView.setText("ViewBinding Merge");
    }
}