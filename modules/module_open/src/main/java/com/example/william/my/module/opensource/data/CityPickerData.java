package com.example.william.my.module.opensource.data;

import androidx.annotation.Keep;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

@Keep
public class CityPickerData implements IPickerViewData {

    private String id;
    private String name;
    private List<CityListBean> cityList;

    /**
     * 实现IPickerViewData接口，显示在PickerView上面的字符串
     */
    @Override
    public String getPickerViewText() {
        return this.name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityListBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityListBean> cityList) {
        this.cityList = cityList;
    }

    @Keep
    public static class CityListBean {

        private String id;
        private String name;
        private List<AreaListBean> areaList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<AreaListBean> getAreaList() {
            return areaList;
        }

        public void setAreaList(List<AreaListBean> areaList) {
            this.areaList = areaList;
        }

        @Keep
        public static class AreaListBean {

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}