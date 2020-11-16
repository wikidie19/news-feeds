package com.newsfeeds.modul.detailnewsparent.detailnews

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.newsfeeds.R
import com.newsfeeds.base.BaseFragment
import com.newsfeeds.constant.Constant
import com.newsfeeds.helper.DateHelper
import com.newsfeeds.model.articlesearch.DocsArticle
import com.newsfeeds.model.local.favorite.FavoriteNews
import com.newsfeeds.model.local.favorite.FavoriteNewsViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_detail_news.view.*
import org.koin.android.architecture.ext.viewModel

class DetailNewsFragment : BaseFragment<DetailNewsPresenter>(), IDetailNewsView {

    lateinit var mView: View

    private val favoriteNewsViewModel by viewModel<FavoriteNewsViewModel>()
    private var compositeDisposable = CompositeDisposable()
    private var isFavorite = false

    companion object {

        private var newsArticle: DocsArticle? = null

        fun newInstance(newsArticle: DocsArticle?): DetailNewsFragment {
            val bundle = Bundle()
            val fragment = DetailNewsFragment()
            this.newsArticle = newsArticle
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_detail_news, container, false)
        return mView
    }

    override fun attachPresenter(): DetailNewsPresenter {
        return DetailNewsPresenter(requireContext(), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onViewInit()
    }

    override fun onViewInit() {
        favoriteNewsViewModel.listenFavoriteNewsResult().observe(requireActivity(), Observer {
            initViewFavoriteNews(it.toMutableList())
        })

        mView.tvNewsTitle.text = newsArticle?.headline?.main
        mView.tvNewsSnippet.text = newsArticle?.snippet

        if (!newsArticle?.multimedia.isNullOrEmpty()) {
            Glide.with(requireActivity())
                .load("${Constant.StaticUrl.baseImageUrl}${newsArticle?.multimedia?.get(0)?.url}")
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(true)
                )
                .into(mView.ivNewsDetail)
        }

        mView.tvAuthor.text = newsArticle?.byline?.original
        mView.tvPublishTime.text = "Published ${DateHelper.dateStr(
            DateHelper.dateObj(
                "${newsArticle?.pubDate}",
                "yyyy-MM-dd'T'HH:mm:ss"
            ), "MMM. dd, yyyy"
        )}"
        mView.tvNewsParagraph.text = newsArticle?.leadParagraph

        mView.ivFavoriteNews.setOnClickListener {
            if (!isFavorite) {
                val favoriteNews =
                    FavoriteNews(
                        headlineMain = newsArticle?.headline?.main,
                        abstract = newsArticle?.abstract,
                        snippet = newsArticle?.snippet,
                        lead_paragraph = newsArticle?.leadParagraph,
                        source = newsArticle?.source,
                        multimedia = Gson().toJson(newsArticle?.multimedia),
                        headline = Gson().toJson(newsArticle?.headline),
                        pubDate = newsArticle?.pubDate,
                        byline = Gson().toJson(newsArticle?.byline)
                    )
                saveFavoriteNews(favoriteNews)
            } else {
                removeFavoriteNews(newsArticle?.headline?.main)
            }
        }

    }

    private fun initViewFavoriteNews(favoriteNewsList: MutableList<FavoriteNews>?) {
        if (!isAdded)
            return
        favoriteNewsList?.let {
            val filterFavoriteNews = favoriteNewsList.filter {
                it.headlineMain == newsArticle?.headline?.main
            }

            if (!filterFavoriteNews.isNullOrEmpty()) {
                isFavorite = true
                mView.ivFavoriteNews.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.nb_red
                    ), PorterDuff.Mode.SRC_IN
                )
            } else {
                isFavorite = false
                mView.ivFavoriteNews.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.nb_grey
                    ), PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    private fun saveFavoriteNews(favoriteNews: FavoriteNews) {
        val disposable = Observable.just(true)
            .observeOn(Schedulers.io())
            .doOnNext { favoriteNewsViewModel.saveFavoriteNews(favoriteNews) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        compositeDisposable.add(disposable)
    }

    private fun removeFavoriteNews(headlineMain: String?) {
        val disposable = Observable.just(true)
            .observeOn(Schedulers.io())
            .doOnNext { favoriteNewsViewModel.deleteItem(headlineMain) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        compositeDisposable.add(disposable)
    }

    override fun onError(message: String?) {
        showError(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}