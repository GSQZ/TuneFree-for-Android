package com.dirror.music.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.dirror.music.App
import com.dirror.music.R
import com.dirror.music.music.standard.data.StandardSongData
import com.dirror.music.ui.player.PlayerActivity
import java.util.*

/**
 * 播放音乐
 */
fun playMusic(context: Context?, song: StandardSongData, songList: ArrayList<StandardSongData>, playAll: Boolean = false) {
     App.musicController.value?.setPersonFM(false)
    // 获取 position
    val position = if (songList.indexOf(song) == -1) {
        0
    } else {
        songList.indexOf(song)
    }
    // 歌单相同
    if (App.musicController.value?.getPlaylist() == songList) {
        // position 相同
        if (position == App.musicController.value?.getNowPosition() && context is Activity) {
            context.startActivity(Intent(context, PlayerActivity::class.java))
            context.overridePendingTransition(
                R.anim.anim_slide_enter_bottom,
                R.anim.anim_no_anim
            )
        } else {
            App.musicController.value?.playMusic(song, playAll)
        }
    } else {
        // 设置歌单
        App.musicController.value?.setPlaylist(songList)
        // 播放歌单
        App.musicController.value?.playMusic(song, playAll)
    }
}