package com.cvte.led.module_music

import android.os.Handler
import com.cvte.led.module_music.bus.KEY_MUSIC_DURATION_POSITION
import com.cvte.led.module_music.bus.KEY_MUSIC_STATE
import com.cvte.led.module_music.player.LocalMusicPlayer
import com.cvte.led.module_music.player.MusicPlayer
import com.jeremyliao.liveeventbus.LiveEventBus

class MusicPlayManager: OnPlayerCallBack {
    private val mMusicPlayer: MusicPlayer = LocalMusicPlayer()
    private val mHandler: Handler = Handler()
    private val mPositionRunnable: Runnable by lazy {
        Runnable {
            LiveEventBus.get(KEY_MUSIC_DURATION_POSITION).post(mMusicPlayer.getDurationPosition())
            mHandler.postDelayed(mPositionRunnable, 1000)
        }
    }

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
        mMusicPlayer.playerCallBack = this
        createPlayer()
    }

    private fun createPlayer() {
        mMusicPlayer.createPlayer()
    }

    private fun releasePlayer() {
        mMusicPlayer.releasePlayer()
    }

    fun playMusic(uri: String) {
        mMusicPlayer.playUriAndStart(uri)
    }

    private fun startPositionRunnable() {
        mHandler.removeCallbacks(mPositionRunnable)
        mHandler.post(mPositionRunnable)
    }

    private fun stopPositionRunnable() {
        mHandler.removeCallbacks(mPositionRunnable)
    }

    override fun onPlayStateChange(state: PlayState) {
        LiveEventBus.get(KEY_MUSIC_STATE).post(state)
        when (state) {
            PlayState.PLAY_START, PlayState.PLAY_RESUME ->{
                startPositionRunnable()
            }
            PlayState.PLAY_STOP, PlayState.PLAY_PAUSE ->{
                stopPositionRunnable()
            }
        }
    }

    override fun onSeekFinish() {

    }

    override fun onPlayComplete() {

    }

    override fun onPlayerError(errorCode: PlayErrorCode, errorMsg: String?) {

    }

    override fun onPlayerRelease() {

    }
}