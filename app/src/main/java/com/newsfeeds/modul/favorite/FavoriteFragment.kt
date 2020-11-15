package com.newsfeeds.modul.favorite

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.newsfeeds.R
import com.newsfeeds.base.BaseFragment
import com.newsfeeds.model.articlesearch.ByLine
import com.newsfeeds.model.articlesearch.DocsArticle
import com.newsfeeds.model.articlesearch.Headline
import com.newsfeeds.model.articlesearch.Multimedia
import com.newsfeeds.model.local.favorite.FavoriteNews
import com.newsfeeds.model.local.favorite.FavoriteNewsViewModel
import com.newsfeeds.modul.detailnewsparent.DetailNewsParentActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import org.koin.android.architecture.ext.viewModel

class FavoriteFragment : BaseFragment<FavoritePresenter>(), IFavoriteView {

    lateinit var mView: View

    private val favoriteNewsViewModel by viewModel<FavoriteNewsViewModel>()
    private var favoriteNewsList: MutableList<FavoriteNews>? = mutableListOf()
    private var compositeDisposable = CompositeDisposable()
    private lateinit var gridLayoutManager: GridLayoutManager
    lateinit var adapter: FavoriteNewsAdapter

    override fun attachPresenter(): FavoritePresenter {
        return FavoritePresenter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_favorite, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        onViewInit()
    }

    private fun initAdapter() {
        adapter = FavoriteNewsAdapter(requireContext())
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            adapter.setLandscapeView(true)
            gridLayoutManager = GridLayoutManager(requireContext(), 4)
        } else {
            adapter.setLandscapeView(false)
            gridLayoutManager = GridLayoutManager(requireContext(), 1)
        }
        mView.rvFavoriteNews.layoutManager = gridLayoutManager
        mView.rvFavoriteNews.setHasFixedSize(true)
        mView.rvFavoriteNews.isNestedScrollingEnabled = false
        mView.rvFavoriteNews.adapter = adapter
    }

    override fun onViewInit() {
        favoriteNewsViewModel.listenFavoriteNewsResult().observe(requireActivity(), Observer {
            loadData(it.toMutableList())
        })

        mView.ivDeleteFavorite.setOnClickListener {
            initDeleteFavorite()
        }
    }

    private fun loadData(favoriteNewsList: MutableList<FavoriteNews>?) {
        this.favoriteNewsList = favoriteNewsList
        this.favoriteNewsList?.let {
            adapter.updateData(it)
        }
        adapter.setOnItemClickListener { _, position ->
            val newsArticle: MutableList<DocsArticle> = mutableListOf()
            for (favoriteNews in favoriteNewsList!!) {
                newsArticle.add(
                    DocsArticle(
                        abstract = favoriteNews.abstract,
                        webUrl = "",
                        snippet = favoriteNews.snippet,
                        leadParagraph = favoriteNews.lead_paragraph,
                        source = favoriteNews.source,
                        multimedia = Gson().fromJson(
                            favoriteNews.multimedia,
                            Array<Multimedia>::class.java
                        ).toMutableList(),
                        headline = Gson().fromJson(favoriteNews.headline, Headline::class.java),
                        pubDate = favoriteNews.pubDate,
                        byline = Gson().fromJson(favoriteNews.byline, ByLine::class.java)
                    )
                )
            }
            DetailNewsParentActivity.start(requireContext(), position, newsArticle)
        }
    }

    private fun initDeleteFavorite() {
        val disposable = Observable.just(true)
            .observeOn(Schedulers.io())
            .doOnNext { favoriteNewsViewModel.deleteAllFavoriteNews() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        compositeDisposable.add(disposable)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (favoriteNewsList.isNullOrEmpty())
            return

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = GridLayoutManager(requireContext(), 4)
            mView.rvFavoriteNews.layoutManager = gridLayoutManager
            mView.rvFavoriteNews.adapter = adapter
            adapter.setLandscapeView(true)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = GridLayoutManager(requireContext(), 1)
            mView.rvFavoriteNews.layoutManager = gridLayoutManager
            mView.rvFavoriteNews.adapter = adapter
            adapter.setLandscapeView(false)
        }
    }

    override fun onError(message: String?) {
        showError(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}