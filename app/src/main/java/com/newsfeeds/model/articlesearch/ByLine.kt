package com.newsfeeds.model.articlesearch

import com.google.gson.annotations.SerializedName

class ByLine {

    @SerializedName("original")
    var original: String? = ""

    @SerializedName("person")
    var person: MutableList<Person>? = mutableListOf()

}
