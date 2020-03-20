package com.cvte.led.module_music

class MusicPlayManager: OnPlayerCallBack {
    private val mMusicPlayer: MusicPlayer

    companion object {
        private var instance: MusicPlayManager? = null
            get() {
                if (field == null) {
                    field = MusicPlayManager()
                }
                return field
            }
        fun get(): MusicPlayManager{
            return instance!!
        }
    }

    init {
        mMusicPlayer = LocalMusicPlayer()
        mMusicPlayer.playerCallBack = this
        createPlayer()
    }

    private fun createPlayer() {
        mMusicPlayer.createPlayer()
    }

    private fun releasePlayer() {
        mMusicPlayer.releasePlayer()
    }

    override fun onPlayStateChange(state: PlayState) {

    }

    override fun onSeekFinish() {

    }

    override fun onPlayComplete() {

    }

    override fun onPlayerError(errorCode: PlayErrorCode, errorMsg: String) {

    }

    override fun onPlayerRelease() {

    }
}