package com.dirror.music.ui.dialog

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.dirror.music.App
import com.dirror.music.databinding.DialogSongMenuBinding
import com.dirror.music.manager.User
import com.dirror.music.music.local.MyFavorite
import com.dirror.music.music.standard.data.SOURCE_NETEASE
import com.dirror.music.music.standard.data.StandardSongData
import com.dirror.music.plugin.PluginConstants
import com.dirror.music.plugin.PluginSupport
import com.dirror.music.ui.base.BaseBottomSheetDialog
import com.dirror.music.util.AppConfig
import com.dirror.music.util.MagicHttp
import com.dirror.music.util.toast
import gdut.bsx.share2.Share2
import gdut.bsx.share2.ShareContentType

/**
 * 每个歌曲右边三个点点击后显示
 */
class SongMenuDialog
constructor(
    context: Context,
    private val activity: Activity,
    private val songData: StandardSongData,
    private val itemDeleteListener: () -> Unit
) : BaseBottomSheetDialog(context) {

    private var binding = DialogSongMenuBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
    }

    override fun initListener() {
        super.initListener()
        binding.apply {
            // 下一首播放
            itemNextPlay.setOnClickListener {
                App.musicController.value?.addToNextPlay(songData)
                toast("成功添加到下一首播放")
                dismiss()
            }
            // 添加到本地我喜欢
            itemAddLocalMyFavorite.setOnClickListener {
                MyFavorite.addSong(songData)
                dismiss()
            }
            // 添加到网易云我喜欢
            itemAddNeteaseFavorite.setOnClickListener {
                if (AppConfig.cookie.isEmpty()) {
                    toast("离线模式无法收藏到在线我喜欢~")
                } else {
                    when (songData.source) {
                        SOURCE_NETEASE -> {
                            App.cloudMusicManager.likeSong(songData.id ?: "", {
                                toast("添加到我喜欢成功")
                            }, {
                                toast("添加到我喜欢失败")
                            })
                        }
                        else -> {
                            toast("暂不支持此音源")
                            dismiss()
                        }
                    }
                }
            }
            // 分享给好友
            itemSongShare.setOnClickListener {
                var shareImageUri: Uri
                toast("加载海报中...")
                MagicHttp.OkHttpManager().getPic(
                    activity,
                    "https://csm.sayqz.com/api/web/share/?id=${songData.id}"
                ) { uri ->
                    try {
                        shareImageUri = uri
                        songData.name?.let { it1 ->
                            Share2.Builder(activity)
                                .setContentType(ShareContentType.IMAGE)
                                .setTitle(it1)
                                .setShareFileUri(shareImageUri)
                                .build()
                                .shareBySystem()
                        }
                    } catch (e: Exception) {

                    }
                }
                // 自己消失
                dismiss()
            }
            // 歌曲信息
            itemSongInfo.setOnClickListener {
                // toast("歌曲信息 ${ songData.id }")
                SongInfoDialog(context, songData).show()
                // 自己消失
                dismiss()
            }
            // 歌曲评论
            itemSongComment.setOnClickListener {
                App.activityManager.startCommentActivity(
                    activity,
                    songData.source ?: SOURCE_NETEASE,
                    songData.id ?: ""
                )
                dismiss()
            }
            // 歌曲删除
            itemDeleteSong.setOnClickListener {
                itemDeleteListener()
                dismiss()
            }

            PluginSupport.setSong(songData);
            PluginSupport.setActivity(activity)
            PluginSupport.setDialog(this@SongMenuDialog)
            PluginSupport.setMenu(songMenuParent)
            PluginSupport.apply(PluginConstants.POINT_SONG_MENU_DIALOG_INIT)
        }
    }

}