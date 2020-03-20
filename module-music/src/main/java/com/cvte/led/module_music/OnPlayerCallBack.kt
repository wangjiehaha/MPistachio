package com.cvte.led.module_music

interface OnPlayerCallBack {
    fun onPlayStateChange(state: PlayState)

    fun onSeekFinish()

    fun onPlayComplete()

    fun onPlayerError(errorCode: PlayErrorCode, errorMsg: String)

    fun onPlayerRelease()
}

enum class PlayState{
    PLAY_START,
    PLAY_STOP,
    PLAY_PAUSE,
    PLAY_RESUME
}

enum class PlayErrorCode {
    ERROR_UNKNOWN,
    ERROR_PLAYER_NO_INIT,
    ERROR_NO_SUPPORT
}