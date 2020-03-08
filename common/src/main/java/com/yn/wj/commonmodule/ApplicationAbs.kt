package com.yn.wj.commonmodule

import android.app.Application
import android.content.res.Configuration

abstract class ApplicationAbs : Application() {

    private val logicList: ArrayList<Class<out BaseAppLogic>> = ArrayList()
    private val logicClassList: ArrayList<BaseAppLogic> = ArrayList()

    override fun onCreate() {
        super.onCreate()
        initLogic()
        logicCreate()
    }

    protected abstract fun initLogic()

    protected fun registerApplicationLogic(logicClass: Class<out BaseAppLogic>) {
        logicList.add(logicClass)
    }

    private fun logicCreate() {
        for (logicClass in logicList) {
            try {
                val appLogic: BaseAppLogic = logicClass.newInstance()
                logicClassList.add(appLogic)
                appLogic.setApplication(this)
                appLogic.onCreate()
            } catch (e: InstantiationError) {
                e.printStackTrace()
            } catch (e: IllegalAccessError) {
                e.printStackTrace()
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        for (logic in logicClassList) {
            logic.onTerminate()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        for (logic in logicClassList) {
            logic.onLowMemory()
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        for (logic in logicClassList) {
            logic.onTrimMemory()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        for (logic in logicClassList) {
            logic.onConfigurationChanged(newConfig)
        }
    }
}
