package com.newsfeeds.base

interface IBaseView {
    fun onViewInit()
    fun onError(message: String?)

    //optionals function
    fun onStartLoad() {}
    fun onFinishLoad() {}
    fun onFailedRequest() {}

}
