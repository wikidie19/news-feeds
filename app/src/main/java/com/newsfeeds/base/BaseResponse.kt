package com.newsfeeds.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BaseResponse<T> {

    @SerializedName("status")
    var status: String? = ""

    @SerializedName("copyright")
    var copyright: String? = ""

    @SerializedName("response")
    @Expose
    var response: T? = null

}