package com.yn.wj.pistachio.router

import com.alibaba.android.arouter.launcher.ARouter
import com.yn.wj.commonmodule.BaseAppLogic
import com.yn.wj.pistachio.BuildConfig

class ARouterLogic: BaseAppLogic() {

    private val isDebugARouter = BuildConfig.DEBUG

    override fun onCreate() {
        super.onCreate()
        if (isDebugARouter) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(mApplication)
    }
}