package com.yn.wj.mypistachio

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.alibaba.android.arouter.launcher.ARouter
import com.yn.wj.commonmodule.RouterTable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val handler = Handler()
        handler.postDelayed({ startMain() }, 3 * 1000)
    }

    private fun startMain() {
        ARouter.getInstance().build(RouterTable.ACTIVITY_SIGN_IN).navigation()
        finish()
    }
}
