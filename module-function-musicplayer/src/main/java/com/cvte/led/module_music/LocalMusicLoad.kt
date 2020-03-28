package com.cvte.led.module_music

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import java.lang.Exception

object LocalMusicLoad {

     fun getAllLocalMusic(context: Context): ArrayList<MusicEntity> {
        val list = ArrayList<MusicEntity>()
        var cursor:Cursor? = null
        try {
            cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
            while (cursor?.moveToNext() == true) {
                val entity = MusicEntity()
                entity.uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                entity.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                entity.author = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                if (size > 1000 * 800) {
                    list.add(entity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list
    }
}