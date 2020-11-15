package com.newsfeeds.extention

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <X, Y> LiveData<X>.map(func: (source: X) -> Y) = Transformations.map(this, func)