package com.newsfeeds.network

import com.google.gson.JsonObject
import com.newsfeeds.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class RetrofitException internal constructor(
    message: String?,
    val code: Int,
    val url: String?,
    val response: Response<*>?,
    val kind: Kind,
    exception: Throwable?,
    val retrofit: Retrofit?
) : RuntimeException(message, exception) {
    /**
     * Identifies the event kind which triggered a [RetrofitException].
     */
    enum class Kind {
        /**
         * An [IOException] occurred while communicating to the server.
         */
        NETWORK,

        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,

        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    /**
     * The request URL which produced the error.
     */
    /**
     * Response object containing status code, headers, body, etc.
     */
    /**
     * The event kind which triggered this error.
     */
    /**
     * The Retrofit this request was executed on
     */

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>?): T? {
        if (response?.errorBody() == null) {
            return null
        }
        val converter: Converter<ResponseBody?, T> =
            retrofit!!.responseBodyConverter(type, arrayOfNulls(0))
        return converter.convert(response.errorBody())
    }

    companion object {
        @JvmStatic
        fun httpError(
            url: String?,
            response: Response<*>,
            retrofit: Retrofit
        ): RetrofitException {
            var message = response.code().toString() + " " + response.message()
            val code = 0
            if (response.errorBody() != null) {
                val converter =
                    retrofit.responseBodyConverter<JsonObject>(
                        JsonObject::class.java, arrayOfNulls(0)
                    )
                try {
                    val errorBody = response.errorBody()
                    val error = converter.convert(errorBody)
                    if (error?.get("message") != null) {
                        message = error.get("message").asString
                    }
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                }
            }
            return RetrofitException(
                message,
                code,
                url,
                response,
                Kind.HTTP,
                null,
                retrofit
            )
        }

        @JvmStatic
        fun networkOffline(exception: NoConnectivityException): RetrofitException {
            return RetrofitException(
                exception.message,
                0,
                null,
                null,
                Kind.NETWORK,
                exception,
                null
            )
        }

        @JvmStatic
        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(
                exception.message,
                0,
                null,
                null,
                Kind.NETWORK,
                exception,
                null
            )
        }

        @JvmStatic
        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(
                exception.message,
                0,
                null,
                null,
                Kind.UNEXPECTED,
                exception,
                null
            )
        }
    }

}