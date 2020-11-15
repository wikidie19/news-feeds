package com.newsfeeds.model.articlesearch

import com.google.gson.annotations.SerializedName

class Headline {

    @SerializedName("main")
    var main: String? = ""

    @SerializedName("print_headline")
    var printHeadline: String? = ""

}