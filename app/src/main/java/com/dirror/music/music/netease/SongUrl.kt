package com.dirror.music.music.netease

import com.dirror.music.App
import com.dirror.music.App.Companion.mmkv
import com.dirror.music.api.API_AUTU
import com.dirror.music.api.API_MUSIC_ELEUU
import com.dirror.music.manager.User
import com.dirror.music.music.dirror.SearchSong
import com.dirror.music.music.netease.data.SongUrlData
import com.dirror.music.util.*
import com.google.gson.Gson
import okhttp3.FormBody

object SongUrl {

//    const val API = "${API_AUTU}/song/url?id=33894312"
//    const val API = "${API_AUTU}/api/?type=apiSongUrlV1&id=2039194916"

    fun getSongUrl(id: String): String {
        return if (SearchSong.getDirrorSongUrl(id) != "") {
            SearchSong.getDirrorSongUrl(id)
        } else {
            "https://music.163.com/song/media/outer/url?id=${id}.mp3"
        }
    }

    fun getSongUrlCookie(id: String, success: (String) -> Unit) {
//        var api = User.neteaseCloudMusicApi
//        if (api.isEmpty()) {
//            api = API_MUSIC_ELEUU
//        }
//        val api = API_MUSIC_ELEUU
        val api = "https://csm.sayqz.com/api/?type=apiSongUrlV1"
        val level: String =
            getSoundQualityLevel(mmkv.decodeString(Config.SOUND_QUALITY_SELECTION, "标准") ?: "标准")


        val url =
            "${api}&id=" + id + "&level=" + level + "&cookie=" + AppConfig.cookie
        MagicHttp.OkHttpManager().newGet(url, {
            // 成功
            try {
//                loge(it, "数据")
                val songUrlData = Gson().fromJson(it, SongUrlData::class.java)
//                loge(songUrlData.data[0].url ?: "", "音乐地址")
                success.invoke(songUrlData.data[0].url ?: "")
            } catch (e: Exception) {

            }
        }, {

        })


//        val requestBody = FormBody.Builder()
//            .add("crypto", "api")
//            .add("cookie", AppConfig.cookie)
//            .add("withCredentials", "true")
//            .add("realIP", "211.161.244.70")
//            .add("id", id)
//            .add("type", "apiSongUrlV1")
//            .add("level", getSoundQualityLevel(level))
//            .build()
//        MagicHttp.OkHttpManager()
//            .newPost("${api}&id=" + id + "&level=" + getSoundQualityLevel(level), requestBody, {
//                try {
//                    loge("${api}&id=" + id + "&level=" + getSoundQualityLevel(level), "URL地址")
//                    loge(it, "数据")
//                    val songUrlData = Gson().fromJson(it, SongUrlData::class.java)
//                    loge(songUrlData.data[0].url ?: "", "音乐地址")
//                    success.invoke(songUrlData.data[0].url ?: "")
//                } catch (e: Exception) {
//                    // failure.invoke(ErrorCode.ERROR_JSON)
//                }
//            }, {
//
//            })
    }

    suspend fun getSongUrlN(id: String): String {
        val url = "$API_AUTU/song/url?id=$id"
        val result = HttpUtils.get(url, SongUrlData::class.java)
        return result?.data?.get(0)?.url ?: getSongUrl(id)
    }

    private fun getSoundQualityLevel(level: String) =
        when (level) {
            "标准" -> "standard"
            "较高" -> "higher"
            "极高" -> "exhigh"
            "无损" -> "lossless"
            "Hi-Res" -> "hires"
            "高清环绕声" -> "jyeffect"
            "沉浸环绕声" -> "sky"
            "超清母带" -> "jymaster"
            else -> "standard"
        }

}