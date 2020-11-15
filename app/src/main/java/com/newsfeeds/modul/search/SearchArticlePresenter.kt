package com.newsfeeds.modul.search

import android.content.Context
import com.newsfeeds.BuildConfig
import com.newsfeeds.base.BasePresenter
import com.newsfeeds.base.BaseResponse
import com.newsfeeds.model.articlesearch.ResponseArticle
import com.newsfeeds.network.ResponseObserver
import com.newsfeeds.network.tryCall
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchArticlePresenter(context: Context, iView: ISearchArticleView): BasePresenter<ISearchArticleView>(context, iView) {

    fun getFeeds(query: String?, page: Int?) {
        iView.onStartLoad()

        val request = tryCall(service?.getFeedsArticle(query, BuildConfig.API_KEY, page))
        request.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ResponseObserver<BaseResponse<ResponseArticle>>(iView) {
                override fun onComplete() {
                    dismissAndDispose()
                }

                override fun onNext(result: BaseResponse<ResponseArticle>) {
                    iView.onSuccessGetFeeds(result.response)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    dismissAndDispose()
                }
            })

    }

}