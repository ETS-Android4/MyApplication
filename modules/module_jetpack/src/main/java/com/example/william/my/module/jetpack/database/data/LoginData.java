package com.example.william.my.module.jetpack.database.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LoginData implements Parcelable {

    @SerializedName("token")
    private String token;

    @SerializedName("data")
    private UserData user;

    /**
     * 从序列化后的对象中创建原始对象
     */
    protected LoginData(Parcel in) {
        token = in.readString();
        //user是另一个序列化对象，此方法序列需要传递当前线程的上下文类加载器，否则会报无法找到类的错误
        user = in.readParcelable(UserData.class.getClassLoader());
    }

    public static final Creator<LoginData> CREATOR = new Creator<LoginData>() {

        /**
         * 实现从Parcel容器中读取传递数据值,封装成Parcelable对象返回逻辑层
         */
        @Override
        public LoginData createFromParcel(Parcel in) {
            return new LoginData(in);
        }

        /**
         * 创建一个类型为T，长度为size的数组，供外部类反序列化本类数组使用
         */
        @Override
        public LoginData[] newArray(int size) {
            return new LoginData[size];
        }
    };

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    /**
     * 当前对象的内容描述,一般返回0即可
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将当前对象写入序列化结构中
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
    }

    public static class UserData implements Parcelable {

        @SerializedName("id")
        private String id;

        @SerializedName("nickname")
        private String nickname;

        protected UserData(Parcel in) {
            id = in.readString();
            nickname = in.readString();
        }

        public static final Creator<UserData> CREATOR = new Creator<UserData>() {
            @Override
            public UserData createFromParcel(Parcel in) {
                return new UserData(in);
            }

            @Override
            public UserData[] newArray(int size) {
                return new UserData[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(nickname);
        }
    }
}
