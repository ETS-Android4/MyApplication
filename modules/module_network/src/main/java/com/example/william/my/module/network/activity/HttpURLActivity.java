package com.example.william.my.module.network.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.library.helper.AppExecutorsHelper;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * setDoOutput() 和 setDoInput()：设置是否向HttpURLConnection输出与读取。
 * setDoOutput()默认是false，setDoInput()默认是true
 * setUseCaches()设置缓存，POST请求不能使用缓存
 * <p>
 * InputStream & OutputStream 输入流与输出流
 * 转换流
 * InputStreamReader & OutputStreamWriter 字符与节流转换
 * 缓冲流
 * 关闭了缓冲区对象实际也关闭了与缓冲区关联的流对象
 * BufferedReader & BufferedWriter 字符缓冲流
 * BufferedInputStream & BufferedOutputStream 字节缓冲流
 * 文件流
 * FileReader & FileWriter 字符文件流
 * FileInputStream & FileOutputStream 字节文件流
 */
@Route(path = ARouterPath.NetWork.NetWork_HttpURL)
public class HttpURLActivity extends BaseResponseActivity {

    private boolean b;

    @Override
    public void setOnClick() {
        super.setOnClick();
        AppExecutorsHelper.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                b = !b;
                if (b) {
                    get(Urls.URL_BANNER);
                } else {
                    String body = "username=" + "17778060027" +
                            "&password=" + "ww123456";
                    post(Urls.URL_LOGIN, body);
                }
            }
        });
    }

    private void get(String urlString) {
        try {
            // 1. 得到访问地址的URL
            URL url = new URL(urlString);
            // 2. 得到网络访问对象java.net.HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 3. 设置请求参数
            // 设置请求方式
            connection.setRequestMethod("GET");
            // 设置超时时间
            connection.setConnectTimeout(3000);
            // 建立连接
            connection.connect();

            // 4. 得到响应状态码的返回值 responseCode
            int code = connection.getResponseCode();
            // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
            StringBuilder msg = new StringBuilder();
            if (code == 200) {
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                // 循环从流中读取
                while ((line = reader.readLine()) != null) {
                    msg.append(line).append("\n");
                }
                // 关闭流
                reader.close();
            }
            // 6. 断开连接，释放资源
            connection.disconnect();
            showResponse(msg.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void post(String urlString, String params) {
        try {
            // 1. 获取访问地址URL
            URL url = new URL(urlString);
            // 2. 创建HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            /* 3. 设置请求参数等 */
            // 请求方式
            connection.setRequestMethod("POST");
            // 超时时间
            connection.setConnectTimeout(3000);
            // 设置使用标准编码格式编码参数：名-值对
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 建立连接
            connection.connect();

            /* 4. 处理输入输出 */
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(params.getBytes());
            outputStream.flush();
            outputStream.close();

            // 5. 得到响应状态码的返回值 responseCode
            int code = connection.getResponseCode();
            // 6. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
            StringBuilder msg = new StringBuilder();

            if (code == 200) {
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    msg.append(line).append("\n");
                }
                // 关闭流
                reader.close();
            }
            // 7. 断开连接
            connection.disconnect();
            showResponse(msg.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}