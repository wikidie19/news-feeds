package com.newsfeeds.modul.feeds.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.newsfeeds.R
import com.newsfeeds.base.adapter.BaseFooterViewHolder
import com.newsfeeds.base.adapter.BaseListAdapter
import com.newsfeeds.base.adapter.BaseViewHolder
import com.newsfeeds.constant.Constant
import com.newsfeeds.helper.DateHelper
import com.newsfeeds.model.articlesearch.DocsArticle
import com.newsfeeds.model.local.feeds.FeedsData
import kotlinx.android.synthetic.main.list_news_portrait.view.*

class FeedsAdapter(context: Context) :
    BaseListAdapter<DocsArticle, BaseViewHolder<*>>(context) {

    private var showFooter = false
    var isShowLoading = false
    var isLandscape = false

    init {
        withFooter = true
    }

    fun setLandscapeView(isLandscape: Boolean) {
        this.isLandscape = isLandscape
    }

    fun setShowFooter(showFooter: Boolean) {
        this.showFooter = showFooter
    }

    override fun getItemCount(): Int {
        return if (items.size > 0) items.size + 1 else 0
    }

    override fun getItemResourceLayout(viewType: Int): Int {
        if (viewType == TYPE_ITEM) {
            if (isLandscape) {
                return R.layout.list_news_landscape
            } else {
                return R.layout.list_news_portrait
            }
        } else {
            return R.layout.loading_footer
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionFooter(position)) TYPE_FOOTER else TYPE_ITEM
    }

    fun isPositionFooter(position: Int): Boolean {
        return position == items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        var holding: BaseViewHolder<*>? = null
        if (viewType == TYPE_ITEM) {
            holding = ViewHolder(getView(parent, viewType))
        } else if (viewType == TYPE_FOOTER) {
            holding = FooterViewHolder(getView(parent, viewType))
        }

        return holding!!
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is ViewHolder) {
            super.onBindViewHolder(holder, position)
        } else if (holder is FooterViewHolder) {
            if (showFooter) holder.show() else holder.hide()
        }
    }

    fun updateData(feedsData: MutableList<DocsArticle>) {
        this.items.clear()
        this.items.addAll(feedsData)
        notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(val view: View) :
        BaseViewHolder<DocsArticle>(view, onItemClickListener) {

        override fun bind(docsArticle: DocsArticle) {
            if (!docsArticle.multimedia.isNullOrEmpty()) {
                Glide.with(context)
                    .load("${Constant.StaticUrl.baseImageUrl}${docsArticle.multimedia?.get(0)?.url}")
                    .apply(
                        RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .skipMemoryCache(true)
                    )
                    .into(view.ivNewsThumbnail)
            }

            view.tvNewsTitle.text = docsArticle.headline?.main
            view.tvNewsSnippet.text = docsArticle.snippet
            view.tvPublishTime.text = docsArticle.pubDate
            view.tvPublishTime.text = DateHelper.getTimeAgo(
                DateHelper.dateObj(
                    "${docsArticle.pubDate}",
                    "yyyy-MM-dd'T'HH:mm:ss"
                ).time
            )
        }

    }

    inner class FooterViewHolder internal constructor(itemView: View) :
        BaseFooterViewHolder(itemView) {

        private val progressBar: ProgressBar = itemView.findViewById(R.id.pg_load_more)

        override fun show() {
            progressBar.visibility = View.VISIBLE
        }

        override fun hide() {
            progressBar.visibility = View.GONE
        }

        override fun bind(item: Any?) {

        }

    }

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }

}