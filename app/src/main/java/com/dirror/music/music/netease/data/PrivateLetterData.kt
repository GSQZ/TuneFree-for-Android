package com.dirror.music.music.netease.data

import androidx.annotation.Keep

@Keep
data class PrivateLetterData(
    val msgs: ArrayList<MsgsData>,
    val code: Int,
    val more: Boolean,
    val newMsgCount: Int
) {
    data class MsgsData(
        val fromUser: FromUserData,
        val newMsgCount: Int,
        val lastMsg: String,
        val lastMsgTime: Long
    ) {
        data class FromUserData(
            val userId: Long,
            val nickname: String,
            val avatarUrl: String, // 头像
        )
    }
}

data class LastMsgData(
    val msg: String
)
