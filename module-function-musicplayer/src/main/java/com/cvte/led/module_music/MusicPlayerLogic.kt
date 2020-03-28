package com.cvte.led.module_music

import com.yn.wj.commonmodule.BaseAppLogic

class MusicPlayerLogic: BaseAppLogic(){
    override fun onCreate() {
        super.onCreate()
        DataListManager.instance.init(mApplication)
    }
}