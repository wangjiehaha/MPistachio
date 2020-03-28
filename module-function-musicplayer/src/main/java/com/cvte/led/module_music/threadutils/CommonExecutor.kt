package com.cvte.led.module_music.threadutils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CommonExecutor private constructor(){
    companion object {
        val instance: CommonExecutor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CommonExecutor() }
    }
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val executorService: ExecutorService = Executors.newCachedThreadPool()

    fun executeRunnable(runnable: CommonRunnable<*,*>) {
        runnable.handler = mHandler
        executorService.execute(runnable)
    }
}

abstract class CommonRunnable<K, T>: Runnable, ThreadListener<K, T>{
    var handler: Handler? = null

    override fun run() {
        handler?.post { onThreadStart() }
        onRunning()
    }

    protected fun postProgress(k: K) {
        handler?.post { onPostProgress(k) }
    }

    protected fun postFinish(t: T) {
        handler?.post{ onTreadFinish(t) }
    }

    abstract fun onRunning()
}