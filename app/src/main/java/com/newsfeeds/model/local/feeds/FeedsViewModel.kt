package com.newsfeeds.model.local.feeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.newsfeeds.extention.map

class FeedsViewModel(private val feedsRepository: FeedsRepository) : ViewModel() {

    private lateinit var feedsDataResult: LiveData<List<FeedsData>>

    init {
        subscribeFeedsDataResult()
    }

    fun saveFeedsData(feedsData: FeedsData) {
        feedsRepository.saveFeedsData(feedsData)
    }

    fun deleteAllFeedsData() {
        feedsRepository.deleteAllFeedsData()
    }

    fun deleteItem(headlineMain: String?) {
        feedsRepository.deleteItem(headlineMain)
    }

    fun listenFeedsDataResult(): LiveData<List<FeedsData>> {
        return feedsDataResult
    }

    private fun subscribeFeedsDataResult() {
        feedsDataResult = feedsRepository.loadFeedsData().map { data ->
            data.reversed().map {
                FeedsData(
                    it.headlineMain,
                    it.abstract,
                    it.snippet,
                    it.lead_paragraph,
                    it.source,
                    it.multimedia,
                    it.headline,
                    it.pubDate,
                    it.byline,
                    it.dateCreate
                )
            }
        }
    }
}
