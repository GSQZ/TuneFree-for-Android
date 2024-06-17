package com.dirror.music.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import com.dirror.music.R
import com.dirror.music.databinding.DialogNoticeBinding
import com.dirror.music.util.*

class NoticeDialog(context: Context, private val noticeData: NoticeUtil.NoticeData, confirm: (NoticeDialog)-> Unit): Dialog(context, R.style.style_default_dialog) {

    private var binding: DialogNoticeBinding = DialogNoticeBinding.inflate(layoutInflater)
    private var confirm = confirm

    init {
        setContentView(binding.root)
        // 设置显示位置
        window?.setGravity(Gravity.BOTTOM)
        // 设置大小
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        // 不能点击外部关闭 Dialog
        setCanceledOnTouchOutside(false)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvTitle.text = noticeData.title
        binding.tvContent.text = noticeData.msg
        binding.tvName.text = noticeData.name
        binding.tvTime.text = noticeData.time


//        updateData.tagVersion?.let {
//            if (getVisionCode() < it) {
//                toast("过低版本，请更新")
//                openUrlByBrowser(this.context, updateData.url)
//                binding.btnCancel.visibility = View.GONE
//            }
//        }

        binding.btnConfirm.setOnClickListener {
//            dismiss()
            confirm.invoke(this)
        }
//
//        binding.btnDownload.setOnClickListener {
//            openUrlByBrowser(this.context, updateData.url)
//        }
    }

}