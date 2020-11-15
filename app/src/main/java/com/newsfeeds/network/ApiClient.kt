package com.newsfeeds.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.newsfeeds.BuildConfig
import com.newsfeeds.R
import com.newsfeeds.helper.ApplicationHelper
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * This class is handle networking request to API
 */
class ApiClient {

    private var retrofit: Retrofit? = null

    companion object {
        var context: Context? = null

        fun instance(context: Context): Retrofit {
            Companion.context = context
            return ApiClient().getClient(context)
        }
    }

    /**
     * Set base url API
     */
    private fun baseUrl(): String {
        return BuildConfig.BASE_URL
    }

    /**
     * Call service with retrofit
     */
    private fun getClient(context: Context): Retrofit {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(context.cacheDir, cacheSize.toLong())

        val okhttpBuilder = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(HeaderInterceptor())
        if (BuildConfig.DEBUG) {
            okhttpBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl())
            .client(okhttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
            .build()
        return retrofit!!
    }

    /**
     * Setting header
     * Insert token form local memory, token receive from login response
     * Insert player_id for notification service, generate from BaseApplication
     * @see BaseApplication
     */
    internal class HeaderInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val ongoing = chain.request().newBuilder()
            ongoing.addHeader("Content-Type", "application/json")
            val newRequest = ongoing.build()
            return onOnIntercept(chain, newRequest)
        }

        @Throws(IOException::class)
        private fun onOnIntercept(chain: Interceptor.Chain, request: Request): Response {
            try {
                return chain.proceed(request)
            } catch (e: SocketTimeoutException) {
                ApplicationHelper.printstackTrace(e)
                val response = chain.proceed(chain.request())
                return response.newBuilder().body(
                    context?.resources?.getString(R.string.host_not_found)?.let {
                        ResponseBody.create(
                            response.body?.contentType(),
                            it
                        )
                    }
                ).build()
            } catch (e: UnknownHostException) {
                ApplicationHelper.printstackTrace(e)
                val response = chain.proceed(chain.request())
                return response.newBuilder().body(
                    context?.resources?.getString(R.string.host_not_found)?.let {
                        ResponseBody.create(
                            response.body?.contentType(),
                            it
                        )
                    }
                ).build()
            }
        }

    }
}
