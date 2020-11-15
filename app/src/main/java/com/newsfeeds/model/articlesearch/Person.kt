package com.newsfeeds.model.articlesearch

import com.google.gson.annotations.SerializedName

class Person {

    @SerializedName("firstname")
    var firstName: String? = ""

    @SerializedName("middlename")
    var middleName: String? = ""

    @SerializedName("lastname")
    var lastName: String? = ""

    @SerializedName("qualifier")
    var qualifier: String? = ""

    @SerializedName("title")
    var title: String? = ""

    @SerializedName("role")
    var role: String? = ""

    @SerializedName("organization")
    var organization: String? = ""

}