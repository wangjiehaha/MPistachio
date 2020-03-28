package com.cvte.led.module_music

import android.content.Context
import com.cvte.led.module_music.bus.KEY_LOCAL_MUSIC_LIST
import com.cvte.led.module_music.threadutils.CommonExecutor
import com.cvte.led.module_music.threadutils.CommonRunnable
import com.jeremyliao.liveeventbus.LiveEventBus

class DataListManager private constructor(){
    companion object {
        val instance: DataListManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DataListManager() }
    }

    var musicPlayList: ArrayList<MusicEntity> = ArrayList()
    var localMusicList: ArrayList<MusicEntity> = ArrayList()

    fun init(context: Context) {
        fillLocalMusicList(context)
    }

    fun getPlayListMusicEntity(position: Int): MusicEntity? {
        return if (position < 0 || position >= musicPlayList.size){
            null
        } else {
            musicPlayList[position]
        }
    }

    private fun fillLocalMusicList(context: Context) {
        val runnable: CommonRunnable<Void, ArrayList<MusicEntity>> =
                object : CommonRunnable<Void, ArrayList<MusicEntity>>() {
            override fun onRunning() {
                val list = LocalMusicLoad.getAllLocalMusic(context)
                postFinish(list)
            }

            override fun onThreadStart() {
                localMusicList.clear()
                LiveEventBus.get(KEY_LOCAL_MUSIC_LIST).post(localMusicList)
            }

            override fun onPostProgress(k: Void) {
            }

            override fun onTreadFinish(t: ArrayList<MusicEntity>) {
                localMusicList.addAll(t)
                LiveEventBus.get(KEY_LOCAL_MUSIC_LIST).post(localMusicList)
            }
        }
        CommonExecutor.instance.executeRunnable(runnable)
    }
}

class MusicEntity(var uri: String?, var title: String?, var author: String?,
                  var lrcUri: String?){
    constructor() : this(null, null, null, null) {

    }
}