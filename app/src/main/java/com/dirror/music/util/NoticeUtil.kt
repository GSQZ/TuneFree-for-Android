package com.dirror.music.util

import android.app.Activity
import androidx.annotation.Keep
import com.dirror.music.api.API_NOTICE
import com.dirror.music.api.API_UPDARE
import com.dirror.music.ui.dialog.NoticeDialog
import com.google.gson.Gson
import java.lang.Exception

@Keep
object NoticeUtil {

    /**
     * 检查新版本
     * 传入 [activity]，[showLastedToast] 开启表示显示如果是最新版或是获取数据错误时弹出 Toast
     */
    fun checkNewPopUp(activity: Activity, showLastedToast: Boolean) {
        getServerVersion({ noticeData ->
            runOnMainThread {
                if (noticeData.show){
                    val noticeDialog = NoticeDialog(activity, noticeData) {
                        it.dismiss()
                    }
                    noticeDialog.show()
                }

            }
        }, {
            if (showLastedToast) {
                toast("获取服务器版本信息失败")
            }
        })
    }

    /**
     * 检查服务器版本
     */
    private fun getServerVersion(success: (NoticeData) -> Unit, failure: () -> Unit) {
        val url = API_NOTICE
        MagicHttp.OkHttpManager().newGet(url, {
            // 成功
            try {
                success.invoke(Gson().fromJson(it, NoticeData::class.java))
            } catch (e: Exception) {
                failure.invoke()
            }
        }, {

        })
    }

    @Keep
    data class NoticeData(
        val title: String,
        val time: String?,
        val msg: String,
        val name: String?,
        val show: Boolean
    )

}