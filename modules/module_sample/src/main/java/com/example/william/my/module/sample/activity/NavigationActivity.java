package com.example.william.my.module.sample.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.sample.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * New > Android Resource File，Resource type -> Navigation
 * <p>
 * android:name="NavHostFragment" -> NavHost，用于放置管理 destination 的空视图
 * app:defaultNavHost -> 是否拦截处理返回键
 * https://developer.android.google.cn/guide/navigation
 */
@Route(path = ARouterPath.Sample.Sample_Navigation)
public class NavigationActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);

        setContentView(R.layout.sample_activity_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
        //        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        //        .build();

        /*
         * -> fragment
         */
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        /*
         * -> FragmentContainerView
         */
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }
}