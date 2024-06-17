package com.dirror.music.ui.activity

import android.content.Intent
import android.graphics.Typeface
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.dirror.music.App
import com.dirror.music.databinding.ActivityLogin3Binding
import com.dirror.music.manager.User
import com.dirror.music.ui.base.BaseActivity
import com.dirror.music.util.getStatusBarHeight
import com.dirror.music.util.toast

/**
 * 预计 2.0 版
 * LoginActivity3 界面，取代原来的 LoginActivity2
 */
class LoginActivity3 : BaseActivity() {

    private lateinit var binding: ActivityLogin3Binding

    override fun initBinding() {
        binding = ActivityLogin3Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
//        if (MyApp.mmkv.decodeBool(Config.USER_NETEASE_CLOUD_MUSIC_API_ENABLE, false)) {
//            binding.btnLoginByPhone.visibility = View.VISIBLE
//        } else {
//            binding.btnLoginByPhone.visibility = View.GONE
//        }

        binding.titleBar.updateLayoutParams<LinearLayout.LayoutParams> {
            topMargin = getStatusBarHeight(window, this@LoginActivity3)
        }

        try {
            val typeface = Typeface.createFromAsset(assets, "fonts/Moriafly-Regular.ttf")

        } catch (e: Exception) {

        }
    }

    override fun initListener() {
        binding.apply{
            // 手机验证码登录
            btnLoginByPhoneCaptcha.setOnClickListener{
                App.activityManager.startLoginByPhoneCaptchaActivity(this@LoginActivity3)
            }
            // 手机号登录
            btnLoginByPhone.setOnClickListener {
                App.activityManager.startLoginByPhoneActivity(this@LoginActivity3)
            }

            // UID 登录
            btnLoginByUid.setOnClickListener {
                App.activityManager.startLoginByUidActivity(this@LoginActivity3)
            }
            // 二维码登录
            btnLoginByQRCode.setOnClickListener {
                if (User.neteaseCloudMusicApi.isNotEmpty()) {
                    startActivityForResult(Intent(this@LoginActivity3, LoginByQRCodeActivity::class.java), 0)
                } else {
                    toast("请先配置网易云API再使用二维码登录")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK) {
                finish()
            }
        }
    }

}