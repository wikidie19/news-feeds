package com.newsfeeds.base.adapter

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener {
    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 1
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var offset = 0
    private var limit = 0
    private var mLayoutManager: RecyclerView.LayoutManager
    private var isUseLinearLayoutManager = false
    private var isUseGridLayoutManager = false

    constructor(linearLayoutManager: LinearLayoutManager, offset: Int, limit: Int) {
        mLayoutManager = linearLayoutManager
        isUseLinearLayoutManager = true
        this.offset = offset
        this.limit = limit
    }

    constructor(gridLayoutManager: GridLayoutManager, offset: Int, limit: Int) {
        mLayoutManager = gridLayoutManager
        isUseGridLayoutManager = true
        this.offset = offset
        this.limit = limit
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (isUseLinearLayoutManager && mLayoutManager is LinearLayoutManager) {
            firstVisibleItem =
                (mLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        }
        if (isUseGridLayoutManager && mLayoutManager is GridLayoutManager) {
            firstVisibleItem = (mLayoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        }
        visibleItemCount = mLayoutManager.childCount
        totalItemCount = mLayoutManager.itemCount
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && (totalItemCount - visibleItemCount
                    <= firstVisibleItem + visibleThreshold) && totalItemCount >= limit
        ) {
            // End has been reached

            // Do something
            offset = totalItemCount - 2
            Log.e("COUNTING", "COUNT $totalItemCount")
            onLoadMore(offset)
            loading = true
        }
    }

    abstract fun onLoadMore(offset: Int)
}