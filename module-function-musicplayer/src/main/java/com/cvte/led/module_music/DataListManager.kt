package com.cvte.led.module_music

class DataListManager private constructor(){
    companion object {
        val instance: DataListManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DataListManager() }
    }

    var musicPlayList: ArrayList<MusicEntity> = ArrayList()

    fun getPlayListMusicEntity(position: Int): MusicEntity? {
        return if (position < 0 || position >= musicPlayList.size){
            null
        } else {
            musicPlayList[position]
        }
    }
}

class MusicEntity(var uri: String, var title: String, var author: String,
                  var lrcUri: String)