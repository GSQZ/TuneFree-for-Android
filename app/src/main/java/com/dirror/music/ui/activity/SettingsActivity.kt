package com.dirror.music.ui.activity

import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.dirror.music.App
import com.dirror.music.App.Companion.mmkv
import com.dirror.music.App.Companion.musicController
import com.dirror.music.api.API_AUTU
import com.dirror.music.databinding.ActivitySettingsBinding
import com.dirror.music.ui.base.BaseActivity
import com.dirror.music.ui.live.NeteaseCloudMusicApiActivity
import com.dirror.music.util.*
import com.dirror.music.util.cache.ACache
import com.dirror.music.util.cache.CommonCacheInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

/**
 * 设置 Activity
 */
class SettingsActivity : BaseActivity() {

    companion object {
        const val ACTION = "com.dirror.music.SETTINGS_CHANGE"
    }

    private lateinit var binding: ActivitySettingsBinding

    override fun initBinding() {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        miniPlayer = binding.miniPlayer
        setContentView(binding.root)
    }

    override fun initData() {
        thread {
            val size = ImageCacheManager.getImageCacheSize()
            val httpCacheSize = CommonCacheInterceptor.getCacheSize()
            runOnMainThread {
                binding.valueViewImageCache.setValue(size)
                binding.valueHttpCache.setValue(httpCacheSize)
            }
        }

    }

    override fun initView() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            binding.itemAudioFocus.visibility = View.GONE
        }
        // 按钮
        binding.apply {
            switcherPlaylistScrollAnimation.setChecked(
                mmkv.decodeBool(
                    Config.PLAYLIST_SCROLL_ANIMATION,
                    true
                )
            )
            switcherDarkTheme.setChecked(mmkv.decodeBool(Config.DARK_THEME, false))
            switcherSentenceRecommend.setChecked(mmkv.decodeBool(Config.SENTENCE_RECOMMEND, true))
            switcherPlayOnMobile.setChecked(mmkv.decodeBool(Config.PLAY_ON_MOBILE, false))
            switcherPauseSongAfterUnplugHeadset.setChecked(
                mmkv.decodeBool(
                    Config.PAUSE_SONG_AFTER_UNPLUG_HEADSET,
                    true
                )
            )
            switcherSkipErrorMusic.setChecked(mmkv.decodeBool(Config.SKIP_ERROR_MUSIC, true))
            switcherFilterRecord.setChecked(mmkv.decodeBool(Config.FILTER_RECORD, true))
            switcherLocalMusicParseLyric.setChecked(
                mmkv.decodeBool(
                    Config.PARSE_INTERNET_LYRIC_LOCAL_MUSIC,
                    true
                )
            )
            switcherFineTuning.setChecked(mmkv.decodeBool(Config.FINE_TUNING, true))
            switcherSmartFilter.setChecked(mmkv.decodeBool(Config.SMART_FILTER, true))
            switcherAudioFocus.setChecked(mmkv.decodeBool(Config.ALLOW_AUDIO_FOCUS, true))
            switcherSingleColumnPlaylist.setChecked(
                mmkv.decodeBool(
                    Config.SINGLE_COLUMN_USER_PLAYLIST,
                    false
                )
            )
            switcherStatusBarLyric.setChecked(mmkv.decodeBool(Config.MEIZU_STATUS_BAR_LYRIC, true))
            switcherInkScreenMode.setChecked(mmkv.decodeBool(Config.INK_SCREEN_MODE, false))
//            switcherAutoChangeResource.setChecked(
//                mmkv.decodeBool(
//                    Config.AUTO_CHANGE_RESOURCE,
//                    false
//                )
//            )
            soundQualityText.setText(mmkv.decodeString(Config.SOUND_QUALITY_SELECTION, "标准"))
        }


    }

    override fun initListener() {
        binding.apply {
            itemCleanBackground.setOnClickListener {
                ACache.get(this@SettingsActivity).remove(Config.APP_THEME_BACKGROUND)
                toast("清除成功")
            }

            switcherPlaylistScrollAnimation.setOnCheckedChangeListener {
                mmkv.encode(
                    Config.PLAYLIST_SCROLL_ANIMATION,
                    it
                )
            }

            switcherDarkTheme.setOnCheckedChangeListener {
                mmkv.encode(Config.DARK_THEME, it)
                DarkThemeUtil.setDarkTheme(it)
            }

            switcherSentenceRecommend.setOnCheckedChangeListener {
                mmkv.encode(Config.SENTENCE_RECOMMEND, it)
            }

            switcherFilterRecord.setOnCheckedChangeListener {
                mmkv.encode(
                    Config.FILTER_RECORD,
                    it
                )
            }

            switcherLocalMusicParseLyric.setOnCheckedChangeListener {
                mmkv.encode(
                    Config.PARSE_INTERNET_LYRIC_LOCAL_MUSIC,
                    it
                )
            }

            switcherSkipErrorMusic.setOnCheckedChangeListener {
                mmkv.encode(
                    Config.SKIP_ERROR_MUSIC,
                    it
                )
            }
            switcherFineTuning.setOnCheckedChangeListener {
                mmkv.encode(
                    Config.FINE_TUNING,
                    it
                )
            }
            switcherPlayOnMobile.setOnCheckedChangeListener {
                mmkv.encode(
                    Config.PLAY_ON_MOBILE,
                    it
                )
            }

            switcherPauseSongAfterUnplugHeadset.setOnCheckedChangeListener {
                mmkv.encode(
                    Config.PAUSE_SONG_AFTER_UNPLUG_HEADSET,
                    it
                )
            }

            switcherSmartFilter.setOnCheckedChangeListener { mmkv.encode(Config.SMART_FILTER, it) }

            switcherAudioFocus.setOnCheckedChangeListener {
                musicController.value?.setAudioFocus(it)
            }

            itemCustomBackground.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, null)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                startActivityForResult(intent, 2)
            }

            switcherSingleColumnPlaylist.setOnCheckedChangeListener {
                mmkv.encode(
                    Config.SINGLE_COLUMN_USER_PLAYLIST,
                    it
                )
            }

            switcherStatusBarLyric.setOnCheckedChangeListener {
                musicController.value?.statusBarLyric = it
                mmkv.encode(Config.MEIZU_STATUS_BAR_LYRIC, it)
            }

            itemClearImageCache.setOnClickListener {
                ImageCacheManager.clearImageCache {
                    toast("清除图片缓存成功")
                    thread {
                        val size = ImageCacheManager.getImageCacheSize()
                        runOnMainThread {
                            binding.valueViewImageCache.setValue(size)
                        }
                    }
                }
            }
            itemClearHttpCache.setOnClickListener {
                GlobalScope.launch {
                    CommonCacheInterceptor.clearCache()
                    withContext(Dispatchers.Main) {
                        toast("清除歌单缓存成功")
                        val size = CommonCacheInterceptor.getCacheSize()
                        binding.valueHttpCache.setValue(size)
                    }
                }
            }
            switcherInkScreenMode.setOnCheckedChangeListener {
                mmkv.encode(Config.INK_SCREEN_MODE, it)
            }

            soundQualitySelection.setOnClickListener {
                val items = arrayOf("标准", "较高", "极高")
                showSelectionDialog(items) { selectedOption ->
                    mmkv.encode(Config.SOUND_QUALITY_SELECTION, selectedOption)
                    soundQualityText.setText(mmkv.decodeString(Config.SOUND_QUALITY_SELECTION, "标准"))
                }
            }


//            itemNeteaseCloudMusicApi.setOnClickListener {
//                startActivity(Intent(this@SettingsActivity, NeteaseCloudMusicApiActivity::class.java))
//            }

//            switcherAutoChangeResource.setOnCheckedChangeListener {
//                mmkv.encode(
//                    Config.AUTO_CHANGE_RESOURCE,
//                    it
//                )
//            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            // 从相册返回的数据
            // 得到图片的全路径
            val path = data?.data.toString()
            path.let {
                toast("设置成功")
                CoilUtil.load(this, it) { bitmap ->
                    thread {
                        ACache.get(this).put(Config.APP_THEME_BACKGROUND, bitmap)
                    }
                }
            }

        }
    }

    override fun onPause() {
        super.onPause()
        mmkv.encode(Config.SOUND_QUALITY_SELECTION, binding.soundQualityText.text.toString())
        BroadcastUtil.send(this, ACTION)
    }

    fun showSelectionDialog(options: Array<String>, onOptionSelected: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
//        builder.setTitle(title)
        builder.setItems(options) { dialog, which ->
            // 点击选项时的回调
            onOptionSelected(options[which])
        }
        builder.create().show()
    }

}