package com.cvte.led.module_music

import android.os.Handler
import com.cvte.led.module_music.bus.*
import com.cvte.led.module_music.player.LocalMusicPlayer
import com.cvte.led.module_music.player.MusicPlayer
import com.jeremyliao.liveeventbus.LiveEventBus
import java.util.*
import kotlin.collections.ArrayList

class MusicPlayManager private constructor(): OnPlayerCallBack {
    companion object {
        val instance: MusicPlayManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MusicPlayManager() }
    }

    private val mMusicPlayer: MusicPlayer = LocalMusicPlayer()
    private val mHandler: Handler = Handler()
    private val mPositionRunnable: Runnable by lazy {
        Runnable {
            LiveEventBus.get(KEY_MUSIC_DURATION_POSITION).post(mMusicPlayer.getDurationPosition())
            mHandler.postDelayed(mPositionRunnable, 1000)
        }
    }
    private val mDataListManager = DataListManager.instance
    private var mCurrentMusicEntity: MusicEntity? = null
    private var mMusicPosition: Int = 0
    private var playMode: PlayMode = PlayMode.PLAY_MODE_LOOP
    private val mRandom: Random = Random()

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

    fun playMusic(playList: ArrayList<MusicEntity>, position: Int) {
        if (playList != mDataListManager.musicPlayList) {
            mDataListManager.musicPlayList = playList
        }
        playMusic(position)
    }

    private fun playMusic(musicPosition: Int) {
        val entity = mDataListManager.getPlayListMusicEntity(musicPosition)
        if (entity != null) {
            mMusicPosition = musicPosition
            playMusic(entity)
        }
    }

    private fun playMusic(musicEntity: MusicEntity) {
        mCurrentMusicEntity = musicEntity
        LiveEventBus.get(KEY_MUSIC_ENTITY).post(mCurrentMusicEntity)
        mMusicPlayer.playUriAndStart(musicEntity.uri)
    }

    private fun startPositionRunnable() {
        mHandler.removeCallbacks(mPositionRunnable)
        mHandler.post(mPositionRunnable)
    }

    private fun stopPositionRunnable() {
        mHandler.removeCallbacks(mPositionRunnable)
    }

    private fun switchToNextMusic(focus: Boolean = false) {
        val position: Int
        if (focus) {
            position = if (mMusicPosition >= mDataListManager.musicPlayList.size){
                0
            } else {
                mMusicPosition + 1
            }
        } else {
            position = when (playMode){
                PlayMode.PLAY_MODE_LOOP -> {
                    if (mMusicPosition >= mDataListManager.musicPlayList.size){
                        0
                    } else {
                        mMusicPosition + 1
                    }
                }
                PlayMode.PLAY_MODE_RANDOM -> {
                    mRandom.nextInt(mDataListManager.musicPlayList.size)
                }
                PlayMode.PLAY_MODE_SINGLE -> {
                    mMusicPosition
                }
            }
        }
        playMusic(position)
    }

    private fun switchToPreMusic(focus: Boolean = false) {
        val position: Int
        if (focus) {
            position = if (mMusicPosition <= 0){
                mDataListManager.musicPlayList.size -1
            } else {
                mMusicPosition - 1
            }
        } else {
            position = when (playMode){
                PlayMode.PLAY_MODE_LOOP -> {
                    if (mMusicPosition <= 0){
                        mDataListManager.musicPlayList.size -1
                    } else {
                        mMusicPosition - 1
                    }
                }
                PlayMode.PLAY_MODE_RANDOM -> {
                    mRandom.nextInt(mDataListManager.musicPlayList.size)
                }
                PlayMode.PLAY_MODE_SINGLE -> {
                    mMusicPosition
                }
            }
        }
        playMusic(position)
    }

    override fun onPlayStateChange(state: PlayState) {
        LiveEventBus.get(KEY_MUSIC_STATE).post(state)
        when (state) {
            PlayState.PLAY_START -> {
                LiveEventBus.get(KEY_MUSIC_DURATION).post(mMusicPlayer.getDuration())
                startPositionRunnable()
            }
            PlayState.PLAY_RESUME ->{
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
        switchToNextMusic()
    }

    override fun onPlayerError(errorCode: PlayErrorCode, errorMsg: String?) {

    }

    override fun onPlayerRelease() {

    }
}