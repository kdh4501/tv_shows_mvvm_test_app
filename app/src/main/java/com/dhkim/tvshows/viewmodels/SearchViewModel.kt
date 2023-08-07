package com.dhkim.tvshows.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhkim.tvshows.network.response.TVShowsResponse
import com.dhkim.tvshows.repositories.SearchTVShowRepository

class SearchViewModel : ViewModel() {

    private val searchTVShowRepository = SearchTVShowRepository()

    fun searchTVShow(query: String, page: Int): LiveData<TVShowsResponse> {
        return searchTVShowRepository.serachTVShow(query, page)
    }
}