package com.newsfeeds.model

import com.google.gson.annotations.SerializedName

class ErrorResponse {

    @SerializedName("fault")
    var fault: Fault? = null

    inner class Fault {
        @SerializedName("faultstring")
        var message: String? = ""
    }

}