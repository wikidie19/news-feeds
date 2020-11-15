package com.newsfeeds.model.articlesearch

import com.google.gson.annotations.SerializedName

class Meta {

    @SerializedName("hits")
    var hits: Int?= 0

    @SerializedName("offset")
    var offset: Int?= 0

    @SerializedName("time")
    var time: Int?= 0

}