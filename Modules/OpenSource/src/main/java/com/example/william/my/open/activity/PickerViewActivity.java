package com.example.william.my.open.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.william.my.module.activity.ResponseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.provider.ResourceUtilsService;
import com.example.william.my.open.data.CityPickerData;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * plist转json
 * http://json2plist.sinaapp.com/
 * 格式化
 * http://www.bejson.com/
 * <p>
 * https://github.com/Bigkoo/Android-PickerView
 */
@Route(path = ARouterPath.OpenSource.OpenSource_PickerView)
public class PickerViewActivity extends ResponseActivity {

    private boolean isLoaded, isSwitch;

    private List<CityPickerData> options1Items = new ArrayList<>();//所有省份数组
    private final List<List<String>> options2Items = new ArrayList<>();//所有城市数组
    private final List<List<List<String>>> options3Items = new ArrayList<>();//所有地区数组
    private final List<List<List<CityPickerData.CityListBean.AreaListBean>>> options3Items_ = new ArrayList<>();//所有地区数组


    @Override
    public void initView() {
        super.initView();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 子线程中解析省市区数据
                initJsonData();
                //initPlistData();
            }
        }).start();
    }

    @Override
    public void setOnClick() {
        super.setOnClick();
        if (isSwitch) {
            showTimePickerView();
        } else {
            if (isLoaded) {
                showOptionsPickerView();
            } else {
                Toast.makeText(PickerViewActivity.this, "Please waiting until the data is parsed", Toast.LENGTH_SHORT).show();
            }
        }
        isSwitch = !isSwitch;
    }

    private void initJsonData() {//解析数据
        /*
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         */
        ResourceUtilsService service = (ResourceUtilsService) ARouter.getInstance().build(ARouterPath.Service.ResourceUtilsService).navigation();

        String JsonData = service.getAssets("province.json");//获取assets目录下的json文件数据

        ArrayList<CityPickerData> cityPickerData = parseData(JsonData);

        /*
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = cityPickerData;

        for (int i = 0; i < cityPickerData.size(); i++) {
            List<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            List<List<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            List<List<CityPickerData.CityListBean.AreaListBean>> Province_AreaList_ = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int j = 0; j < cityPickerData.get(i).getCityList().size(); j++) {//遍历该省份的所有城市
                String CityName = cityPickerData.get(i).getCityList().get(j).getName();
                CityList.add(CityName);//添加城市

                List<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                List<CityPickerData.CityListBean.AreaListBean> City_AreaList_ = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (cityPickerData.get(i).getCityList().get(j).getAreaList() == null || cityPickerData.get(i).getCityList().get(j).getAreaList().size() == 0) {
                    City_AreaList.add("");
                    City_AreaList_.add(new CityPickerData.CityListBean.AreaListBean());
                } else {
                    for (int n = 0; n < cityPickerData.get(i).getCityList().get(j).getAreaList().size(); n++) {
                        City_AreaList.add(cityPickerData.get(i).getCityList().get(j).getAreaList().get(n).getName());
                    }
                    City_AreaList_.addAll(cityPickerData.get(i).getCityList().get(j).getAreaList());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                Province_AreaList_.add(City_AreaList_);//添加该省所有地区数据
            }

            /*
             * 添加城市数据
             */
            options2Items.add(CityList);

            /*
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
            options3Items_.add(Province_AreaList_);
        }

        isLoaded = true;
    }

    public ArrayList<CityPickerData> parseData(String result) {
        ArrayList<CityPickerData> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                CityPickerData entity = gson.fromJson(data.optJSONObject(i).toString(), CityPickerData.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    @SuppressWarnings("rawtypes")
    private void showOptionsPickerView() {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String options = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items_.get(options1).get(options2).get(options3).getName();
                showResponse(options);
            }
        })
                .setDecorView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content))//防止被虚拟按键遮挡
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        //noinspection unchecked
        pickerView.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pickerView.show();
    }

    private void showTimePickerView() {
        TimePickerView pickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                showResponse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();
        //dialog底部显示
        Dialog mDialog = pickerView.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
            params.leftMargin = 0;
            params.rightMargin = 0;
            pickerView.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
        pickerView.show();
    }
}
