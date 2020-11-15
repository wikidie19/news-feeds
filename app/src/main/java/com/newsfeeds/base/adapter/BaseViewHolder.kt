package com.newsfeeds.base.adapter

import android.view.View
import android.view.View.OnLongClickListener
import androidx.recyclerview.widget.RecyclerView
import com.newsfeeds.base.adapter.BaseListAdapter.OnLongItemClickListener

abstract class BaseViewHolder<T> : RecyclerView.ViewHolder, View.OnClickListener,
    OnLongClickListener {
    private var onItemClickListener: BaseListAdapter.OnItemClickListener?
    private var onLongItemClickListener: OnLongItemClickListener? = null

    constructor(
        itemView: View,
        onItemClickListener: BaseListAdapter.OnItemClickListener?
    ) : super(itemView) {
        this.onItemClickListener = onItemClickListener
        itemView.setOnClickListener(this)
    }

    constructor(
        itemView: View,
        onItemClickListener: BaseListAdapter.OnItemClickListener?,
        onLongItemClickListener: OnLongItemClickListener?
    ) : super(itemView) {
        this.onItemClickListener = onItemClickListener
        this.onLongItemClickListener = onLongItemClickListener
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    abstract fun bind(item: T)
    override fun onClick(view: View) {
        if (onItemClickListener != null) {
            onItemClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    override fun onLongClick(view: View): Boolean {
        if (onLongItemClickListener != null) {
            onLongItemClickListener!!.onLongItemClick(view, adapterPosition)
        }
        return true
    }
}