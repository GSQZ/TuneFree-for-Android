package com.dirror.music.ui.activity

import android.graphics.drawable.Drawable
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dirror.music.R
import com.dirror.music.adapter.DailyRecommendSongAdapter
import com.dirror.music.databinding.ActivityRecommendBinding
import com.dirror.music.music.netease.data.toStandardSongDataArrayList
import com.dirror.music.service.playMusic
import com.dirror.music.ui.base.BaseActivity
import com.dirror.music.ui.viewmodel.RecommendActivityViewModel
import com.dirror.music.util.*
import eightbitlab.com.blurview.RenderScriptBlur
import java.util.*

class RecommendActivity : BaseActivity() {

    private lateinit var binding: ActivityRecommendBinding

    private val recommendActivityViewModel: RecommendActivityViewModel by viewModels()

    override fun initBinding() {
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        miniPlayer = binding.miniPlayer
        setContentView(binding.root)
    }

    override fun initData() {
        recommendActivityViewModel.getRecommendSong({
            val songDataArrayList = it.data.dailySongs.toStandardSongDataArrayList()
            runOnMainThread {
                binding.rvRecommendSong.layoutManager = LinearLayoutManager(this)
                binding.rvRecommendSong.adapter = DailyRecommendSongAdapter(it) { position ->
                    playMusic(this, songDataArrayList[position], songDataArrayList)
                }
            }
        }, {
            toast(it)
        })
    }

    override fun initView() {
        val radius = 20f
        val decorView: View = window.decorView
        val windowBackground: Drawable = decorView.background
        binding.blurView.setupWith(decorView.findViewById(R.id.clRecommend))
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(true)

        binding.tvDate.text = String.format("%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        binding.tvMonth.text = String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1)
    }

}