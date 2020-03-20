package com.cvte.led.module_music

import android.media.MediaPlayer

class LocalMusicPlayer: MusicPlayer() {

    private var mMediaPlayer: MediaPlayer? = null

    private val mPreparedListener:MediaPlayer.OnPreparedListener by lazy {
        MediaPlayer.OnPreparedListener {
            it.start()
            playerCallBack?.onPlayStateChange(PlayState.PLAY_START)
        }
    }

    private val mErrorListener:MediaPlayer.OnErrorListener by lazy {
        MediaPlayer.OnErrorListener { mp, what, extra ->
            val errorMsg = ""
            playerCallBack?.onPlayerError(PlayErrorCode.ERROR_UNKNOWN, errorMsg)
            false
        }
    }

    private val mCompletionListener:MediaPlayer.OnCompletionListener by lazy {
        MediaPlayer.OnCompletionListener {
            playerCallBack?.onPlayComplete()
        }
    }

    private val mSeekListener:MediaPlayer.OnSeekCompleteListener by lazy {
        MediaPlayer.OnSeekCompleteListener {
            playerCallBack?.onSeekFinish()
        }
    }

    override fun createPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
        }
        mMediaPlayer?.setOnPreparedListener(mPreparedListener)
        mMediaPlayer?.setOnErrorListener(mErrorListener)
        mMediaPlayer?.setOnCompletionListener(mCompletionListener)
        mMediaPlayer?.setOnSeekCompleteListener(mSeekListener)
    }

    override fun releasePlayer() {
        mMediaPlayer?.release()
        mMediaPlayer = null
        playerCallBack?.onPlayerRelease()
    }

    override fun playUriAndStart(uri: String) {

    }

    override fun resume() {
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.start()
            playerCallBack?.onPlayStateChange(PlayState.PLAY_RESUME)
        }
    }

    override fun pause() {
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.pause()
            playerCallBack?.onPlayStateChange(PlayState.PLAY_PAUSE)
        }
    }

    override fun stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer?.stop()
            playerCallBack?.onPlayStateChange(PlayState.PLAY_STOP)
        }
    }

    override fun seek(position: Long) {
        mMediaPlayer?.seekTo(position.toInt())
    }
}