package com.emm.elephorm.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ElephormApp extends Application {
    private static ElephormApp sInstance;
    protected RequestQueue request;

    @Override
    public void onCreate() {
        super.onCreate();

        request = Volley.newRequestQueue(this);

        sInstance = this;
    }

    public synchronized static ElephormApp getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return request;
    }
}