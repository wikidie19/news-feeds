package com.newsfeeds.model.articlesearch

import com.google.gson.annotations.SerializedName

class DocsArticle {

    @SerializedName("abstract")
    var abstract: String? = ""

    @SerializedName("web_url")
    var webUrl: String? = ""

    @SerializedName("snippet")
    var snippet: String? = ""

    @SerializedName("lead_paragraph")
    var  leadParagraph: String? = ""

    @SerializedName("source")
    var source: String? = ""

    @SerializedName("multimedia")
    var multimedia: MutableList<Multimedia>? = mutableListOf()

    @SerializedName("headline")
    var headline: Headline? =  null

    @SerializedName("pub_date")
    var pubDate: String? = ""

    @SerializedName("byline")
    var byline: ByLine? = null

    constructor(
        abstract: String?,
        webUrl: String?,
        snippet: String?,
        leadParagraph: String?,
        source: String?,
        multimedia: MutableList<Multimedia>?,
        headline: Headline?,
        pubDate: String?,
        byline: ByLine?
    ) {
        this.abstract = abstract
        this.webUrl = webUrl
        this.snippet = snippet
        this.leadParagraph = leadParagraph
        this.source = source
        this.multimedia = multimedia
        this.headline = headline
        this.pubDate = pubDate
        this.byline = byline
    }
}