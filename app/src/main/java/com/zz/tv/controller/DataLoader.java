package com.zz.tv.controller;

import com.zz.tv.data.ChannelInfo;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhangxiaohui on 2017/6/2.
 */

public class DataLoader {
    private static final String url = "";
    private LoadListener listener;
    private OkHttpClient client;

    public DataLoader() {
        client = new OkHttpClient();
    }

    public interface LoadListener {
        void onSuccess(List<ChannelInfo> list);
        void onFailed();
    }

    public void loadChannelList(LoadListener listener) {
        //TODO cancle last
        this.listener = listener;
        //client.
        // code request code here


    }

    private String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
