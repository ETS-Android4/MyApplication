package com.example.william.my.module.opensource.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.opensource.R;
import com.example.william.my.module.router.ARouterPath;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

/**
 * 添加 setTheme(R.style.open_CityPickerTheme);
 * 或 android:theme="@style/DefaultCityPickerTheme"
 * <p>
 * https://github.com/zaaach/CityPicker
 */
@Route(path = ARouterPath.OpenSource.OpenSource_CityPicker)
public class CityPickerActivity extends BaseResponseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.open_CityPickerTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        CityPicker.from(CityPickerActivity.this)
                .enableAnimation(true)//启用动画
                //.setAnimationStyle(anim)//自定义动画
                .setLocatedCity(null)//定位城市
                //.setHotCities(hotCities)//指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        showResponse(String.format("%s，%s", data.getName(), data.getCode()));
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(CityPickerActivity.this, "取消选择", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLocate() {
                        //开始定位，这里模拟一下定位
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //定位完成之后更新数据
                                CityPicker.from(CityPickerActivity.this).locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                            }
                        });
                    }
                })
                .show();
    }
}
