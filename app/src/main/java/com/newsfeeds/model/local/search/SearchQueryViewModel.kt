package com.newsfeeds.model.local.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.newsfeeds.extention.map

class SearchQueryViewModel(private val searchQueryRepository: SearchQueryRepository) : ViewModel() {

    private lateinit var searchQueryDataResult: LiveData<List<SearchQuery>>

    init {
        subscribeFeedsDataResult()
    }

    fun saveSearchQueryData(searchQuery: SearchQuery) {
        searchQueryRepository.saveSearchQueryData(searchQuery)
    }

    fun deleteAllSearchQueryData() {
        searchQueryRepository.deleteAllSearchQueryData()
    }

    fun deleteItem(queryValue: String?) {
        searchQueryRepository.deleteItem(queryValue)
    }

    fun listenSearchQueryDataResult(): LiveData<List<SearchQuery>> {
        return searchQueryDataResult
    }

    private fun subscribeFeedsDataResult() {
        searchQueryDataResult = searchQueryRepository.loadQuerySearchData().map { data ->
            data.reversed().map {
                SearchQuery(
                    it.queryValue,
                    it.dateCreate
                )
            }
        }
    }
}
