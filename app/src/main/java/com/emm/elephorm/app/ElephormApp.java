package com.emm.elephorm.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.emm.elephorm.utils.LruBitmapCache;

public class ElephormApp extends Application {

    public static final String TAG = ElephormApp.class.getSimpleName();
    private static ElephormApp sInstance;
    protected RequestQueue request;
    private ImageLoader mImageLoader;

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

        if (request == null) {
            request = Volley.newRequestQueue(getApplicationContext());
        }

        return request;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.request,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (request != null) {
            request.cancelAll(tag);
        }
    }
}