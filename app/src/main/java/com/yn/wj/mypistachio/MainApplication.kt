package com.yn.wj.mypistachio

import android.app.Application
import com.yn.wj.pistachio.base.BaseApplication
import java.lang.NullPointerException

/**
 *
 *
 * @date Create time：2020/2/1
 */
class MainApplication: BaseApplication(){
    companion object {
        fun getContext(): Application {
            if (sInstance == null) {
                throw NullPointerException() as Throwable
            }
            return sInstance!!
        }
    }

    override fun initLogic() {

    }
}