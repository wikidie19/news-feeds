package com.newsfeeds.base

import android.content.Context
import com.newsfeeds.helper.NetworkHelper
import com.newsfeeds.helper.SharedHelper
import com.newsfeeds.network.ApiClient
import com.newsfeeds.network.ApiInterface
import io.reactivex.disposables.Disposable
import retrofit2.Retrofit

/**
 * This class handle for networking request
 * @see ApiClient to view setting of request data from API
 * All presenter must implement IBaseView to call function in class
 * @see IBaseView
 * @see ApiInterface to adding parameter for get or post data from API
 */
open class BasePresenter<T : IBaseView>(var context: Context?, var iView: T) {

    var shared: SharedHelper
    var service: ApiInterface? = null
    var disposable: Disposable? = null
    var retrofit: Retrofit? = null
    var retrofitAdmin: Retrofit? = null

    init {
        retrofit = ApiClient.instance(context!!)
        service = retrofit?.create(ApiInterface::class.java)
        this.shared = SharedHelper(context!!)
    }

    /**
     * Check available network connection
     */
    fun isNetworkAvailable(): Boolean {
        return NetworkHelper.isConnected(context!!)
    }

    fun sessionLogout() {
        shared.clear()
    }

}
