package com.yn.wj.pistachio.base

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 *
 *
 * @date Create time：2020/2/1
 */
abstract class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setApplication(this)
    }

    companion object {
        var sInstance: Application? = null

        @Synchronized
        private fun setApplication(application: Application) {
            sInstance = application
            application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{
                override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                    if (activity != null) {
                        AppManager.instance.addActivity(activity)
                    }
                }

                override fun onActivityResumed(activity: Activity?) {
                }

                override fun onActivityStarted(activity: Activity?) {
                }

                override fun onActivityPaused(activity: Activity?) {
                }

                override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                }

                override fun onActivityStopped(activity: Activity?) {
                }

                override fun onActivityDestroyed(activity: Activity?) {
                    if (activity != null) {
                        AppManager.instance.removeActivity(activity)
                    }
                }

            })
        }
    }
}