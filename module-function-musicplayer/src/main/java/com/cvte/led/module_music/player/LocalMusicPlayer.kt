package com.cvte.led.module_music.player

import android.media.MediaPlayer
import com.cvte.led.module_music.bus.PlayErrorCode
import com.cvte.led.module_music.bus.PlayState
import java.lang.IllegalStateException

class LocalMusicPlayer: MusicPlayer() {

    private var mMediaPlayer: MediaPlayer? = null
    private var mState = MediaPlayerState.END

    private val mPreparedListener:MediaPlayer.OnPreparedListener by lazy {
        MediaPlayer.OnPreparedListener {
            mState = MediaPlayerState.PREPARED
            it.start()
            mState = MediaPlayerState.STARTED
            playerCallBack?.onPlayStateChange(PlayState.PLAY_START)
        }
    }

    private val mErrorListener:MediaPlayer.OnErrorListener by lazy {
        MediaPlayer.OnErrorListener { mp, what, extra ->
            mState = MediaPlayerState.ERROR
            val errorMsg = ""
            playerCallBack?.onPlayerError(PlayErrorCode.ERROR_UNKNOWN, errorMsg)
            false
        }
    }

    private val mCompletionListener:MediaPlayer.OnCompletionListener by lazy {
        MediaPlayer.OnCompletionListener {
            mState = MediaPlayerState.PLAYBACK_COMPLETED
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
        mState = MediaPlayerState.IDLE
    }

    override fun releasePlayer() {
        mMediaPlayer?.release()
        mMediaPlayer = null
        playerCallBack?.onPlayerRelease()
        mState = MediaPlayerState.END
    }

    override fun playUriAndStart(uri: String?) {
        if (uri == null) {
            return
        }
        when (mState) {
            MediaPlayerState.END ->{
                playerCallBack?.onPlayerError(PlayErrorCode.ERROR_PLAYER_NO_INIT)
                return
            }
            MediaPlayerState.PREPARED, MediaPlayerState.STARTED, MediaPlayerState.PAUSED,
                MediaPlayerState.PLAYBACK_COMPLETED -> {
                mMediaPlayer?.stop()
                mState = MediaPlayerState.STOPPED
                mMediaPlayer?.reset()
                mState = MediaPlayerState.IDLE
            }
            else ->{
                mMediaPlayer?.reset()
                mState = MediaPlayerState.IDLE
            }
        }

        try {
            mMediaPlayer?.setDataSource(uri)
            mState = MediaPlayerState.INITIALIZED
            mMediaPlayer?.prepareAsync()
            mState = MediaPlayerState.PREPARING
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun resume() {
        if (mState == MediaPlayerState.PAUSED) {
            mMediaPlayer?.start()
            mState = MediaPlayerState.STARTED
            playerCallBack?.onPlayStateChange(PlayState.PLAY_RESUME)
        }
    }

    override fun pause() {
        if (mState == MediaPlayerState.STARTED) {
            mMediaPlayer?.pause()
            mState = MediaPlayerState.PAUSED
            playerCallBack?.onPlayStateChange(PlayState.PLAY_PAUSE)
        }
    }

    override fun stop() {
        if (isCanOperate()) {
            mMediaPlayer?.stop()
            mState = MediaPlayerState.STOPPED
            playerCallBack?.onPlayStateChange(PlayState.PLAY_STOP)
        }
    }

    override fun seek(position: Long) {
        if (isCanOperate()) {
            mMediaPlayer?.seekTo(position.toInt())
        }
    }

    override fun getDurationPosition(): Long? {
        return mMediaPlayer?.currentPosition as Long?
    }

    override fun getDuration(): Long? {
        return mMediaPlayer?.duration as Long?
    }

    private fun isCanOperate(): Boolean {
        return mState != MediaPlayerState.END
                && mState != MediaPlayerState.INITIALIZED
                && mState != MediaPlayerState.PREPARING
                && mState != MediaPlayerState.ERROR
                && mState != MediaPlayerState.IDLE
                && mState != MediaPlayerState.STOPPED
    }

    private enum class MediaPlayerState {
        IDLE,
        END,
        ERROR,
        PREPARING,
        INITIALIZED,
        PREPARED,
        STARTED,
        STOPPED,
        PAUSED,
        PLAYBACK_COMPLETED
    }
}