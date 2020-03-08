package com.yn.wj.commonmodule

import android.content.res.Configuration

class BaseAppLogic {

    protected var mApplication: BaseApplication? = null

    fun setApplication(application: BaseApplication) {
        mApplication = application
    }

    fun onCreate() {

    }

    fun onTerminate() {

    }

    fun onLowMemory() {

    }

    fun onTrimMemory() {

    }

    fun onConfigurationChanged(newConfig: Configuration?) {

    }
}