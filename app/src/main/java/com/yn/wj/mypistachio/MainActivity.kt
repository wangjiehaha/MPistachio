package com.yn.wj.mypistachio

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.yn.wj.commonmodule.RouterTable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ARouter.getInstance().build(RouterTable.ACTIVITY_SIGN_IN).navigation()
        finish()
    }
}
