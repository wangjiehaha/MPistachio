package com.yn.wj.commonmodule

import android.content.res.Configuration

open class BaseAppLogic {

    protected lateinit var mApplication: ApplicationAbs

    fun setApplication(application: ApplicationAbs) {
        mApplication = application
    }

    open fun onCreate() {

    }

    open fun onTerminate() {

    }

    open fun onLowMemory() {

    }

    open fun onTrimMemory() {

    }

    open fun onConfigurationChanged(newConfig: Configuration?) {

    }
}