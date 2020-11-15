package com.newsfeeds.modul.detailnewsparent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.viewpager.widget.ViewPager
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
        val adapter = DetailNewsAdapter(supportFragmentManager, articleList)
        vpDetailNews.adapter = adapter

        vpDetailNews.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                selectedPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        Handler().postDelayed({
            selectedPosition = if (selectedPosition >= articleList?.size!!) {
                0
            } else {
                selectedPosition
            }
            vpDetailNews.setCurrentItem(selectedPosition, true)
        }, 10)


    }

    override fun onError(message: String?) {
        showError(message)
    }

}