package com.example.william.my.module.router.item;

import android.os.Parcel;
import android.os.Parcelable;

public class RouterItem implements Parcelable {

    public String mRouterName;
    public String mRouterPath;

    public RouterItem(String mRouterName, String mRouterPath) {
        this.mRouterName = mRouterName;
        this.mRouterPath = mRouterPath;
    }

    protected RouterItem(Parcel in) {
        mRouterName = in.readString();
        mRouterPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRouterName);
        dest.writeString(mRouterPath);
    }

    public static final Creator<RouterItem> CREATOR = new Creator<RouterItem>() {
        @Override
        public RouterItem createFromParcel(Parcel in) {
            return new RouterItem(in);
        }

        @Override
        public RouterItem[] newArray(int size) {
            return new RouterItem[size];
        }
    };
}
