package com.newsfeeds.modul.favorite

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.newsfeeds.R
import com.newsfeeds.base.adapter.BaseListAdapter
import com.newsfeeds.base.adapter.BaseViewHolder
import com.newsfeeds.constant.Constant
import com.newsfeeds.helper.DateHelper
import com.newsfeeds.model.articlesearch.Headline
import com.newsfeeds.model.articlesearch.Multimedia
import com.newsfeeds.model.local.favorite.FavoriteNews
import kotlinx.android.synthetic.main.list_news_portrait.view.*


class FavoriteNewsAdapter(context: Context) :
    BaseListAdapter<FavoriteNews, BaseViewHolder<*>>(context) {

    private var isLandscape = false

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(favoriteNewsList: MutableList<FavoriteNews>) {
        this.items.clear()
        this.items.addAll(favoriteNewsList)
        notifyDataSetChanged()
    }

    fun setLandscapeView(isLandscape: Boolean) {
        this.isLandscape = isLandscape
    }

    override fun getItemResourceLayout(viewType: Int): Int {
        if (isLandscape) {
            return R.layout.list_news_landscape
        } else {
            return R.layout.list_news_portrait
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return ViewHolder(getView(parent, viewType), onItemClickListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is ViewHolder) {
            super.onBindViewHolder(holder, position)
        }
    }

    inner class ViewHolder internal constructor(
        val view: View,
        onItemClickListener: OnItemClickListener
    ) : BaseViewHolder<FavoriteNews>(view, onItemClickListener) {

        override fun bind(item: FavoriteNews) {

            val multimedia =
                Gson().fromJson(item.multimedia, Array<Multimedia>::class.java).toList()

            if (!item.multimedia.isNullOrEmpty()) {
                Glide.with(context)
                    .load("${Constant.StaticUrl.baseImageUrl}${multimedia.get(0).url}")
                    .apply(
                        RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .skipMemoryCache(true)
                    )
                    .into(view.ivNewsThumbnail)
            }

            val headline = Gson().fromJson(item.headline, Headline::class.java)

            view.tvNewsTitle.text = headline?.main
            view.tvNewsSnippet.text = item.snippet
            view.tvPublishTime.text = item.pubDate
            view.tvPublishTime.text = DateHelper.getTimeAgo(
                DateHelper.dateObj(
                    "${item.pubDate}",
                    "yyyy-MM-dd'T'HH:mm:ss"
                ).time
            )
        }

    }
}
