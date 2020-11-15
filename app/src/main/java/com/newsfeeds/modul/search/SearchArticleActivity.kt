package com.newsfeeds.modul.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsfeeds.R
import com.newsfeeds.base.BaseActivity
import com.newsfeeds.base.adapter.EndlessRecyclerOnScrollListener
import com.newsfeeds.helper.LoggerHelper
import com.newsfeeds.model.articlesearch.ResponseArticle
import com.newsfeeds.modul.feeds.adapter.FeedsAdapter
import kotlinx.android.synthetic.main.activity_search_article.*

class SearchArticleActivity: BaseActivity<SearchArticlePresenter>(), ISearchArticleView {

    lateinit var linearLayoutManager: LinearLayoutManager
    private var newsPortraitAdapter: FeedsAdapter? = null
    private var page = 1
    private var query: String? = ""

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
        tvCancel.setOnClickListener {
            finish()
        }

        etSearchNews.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val searchText = etSearchNews.text.toString().trim { it <= ' ' }
                if ((searchText.length > 2) or (searchText.isEmpty())) {
                    query = searchText
                    newsPortraitAdapter?.clear()
                    page = 1
                    newsPortraitAdapter?.isShowLoading = true
                    loadData(page)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
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
        newsPortraitAdapter?.setOnItemClickListener { view, position ->
            LoggerHelper.error(newsPortraitAdapter?.getItem(position)?.headline!!)
        }
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
}