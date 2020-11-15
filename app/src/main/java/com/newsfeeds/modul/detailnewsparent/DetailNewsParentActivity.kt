package com.newsfeeds.modul.detailnewsparent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.newsfeeds.R
import com.newsfeeds.base.BaseActivity
import com.newsfeeds.model.articlesearch.DocsArticle
import com.newsfeeds.modul.detailnewsparent.detailnews.DetailNewsAdapter
import kotlinx.android.synthetic.main.activity_detail_news_parent.*

class DetailNewsParentActivity : BaseActivity<DetailNewsParentPresenter>(), IDetailNewsParentView {

    lateinit var adapter: DetailNewsAdapter

    companion object {

        private var articleList: MutableList<DocsArticle>? = mutableListOf()
        private var selectedPosition = 0

        fun start(context: Context, position: Int, articleList: MutableList<DocsArticle>?) {
            val intent = Intent(context, DetailNewsParentActivity::class.java)
            this.articleList = articleList
            this.selectedPosition = position
            context.startActivity(intent)
        }
    }

    override fun attachPresenter(): DetailNewsParentPresenter {
        return DetailNewsParentPresenter(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news_parent)

        onViewInit()
    }

    override fun onViewInit() {
        val adapter = DetailNewsAdapter(this, articleList)
        vpDetailNews.adapter = adapter
        vpDetailNews.setCurrentItem(selectedPosition, true)

        ivBack.setOnClickListener {
            finish()
        }

    }

    override fun onError(message: String?) {
        showError(message)
    }

}