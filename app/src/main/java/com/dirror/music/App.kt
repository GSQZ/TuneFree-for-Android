/**
 * DsoMusic Copyright (C) 2020-2021 Moriafly
 *
 * This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.
 * This is free software, and you are welcome to redistribute it
 * under certain conditions; type `show c' for details.
 *
 * The hypothetical commands `show w' and `show c' should show the appropriate
 * parts of the General Public License.  Of course, your program's commands
 * might be different; for a GUI interface, you would use an "about box".
 *
 * You should also get your employer (if you work as a programmer) or school,
 * if any, to sign a "copyright disclaimer" for the program, if necessary.
 * For more information on this, and how to apply and follow the GNU GPL, see
 * <https://www.gnu.org/licenses/>.
 *
 * The GNU General Public License does not permit incorporating your program
 * into proprietary programs.  If your program is a subroutine library, you
 * may consider it more useful to permit linking proprietary applications with
 * the library.  If this is what you want to do, use the GNU Lesser General
 * Public License instead of this License.  But first, please read
 * <https://www.gnu.org/licenses/why-not-lgpl.html>.
 */

@file:Suppress("unused", "UNUSED_PARAMETER")

package com.dirror.music

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import com.dirror.music.manager.ActivityManager
import com.dirror.music.manager.CloudMusicManager
import com.dirror.music.room.AppDatabase
import com.dirror.music.service.MusicService
import com.dirror.music.service.MusicServiceConnection
import com.dirror.music.util.*
import com.tencent.mmkv.MMKV
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Dso Music App
 *
 * @author Moriafly
 * @since 2021-7-13
 */
@Keep
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // 全局 context
        context = applicationContext
        // MMKV 初始化
        MMKV.initialize(context)
        mmkv = MMKV.defaultMMKV()!!
        // 管理初始化
        activityManager = ActivityManager()
        cloudMusicManager = CloudMusicManager()
        // 初始化数据库
        appDatabase = AppDatabase.getDatabase(this)
        // 安全检查
        checkSecure()

        if (mmkv.decodeBool(Config.DARK_THEME, false)) {
            DarkThemeUtil.setDarkTheme(true)
        }

        realIP = "175.16.1.195"
        coroutineScope.launch {
            val lastIP = "LAST_IP"
            val lastIPExpiredTime = "LAST_IP_TIME" // 过期时间
            val ip = mmkv.decodeString(lastIP, "")
            val now = System.currentTimeMillis()
            val expiredTime = mmkv.decodeLong(lastIPExpiredTime, now)
            if (ip == null || ip.isEmpty() || expiredTime < now) {
                Log.i(TAG, "ip is expired.")
                realIP = ChineseIPData.getRandomIP(this@App)
                mmkv.encode(lastIP, realIP)
                mmkv.encode(lastIPExpiredTime, now + 24 * 60 * 60 * 1000)
            } else{
                realIP = ip
            }
        }
    }

    /**
     * 安全检查
     */
    private fun checkSecure() {
        if (Secure.isSecure()) {
            UMConfigure.preInit(context, UM_APP_KEY, "")
            // 初始化友盟
            UMConfigure.init(context, UM_APP_KEY, "", UMConfigure.DEVICE_TYPE_PHONE, "")
//             选用 AUTO 页面采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
            // 开启音乐服务
            startMusicService()
        } else {
            Secure.killMyself()
        }
    }

    /**
     * 启动音乐服务
     */
    private fun startMusicService() {
        // 通过 Service 播放音乐，混合启动
        val intent = Intent(this, MusicService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        // 绑定服务
        bindService(intent, musicServiceConnection, BIND_AUTO_CREATE)
    }

    companion object {

        private val TAG = this::class.java.simpleName

        const val UM_APP_KEY = "66323940940d5a4c494c7786"

        lateinit var mmkv: MMKV

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        var musicController = MutableLiveData<MusicService.MusicController?>()

        val musicServiceConnection by lazy { MusicServiceConnection() } // 音乐服务连接

        @Deprecated("过时，使用 StartActivity")
        lateinit var activityManager: ActivityManager

        lateinit var cloudMusicManager: CloudMusicManager

        val coroutineScope = CoroutineScope(EmptyCoroutineContext)

        /** 数据库 */
        lateinit var appDatabase: AppDatabase

        lateinit var realIP: String
    }

}