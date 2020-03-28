package com.cvte.led.module_music.threadutils

interface ThreadListener<K, T> {
    fun onThreadStart()

    fun onPostProgress(k: K)

    fun onTreadFinish(t: T)
}