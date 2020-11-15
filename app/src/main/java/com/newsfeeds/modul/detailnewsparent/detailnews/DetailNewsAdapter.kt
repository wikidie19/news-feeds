package com.newsfeeds.modul.detailnewsparent.detailnews

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.newsfeeds.model.articlesearch.DocsArticle

class DetailNewsAdapter(fm: FragmentManager, articleList: MutableList<DocsArticle>?) :
    FragmentStatePagerAdapter(fm) {

    private var articleList: MutableList<DocsArticle>? = mutableListOf()

    init {
        this.articleList = articleList
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return DetailNewsFragment.newInstance(articleList?.get(position))
    }

    override fun getCount(): Int {
        return articleList?.size!!
    }

    override fun finishUpdate(container: ViewGroup) {
        try {
            super.finishUpdate(container)
        } catch (nullPointerException: NullPointerException) {
            nullPointerException.printStackTrace()
        }
    }

}