package com.cvte.led.module_music.bus

const val KEY_MUSIC_DURATION = "key_music_duration"
const val KEY_MUSIC_DURATION_POSITION = "key_music_duration_position"
const val KEY_MUSIC_STATE = "key_music_state"
const val KEY_MUSIC_ENTITY = "key_music_entity"
const val KEY_LOCAL_MUSIC_LIST = "key_local_music_list"

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

enum class PlayMode {
    PLAY_MODE_LOOP,
    PLAY_MODE_SINGLE,
    PLAY_MODE_RANDOM
}