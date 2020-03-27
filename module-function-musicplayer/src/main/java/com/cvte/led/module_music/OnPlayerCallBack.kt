package com.cvte.led.module_music

import com.cvte.led.module_music.bus.PlayErrorCode
import com.cvte.led.module_music.bus.PlayState

interface OnPlayerCallBack {
    fun onPlayStateChange(state: PlayState)

    fun onSeekFinish()

    fun onPlayComplete()

    fun onPlayerError(errorCode: PlayErrorCode, errorMsg: String? = null)

    fun onPlayerRelease()
}