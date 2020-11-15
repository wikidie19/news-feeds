package com.newsfeeds.modul.feeds

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.google.gson.Gson
import com.newsfeeds.R
import com.newsfeeds.base.BaseFragment
import com.newsfeeds.base.adapter.EndlessRecyclerOnScrollListener
import com.newsfeeds.helper.NetworkHelper
import com.newsfeeds.model.articlesearch.*
import com.newsfeeds.model.local.feeds.FeedsData
import com.newsfeeds.model.local.feeds.FeedsViewModel
import com.newsfeeds.modul.detailnewsparent.DetailNewsParentActivity
import com.newsfeeds.modul.feeds.adapter.FeedsAdapter
import com.newsfeeds.modul.search.SearchArticleActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_feeds.view.*
import org.koin.android.architecture.ext.viewModel

class FeedsFragment : BaseFragment<FeedsPresenter>(), IFeedsView {

    lateinit var mView: View

    private var newsPortraitAdapter: FeedsAdapter? = null
    private var page = 1
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener

    private val newsFeedsViewModel by viewModel<FeedsViewModel>()
    private var docsArticleList: MutableList<DocsArticle>? = mutableListOf()
    private var compositeDisposable = CompositeDisposable()

    override fun attachPresenter(): FeedsPresenter {
        return FeedsPresenter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_feeds, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        onViewInit()
        initLoadMore()

        mView.ivSearchNews.setOnClickListener {
            SearchArticleActivity.start(requireContext())
        }
    }

    private fun initAdapter() {
        newsPortraitAdapter = FeedsAdapter(requireContext())
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            newsPortraitAdapter?.setLandscapeView(true)
            gridLayoutManager = GridLayoutManager(requireContext(), 4)
        } else {
            newsPortraitAdapter?.setLandscapeView(false)
            gridLayoutManager = GridLayoutManager(requireContext(), 1)
        }

        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (newsPortraitAdapter?.isPositionFooter(position)!!) gridLayoutManager.spanCount else 1
            }
        }
        mView.rvNewsFeeds.layoutManager = gridLayoutManager
        mView.rvNewsFeeds.setHasFixedSize(true)
        mView.rvNewsFeeds.isNestedScrollingEnabled = false
        mView.rvNewsFeeds.adapter = newsPortraitAdapter
        newsPortraitAdapter?.isShowLoading = true
        newsPortraitAdapter?.setShowFooter(false)
    }

    private fun initLoadMore() {
        endlessRecyclerOnScrollListener =
            object : EndlessRecyclerOnScrollListener(gridLayoutManager, page, 10) {
                override fun onLoadMore(next: Int) {
                    page += 1
                    newsPortraitAdapter?.isShowLoading = false
                    loadData(page)
                }
            }

        mView.rvNewsFeeds.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    override fun onViewInit() {
        if (!NetworkHelper.isConnected(requireContext())) {
            newsFeedsViewModel.listenFeedsDataResult().observe(requireActivity(), Observer {
                loadDataOffline(it.toMutableList())
            })
        } else {
            initDeleteFeeds()
            loadData(page)
        }
    }

    private fun loadDataOffline(newsFeedsData: MutableList<FeedsData>?) {
        if (!newsFeedsData.isNullOrEmpty()) {
            newsFeedsData.let {
                for (feed in it) {
                    docsArticleList?.add(
                        DocsArticle(
                            abstract = feed.abstract,
                            webUrl = "",
                            snippet = feed.snippet,
                            leadParagraph = feed.lead_paragraph,
                            source = feed.source,
                            multimedia = Gson().fromJson(
                                feed.multimedia,
                                Array<Multimedia>::class.java
                            ).toMutableList(),
                            headline = Gson().fromJson(feed.headline, Headline::class.java),
                            pubDate = feed.pubDate,
                            byline = Gson().fromJson(feed.byline, ByLine::class.java)
                        )
                    )
                }
                newsPortraitAdapter?.updateData(docsArticleList!!)
            }
            newsPortraitAdapter?.setOnItemClickListener { _, position ->
                DetailNewsParentActivity.start(requireContext(), position - 1, docsArticleList)
            }
        }
    }

    private fun loadData(page: Int) {
        presenter?.getFeeds("", page)
    }

    override fun onSuccessGetFeeds(responseArticle: ResponseArticle?) {
        newsPortraitAdapter?.addAll(responseArticle?.docs)
        newsPortraitAdapter?.setOnItemClickListener { _, position ->
            DetailNewsParentActivity.start(requireContext(), position, newsPortraitAdapter?.items)
        }

        if (NetworkHelper.isConnected(requireContext())) {
            for (article in responseArticle?.docs!!) {
                val feedsData = FeedsData(
                    headlineMain = article.headline?.main,
                    abstract = article.abstract,
                    snippet = article.snippet,
                    lead_paragraph = article.leadParagraph,
                    source = article.source,
                    multimedia = Gson().toJson(article.multimedia),
                    headline = Gson().toJson(article.headline),
                    pubDate = article.pubDate,
                    byline = Gson().toJson(article.byline)
                )
                saveFeeds(feedsData)
            }
        }
    }

    override fun onStartLoad() {
        if (newsPortraitAdapter?.isShowLoading!!) {
            mView.rvNewsFeeds.showShimmerAdapter()
        } else {
            newsPortraitAdapter?.setShowFooter(true)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mView.rvNewsFeeds.removeOnScrollListener(endlessRecyclerOnScrollListener)
            gridLayoutManager = GridLayoutManager(requireContext(), 4)
            gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (newsPortraitAdapter?.isPositionFooter(position)!!) gridLayoutManager.spanCount else 1
                }
            }

            mView.rvNewsFeeds.layoutManager = gridLayoutManager
            newsPortraitAdapter?.setLandscapeView(true)

            mView.rvNewsFeeds.adapter = newsPortraitAdapter
            initLoadMore()

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mView.rvNewsFeeds.removeOnScrollListener(endlessRecyclerOnScrollListener)
            gridLayoutManager = GridLayoutManager(requireContext(), 1)
            gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (newsPortraitAdapter?.isPositionFooter(position)!!) gridLayoutManager.spanCount else 1
                }
            }

            mView.rvNewsFeeds.layoutManager = gridLayoutManager
            newsPortraitAdapter?.setLandscapeView(false)

            mView.rvNewsFeeds.adapter = newsPortraitAdapter
            initLoadMore()

        }
    }

    private fun saveFeeds(feedsData: FeedsData) {
        val disposable = Observable.just(true)
            .observeOn(Schedulers.io())
            .doOnNext { newsFeedsViewModel.saveFeedsData(feedsData) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        compositeDisposable.add(disposable)
    }

    private fun initDeleteFeeds() {
        val disposable = Observable.just(true)
            .observeOn(Schedulers.io())
            .doOnNext { newsFeedsViewModel.deleteAllFeedsData() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        compositeDisposable.add(disposable)
    }

    override fun onFinishLoad() {
        mView.rvNewsFeeds.hideShimmerAdapter()
        newsPortraitAdapter?.setShowFooter(false)
    }

    override fun onError(message: String?) {
        showError(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}