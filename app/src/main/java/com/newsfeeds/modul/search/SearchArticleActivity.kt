package com.newsfeeds.modul.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsfeeds.R
import com.newsfeeds.base.BaseActivity
import com.newsfeeds.base.adapter.EndlessRecyclerOnScrollListener
import com.newsfeeds.model.articlesearch.ResponseArticle
import com.newsfeeds.model.local.search.SearchQuery
import com.newsfeeds.model.local.search.SearchQueryViewModel
import com.newsfeeds.modul.detailnewsparent.DetailNewsParentActivity
import com.newsfeeds.modul.feeds.adapter.FeedsAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_article.*
import org.koin.android.architecture.ext.viewModel
import java.util.*

class SearchArticleActivity : BaseActivity<SearchArticlePresenter>(), ISearchArticleView {

    lateinit var linearLayoutManager: LinearLayoutManager
    private var newsPortraitAdapter: FeedsAdapter? = null
    private var page = 1
    private var query: String? = ""
    private var timer: Timer? = null

    private val searchQueryViewModel by viewModel<SearchQueryViewModel>()
    private var compositeDisposable = CompositeDisposable()

    var listQuery: MutableList<String> = mutableListOf()

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SearchArticleActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun attachPresenter(): SearchArticlePresenter {
        return SearchArticlePresenter(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_article)

        initAdapter()
        onViewInit()
        initLoadMore()
    }

    private fun initAdapter() {
        newsPortraitAdapter = FeedsAdapter(this)
        linearLayoutManager = LinearLayoutManager(this)
        rvNewsSearch.layoutManager = linearLayoutManager
        rvNewsSearch.setHasFixedSize(true)
        rvNewsSearch.isNestedScrollingEnabled = false
        rvNewsSearch.adapter = newsPortraitAdapter
        newsPortraitAdapter?.isShowLoading = true
        newsPortraitAdapter?.setShowFooter(false)
    }

    override fun onViewInit() {

        searchQueryViewModel.listenSearchQueryDataResult().observe(this, Observer {
            initLastSearchQuery(it.toMutableList())
        })

        tvCancel.setOnClickListener {
            finish()
        }

        ivBack.setOnClickListener {
            finish()
        }

        etSearchNews.onFocusChangeListener = View.OnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                etSearchNews.showDropDown()
            }
        }

        etSearchNews.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val searchText = etSearchNews.text.toString().trim { it <= ' ' }
                if ((searchText.length > 2) or (searchText.isEmpty())) {
                    timer = Timer()
                    timer?.schedule(object : TimerTask() {
                        override fun run() {
                            runOnUiThread {
                                query = searchText
                                newsPortraitAdapter?.clear()
                                page = 1
                                newsPortraitAdapter?.isShowLoading = true
                                loadData(page)

                                etSearchNews.dismissDropDown()
                                hideKeyboard()

                                saveSearchQuery(SearchQuery(queryValue = query))
                            }
                        }
                    }, 1000)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (timer != null) {
                    timer?.cancel()
                }
            }

        })

    }

    private fun initLastSearchQuery(searchQueryList: MutableList<SearchQuery>) {
        for (query in searchQueryList) {
            listQuery.add("${query.queryValue}")
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            listQuery
        )
        etSearchNews.setAdapter(adapter)

        etSearchNews.setOnItemClickListener { _, _, position, _ ->
            query = adapter.getItem(position)
            newsPortraitAdapter?.clear()
            page = 1
            newsPortraitAdapter?.isShowLoading = true
            loadData(page)

            hideKeyboard()
        }

    }

    private fun initLoadMore() {
        rvNewsSearch.addOnScrollListener(object :
            EndlessRecyclerOnScrollListener(linearLayoutManager, page, 10) {
            override fun onLoadMore(next: Int) {
                page += 1
                newsPortraitAdapter?.isShowLoading = false
                loadData(page)
            }
        })
    }

    private fun loadData(page: Int) {
        presenter?.getFeeds(query, page)
    }

    override fun onSuccessGetFeeds(responseArticle: ResponseArticle?) {
        newsPortraitAdapter?.addAll(responseArticle?.docs)
        newsPortraitAdapter?.setOnItemClickListener { _, position ->
            DetailNewsParentActivity.start(this, position, responseArticle?.docs)
        }
    }

    private fun saveSearchQuery(searchQuery: SearchQuery) {
        val disposable = Observable.just(true)
            .observeOn(Schedulers.io())
            .doOnNext { searchQueryViewModel.saveSearchQueryData(searchQuery) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        compositeDisposable.add(disposable)
    }

    override fun onStartLoad() {
        if (newsPortraitAdapter?.isShowLoading!!) {
            progressBar.visibility = View.VISIBLE
        } else {
            newsPortraitAdapter?.setShowFooter(true)
        }
    }

    override fun onFinishLoad() {
        progressBar.visibility = View.GONE
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