package com.example.william.my.module.network.activity;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.william.my.bean.base.Urls;
import com.example.william.my.module.activity.BaseResponseActivity;
import com.example.william.my.module.router.ARouterPath;

/**
 * https://developer.android.google.cn/training/volley/index.html
 */
@Route(path = ARouterPath.NetWork.NetWork_Volley)
public class VolleyActivity extends BaseResponseActivity {

    @Override
    public void setOnClick() {
        super.setOnClick();
        volley();
    }

    private void volley() {
        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Get a RequestQueue
        //RequestQueue requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                showResponse("Response is: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showResponse("That didn't work!");
            }
        });


//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_LOGIN, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // TODO: Handle error
//            }
//        });

        // Add the request to the RequestQueue.
        //requestQueue.add(stringRequest);

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        //VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
