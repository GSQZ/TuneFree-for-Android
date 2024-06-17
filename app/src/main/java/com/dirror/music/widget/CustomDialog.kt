package com.dirror.music.widget

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.dirror.music.R

class CustomDialog: Dialog {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, theme: Int) : super(context, theme) {}

    class Builder(context: Context) {
        private var title: String? = null
        private var message: String? = null
        private var contentView: View? = null
        private var positiveButtonText: String? = null
        private var negativeButtonText: String? = null
        private var singleButtonText: String? = null
        private var positiveButtonClickListener: View.OnClickListener? = null
        private var negativeButtonClickListener: View.OnClickListener? = null
        private var singleButtonClickListener: View.OnClickListener? = null

        private val layout: View
        private val dialog: CustomDialog = CustomDialog(context, R.style.CustomDialog)

        init {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.custom_dialog, null)
            dialog.addContentView(layout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }

        fun setTitle(title: String): Builder{
            this.title = title
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setContentView(v: View): Builder {
            this.contentView = v
            return this
        }

        fun setPositiveButton(positiveButtonText: String, listener: View.OnClickListener): Builder {
            this.positiveButtonText = positiveButtonText
            this.positiveButtonClickListener = listener
            return this
        }

        fun setNegativeButton(negativeButtonText: String, listener: View.OnClickListener): Builder {
            this.negativeButtonText = negativeButtonText
            this.negativeButtonClickListener = listener
            return this
        }

        fun setSingleButton(singleButtonText: String, listener: View.OnClickListener): Builder {
            this.singleButtonText = singleButtonText
            this.singleButtonClickListener = listener
            return this
        }

        /**
         * 创建单按钮对话框
         * @return
         */
        fun createSingleButtonDialog(): CustomDialog {
            showSingleButton()
            layout.findViewById<View>(R.id.singleBtn).setOnClickListener(singleButtonClickListener)
            //如果传入的按钮文字为空，则使用默认的“知道了”
            if (singleButtonText != null) {
                (layout.findViewById<View>(R.id.singleBtn) as TextView).text = singleButtonText
            } else {
                (layout.findViewById<View>(R.id.singleBtn) as TextView).text = "知道了"
            }
            create()
            return dialog
        }

        /**
         * 创建双按钮对话框
         * @return
         */
        fun createTwoButtonDialog(): CustomDialog {
            showTwoButton()
            layout.findViewById<View>(R.id.positiveBtn).setOnClickListener(positiveButtonClickListener)
            layout.findViewById<View>(R.id.negativeBtn).setOnClickListener(negativeButtonClickListener)
            //如果传入的按钮文字为空，则使用默认的“确定”和“取消”
            if (positiveButtonText != null) {
                (layout.findViewById<View>(R.id.positiveBtn) as TextView).text = positiveButtonText
            } else {
                (layout.findViewById<View>(R.id.positiveBtn) as TextView).text = "确定"
            }
            if (negativeButtonText != null) {
                (layout.findViewById<View>(R.id.negativeBtn) as TextView).text = negativeButtonText
            } else {
                (layout.findViewById<View>(R.id.negativeBtn) as TextView).text = "取消"
            }
            create()
            return dialog
        }

        /**
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        private fun create() {
            if (message != null) {      //设置提示内容
                (layout.findViewById<View>(R.id.message) as TextView).text = message
            } else if (contentView != null) {       //如果使用Builder的setContentview()方法传入了布局，则使用传入的布局
                (layout.findViewById<View>(R.id.content) as LinearLayout).removeAllViews()
                (layout.findViewById<View>(R.id.content) as LinearLayout)
                    .addView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
            if(title!=null && title!!.isNotEmpty()){
                (layout.findViewById<View>(R.id.title) as TextView).text = title
                showTitle()
            }
            dialog.setContentView(layout)
            dialog.setCancelable(true)     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false)        //用户不能通过点击对话框之外的地方取消对话框显示
        }

        /**
         * 显示双按钮布局，隐藏单按钮
         */
        private fun showTwoButton() {
            layout.findViewById<View>(R.id.singleButtonLayout).visibility = View.GONE
            layout.findViewById<View>(R.id.twoButtonLayout).visibility = View.VISIBLE
        }

        /**
         * 显示单按钮布局，隐藏双按钮
         */
        private fun showSingleButton() {
            layout.findViewById<View>(R.id.singleButtonLayout).visibility = View.VISIBLE
            layout.findViewById<View>(R.id.twoButtonLayout).visibility = View.GONE
        }

        private fun showTitle() {
            layout.findViewById<View>(R.id.title).visibility = View.VISIBLE
        }

    }
}