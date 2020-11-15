package com.newsfeeds.network

import android.accounts.NetworkErrorException
import android.os.Handler
import androidx.annotation.CallSuper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.newsfeeds.BuildConfig
import com.newsfeeds.R
import com.newsfeeds.base.BasePresenter
import com.newsfeeds.base.IBaseView
import com.newsfeeds.helper.ApplicationHelper
import com.newsfeeds.model.ErrorResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.HttpException

/**
 * BasePresenter Extension: only need to call subscribe
 */
fun <T : IBaseView, O> BasePresenter<T>.tryCall(
    observable: Observable<O>?,
    isWrapThread: Boolean = true
): Observable<O> {
    if (!this.isNetworkAvailable()) {
        this.iView.onFinishLoad()
        return Observable.error(NetworkErrorException(context?.resources?.getString(R.string.no_connection)))
    }

    if (isWrapThread)
        return observable?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())!!

    return observable!!
}

fun <T : IBaseView, O> BasePresenter<T>.downloadFile(
    observable: Observable<O>,
    isWrapThread: Boolean = true
): Observable<O> {
    if (!this.isNetworkAvailable()) {
        this.iView.onFinishLoad()
        return Observable.error(NetworkErrorException(context?.resources?.getString(R.string.no_connection)))
    }

    if (isWrapThread)
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    return observable
}

/**
 * ResponseObserver<T:BaseResponse> : Observer<T>
 * ~ this class will convert any error response to our error body format
 * @see BaseResponse -> BaseResponse.ErrorResponse
 */

open class ResponseObserver<T>(var mvp: IBaseView) : Observer<T> {
    var selfThread: Disposable? = null

    @CallSuper
    override fun onError(e: Throwable) {
        when (e) {
            is HttpException -> {
                try {
                    if (e.response()?.errorBody() != null) {
                        val errorBody: ResponseBody? = e.response()?.errorBody()
                        try {
                            val type = object : TypeToken<ErrorResponse>() {}.type
                            val errorResponse: ErrorResponse? =
                                Gson().fromJson(errorBody?.charStream(), type)
                            if ("${errorResponse?.fault?.message}".contains("500")) {
                                this.mvp.onError("Server Error, Please try again")
                            } else
                                this.mvp.onError("${errorResponse?.fault?.message}")
                        } catch (e: Exception) {
                            if (BuildConfig.DEBUG) {
                                e.printStackTrace()
                            }
                        }
                    }
                } catch (e1: Exception) {
                    ApplicationHelper.printstackTrace(e1)
                    this.mvp.onError("Failed to parsing response")
                }
            }

            else -> {
                ApplicationHelper.printstackTrace(e)
                if (BuildConfig.DEBUG || BuildConfig.BUILD_TYPE.equals("debug")) {
                    if ("${e.message}".contains("500")) {
                        this.mvp.onError("Server Error, Please try again")
                    } else
                        this.mvp.onError("${e.message}")
                } else if ("${e.message}".contains("500")) {
                    this.mvp.onError("Server Error, Please try again")
                } else if ("${e.message}".contains("Unable to resolve host")) {
                    this.mvp.onError("Oops, something wrong, please check your connection and try again")
                }
                this.mvp.onFailedRequest()
            }
        }
    }

    override fun onNext(result: T) {

    }

    override fun onSubscribe(d: Disposable) {
        selfThread = d
    }

    override fun onComplete() {

    }

    fun dismissAndDispose(withDelay: Boolean = false, delay: Long = 500) {
        if (withDelay) {
            Handler().postDelayed({
                mvp.onFinishLoad()
                selfThread?.dispose()
            }, delay)
            return
        }

        mvp.onFinishLoad()
        selfThread?.dispose()
    }

}
