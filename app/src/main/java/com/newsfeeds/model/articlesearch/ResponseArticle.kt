package com.newsfeeds.model.articlesearch

import com.google.gson.annotations.SerializedName

class ResponseArticle {

    @SerializedName("docs")
    var docs: MutableList<DocsArticle>? = mutableListOf()

    @SerializedName("meta")
    var meta: Meta? = null

}