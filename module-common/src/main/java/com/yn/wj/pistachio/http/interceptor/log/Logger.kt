package com.yn.wj.pistachio.http.interceptor.log

import okhttp3.internal.platform.Platform

interface Logger {
    fun log(level: Int, tag: String?, msg: String?)

    companion object {
        val DEFAULT: Logger = object : Logger {
            override fun log(level: Int, tag: String?, msg: String?) {
                Platform.get().log(level, msg, null)
            }
        }
    }
}