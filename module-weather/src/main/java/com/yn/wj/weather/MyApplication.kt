package com.yn.wj.weather

import com.yn.wj.commonmodule.BaseAppLogic
import com.yn.wj.pistachio.base.BaseApplication
import interfaces.heweather.com.interfacesmodule.view.HeConfig

class MyApplication: BaseApplication() {

    override fun initLogic() {
        registerApplicationLogic(WeatherLogic::class.java)
    }
}

const val WEATHER_ID = "HE2003071647041639"
const val WEATHER_KEY = "3d931941986a454aa5f19911725ae5ae"

class WeatherLogic : BaseAppLogic() {

    override fun onCreate() {
        super.onCreate()
        HeConfig.init(WEATHER_ID, WEATHER_KEY)
    }
}