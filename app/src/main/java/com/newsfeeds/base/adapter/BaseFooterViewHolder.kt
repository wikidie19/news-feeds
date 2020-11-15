package com.newsfeeds.base.adapter

import android.view.View

abstract class BaseFooterViewHolder(itemView: View?) : BaseViewHolder<Any?>(itemView!!, null) {
    abstract fun show()
    abstract fun hide()
}