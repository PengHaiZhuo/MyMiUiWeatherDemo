package com.phz.mymiuiweatherdemo;

import android.app.Application;

/**
 * @author:haizhuo
 */
public class MyApplication extends Application {

    private static volatile MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication=this;
    }

    public static MyApplication getInstance(){
        if (myApplication == null) {
            synchronized (MyApplication.class) {
                if (myApplication == null) {
                    myApplication = new MyApplication();
                }
            }
        }
        return myApplication;
    }
}
