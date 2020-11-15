package com.newsfeeds.model.local.feeds

import androidx.lifecycle.LiveData

class FeedsRepository(private val feedsDao: FeedsDao) {

    fun saveFeedsData(feedsData: FeedsData) {
        feedsDao.insertFeedsData(feedsData)
    }

    fun deleteAllFeedsData(){
        feedsDao.deleteAllFeedsData()
    }

    fun deleteItem(headlineMain: String?) {
        feedsDao.deleteItem(headlineMain)
    }

    fun loadFeedsData(): LiveData<List<FeedsData>> {
        return feedsDao.getFeedsData()
    }

}
