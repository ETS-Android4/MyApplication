package com.example.william.my.module.network.nano;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.william.my.core.network.base.RxRetrofitConfig;
import com.example.william.my.core.network.utils.NetworkUtils;
import com.example.william.my.library.base.BaseApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class NanoServer extends NanoHTTPD {

    private static final String TAG = "Nano";

    public static final int DEFAULT_SERVER_PORT = 5566;

    public NanoServer() throws IOException {
        super(DEFAULT_SERVER_PORT);
        Log.e(TAG, "Running! Point your browsers to " +
                "http://" + NetworkUtils.getIPAddress(true) + ":" + DEFAULT_SERVER_PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {
        return parseRequest(session);
        //return newFixedLengthResponse("<html><body><h1>Hello server</h1></body></html>\n");
    }

    /**
     * 解析数据
     */
    private Response parseRequest(IHTTPSession session) {
        Response response;
        if (session.getMethod() == Method.GET) {
            response = parseGetRequest(session);
        } else if (session.getMethod() == Method.POST) {
            response = parsePostRequest(session);
        } else {
            response = responseForNotFound();
        }
        return response;
    }

    private Response parseGetRequest(IHTTPSession session) {
        String uri = session.getUri();
        String filename = uri.substring(1);

        boolean is_ascii = true;
        if (uri.equals("/")) {
            filename = "index.html";
        }
        if (uri.equals("/login")) {
            return newFixedLengthResponse("登录成功");
        }
        String mimeType;
        if (filename.contains(".html") || filename.contains(".htm")) {
            mimeType = "text/html";
            is_ascii = true;
        } else if (filename.contains(".js")) {
            mimeType = "text/javascript";
            is_ascii = true;
        } else if (filename.contains(".css")) {
            mimeType = "text/css";
            is_ascii = true;
        } else if (filename.contains(".gif")) {
            mimeType = "text/gif";
            is_ascii = false;
        } else if (filename.contains(".jpeg") || filename.contains(".jpg")) {
            mimeType = "text/jpeg";
            is_ascii = false;
        } else if (filename.contains(".png")) {
            mimeType = "image/png";
            is_ascii = false;
        } else if (filename.contains(".svg")) {
            mimeType = "image/svg+xml";
            is_ascii = false;
        } else {
            filename = "index.html";
            mimeType = "text/html";
        }
        if (is_ascii) {
            return loadHtml(filename, mimeType);
        } else {
            return loadOrder(filename, mimeType);
        }
    }

    private Response parsePostRequest(IHTTPSession session) {
        NanoHTTPD.Response response;
        if (session.getParameters() == null) {
            return responseParamsNotFound();
        } else {
            switch (session.getUri()) {
                case "/login":
                    //根据自己业务返回不同的需求
                    response = newFixedLengthResponse("登录成功");
                    break;
                case "/upload":
                    response = responseParamsNotFound();
                    Map<String, String> files = new HashMap<>();
                    try {
                        session.parseBody(files);
                        //{"file":"/data/user/0/com.example.william.my.module.network/cache/NanoHTTPD-8068393393718306101"}
                        Log.e(TAG, JSONObject.toJSONString(files));
                    } catch (IOException | ResponseException e) {
                        e.printStackTrace();
                    }
                    Map<String, List<String>> params = session.getParameters();
                    //{"file":["debug.txt"]}
                    Log.e(TAG, JSONObject.toJSONString(params));
                    for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                        final String key = entry.getKey();
                        final String fileName = entry.getValue().get(0);
                        if (key.contains("file")) {
                            final String filePath = files.get(key);
                            Log.e(TAG, "name: " + key + ", fileName: " + fileName + ", filePath: " + filePath);
                            saveFileToSDCard(fileName, filePath);
                            response = newFixedLengthResponse(Response.Status.OK, MIME_HTML, "{\"message\":\"上传成功\",\"status\":0}");
                        }
                    }
                    break;
                case "/download":
                    Log.e(TAG, "download");
                    response = responseForNotFound();
                    break;
                default:
                    response = responseForNotFound();
                    break;
            }
        }
        return response;
    }

    /**
     * 加载HTML文件
     */
    private Response loadHtml(String filename, String mimeType) {
        AssetManager assetManager = RxRetrofitConfig.getApp().getAssets();
        StringBuilder response = new StringBuilder();
        BufferedReader reader;
        InputStream isr;
        String line;
        try {
            isr = assetManager.open(filename, AssetManager.ACCESS_BUFFER);
            reader = new BufferedReader(new InputStreamReader(isr));
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.OK, mimeType, "");
        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, mimeType, response.toString());
    }

    private Response loadOrder(String filename, String mimeType) {
        AssetManager assetManager = RxRetrofitConfig.getApp().getAssets();
        InputStream isr;
        try {
            isr = assetManager.open(filename);
            return newFixedLengthResponse(Response.Status.OK, mimeType, isr, isr.available());
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.NOT_FOUND, mimeType, "");
        }
    }

    /**
     * 页面不存在
     */
    private Response responseForNotFound() {
        Response.Status status = Response.Status.NOT_FOUND;
        return newFixedLengthResponse(status, MIME_HTML, "NOT_FOUND");
    }

    /**
     * 请求参数错误
     */
    private Response responseParamsNotFound() {
        Response.Status status = Response.Status.BAD_REQUEST;
        return newFixedLengthResponse(status, MIME_HTML, "请求参数错误");
    }

    private void saveFileToSDCard(String fileName, String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = BaseApp.getApp().getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    FileInputStream is = new FileInputStream(filePath);
                    OutputStream fos = BaseApp.getApp().getContentResolver().openOutputStream(uri);

                    if (fos != null) {
                        int readSize;
                        byte[] buffer = new byte[1024];
                        while (-1 != (readSize = is.read(buffer))) {
                            fos.write(buffer, 0, readSize);
                        }
                        fos.close();
                    }
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
