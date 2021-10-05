package com.example.singleapp;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.mmkv.MMKV;

public class SceneApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
        Fresco.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
