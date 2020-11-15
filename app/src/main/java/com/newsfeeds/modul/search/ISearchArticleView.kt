package com.newsfeeds.modul.search

import com.newsfeeds.base.IBaseView
import com.newsfeeds.model.articlesearch.ResponseArticle

interface ISearchArticleView: IBaseView {

    fun onSuccessGetFeeds(responseArticle: ResponseArticle?) {}

}