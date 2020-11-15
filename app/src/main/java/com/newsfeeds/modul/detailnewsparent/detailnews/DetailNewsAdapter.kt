package com.newsfeeds.modul.detailnewsparent.detailnews

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.newsfeeds.model.articlesearch.DocsArticle

class DetailNewsAdapter(fm: FragmentActivity, articleList: MutableList<DocsArticle>?) :
    FragmentStateAdapter(fm) {

    private var articleList: MutableList<DocsArticle>? = mutableListOf()

    init {
        this.articleList = articleList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return articleList?.size!!
    }

    override fun createFragment(position: Int): Fragment {
        return DetailNewsFragment.newInstance(articleList?.get(position))
    }

}