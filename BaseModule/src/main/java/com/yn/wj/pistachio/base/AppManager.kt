package com.yn.wj.pistachio.base

import android.app.Activity
import android.support.v4.app.Fragment
import java.lang.Exception
import java.util.*

/**
 * activity栈管理类
 */
class AppManager private constructor(){

    companion object {
        private var activityStack: Stack<Activity>? = null
        private var fragmentStack: Stack<Fragment>? = null
        val instance = ActivityManagerHolder.activityManager
    }

    private object ActivityManagerHolder{
        var activityManager = AppManager()
    }

    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityStack?.remove(activity)
    }

    fun hasActivity(): Boolean {
        return !(activityStack?.isEmpty()!!)
    }

    fun currentActivity(): Activity? {
        return activityStack?.lastElement()
    }

    fun finishActivity() {
        val activity: Activity? = activityStack?.lastElement()
        finishActivity(activity)
    }

    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            val isFinish: Boolean = activity.isFinishing
            if (!isFinish) {
                activity.finish()
            }
        }
    }

    fun finishActivity(cls: Class<*>) {
        if (activityStack != null) {
            for (activity: Activity in activityStack!!) {
                if (activity.javaClass == cls) {
                    finishActivity(activity)
                }
            }
        }
    }

    fun getActivity(cls: Class<*>): Activity?{
        if (activityStack != null) {
            for (activity in activityStack!!) {
                if (activity.javaClass == cls) {
                    return activity
                }
            }
        }
        return null
    }

    fun addFragment(fragment: Fragment) {
        if (fragmentStack == null) {
            fragmentStack = Stack()
        }
        fragmentStack?.add(fragment)
    }

    fun removeFragment(fragment: Fragment) {
        fragmentStack?.remove(fragment)
    }

    fun getCurrentFragment(): Fragment? {
        return fragmentStack?.lastElement()
    }

    fun finishAllActivity() {
        if (activityStack != null) {
            for (activity in activityStack!!){
                finishActivity(activity)
            }
        }
        activityStack?.clear()
    }

    fun appExit() {
        try {
            finishAllActivity()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}