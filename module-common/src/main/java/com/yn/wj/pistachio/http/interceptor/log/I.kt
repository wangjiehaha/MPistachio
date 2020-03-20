package com.yn.wj.pistachio.http.interceptor.log

import okhttp3.internal.platform.Platform
import java.util.logging.Level
import java.util.logging.Logger

internal class I protected constructor() {
    companion object {
        @JvmStatic
        fun log(type: Int, tag: String?, msg: String?) {
            val logger = Logger.getLogger(tag)
            when (type) {
                Platform.INFO -> logger.log(Level.INFO, msg)
                else -> logger.log(Level.WARNING, msg)
            }
        }
    }

    init {
        throw UnsupportedOperationException()
    }
}