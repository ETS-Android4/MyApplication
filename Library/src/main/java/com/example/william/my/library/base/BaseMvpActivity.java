package com.example.william.my.library.base;

import androidx.lifecycle.Lifecycle;

import com.trello.lifecycle4.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle4.LifecycleProvider;

public class BaseMvpActivity extends BaseActivity {

    private LifecycleProvider<Lifecycle.Event> provider =
            AndroidLifecycle.createLifecycleProvider(this);
}
