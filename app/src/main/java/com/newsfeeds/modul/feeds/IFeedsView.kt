package com.newsfeeds.modul.feeds

import com.newsfeeds.base.IBaseView
import com.newsfeeds.model.articlesearch.ResponseArticle

interface IFeedsView: IBaseView {

    fun onSuccessGetFeeds(responseArticle: ResponseArticle?) {}

}