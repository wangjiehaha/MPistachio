package com.cvte.led.module_music.player

import com.cvte.led.module_music.OnPlayerCallBack

abstract class MusicPlayer{
    var playerCallBack: OnPlayerCallBack? = null

    abstract fun createPlayer()

    abstract fun releasePlayer()

    abstract fun playUriAndStart(uri: String)

    abstract fun resume()

    abstract fun pause()

    abstract fun stop()

    abstract fun seek(position: Long)

    abstract fun getDurationPosition(): Long?

    abstract fun getDuration(): Long?
}