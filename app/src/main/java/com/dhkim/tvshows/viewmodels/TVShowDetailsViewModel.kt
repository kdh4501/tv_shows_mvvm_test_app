package com.dhkim.tvshows.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhkim.tvshows.network.response.TVShowDetailsResponse
import com.dhkim.tvshows.repositories.TVShowDetailsRepository

class TVShowDetailsViewModel: ViewModel() {
    private val mostPopularTVShowsRepository: TVShowDetailsRepository = TVShowDetailsRepository()

    fun getTVShowDetails(tvShowId: String): MutableLiveData<TVShowDetailsResponse?> {
        return mostPopularTVShowsRepository.getTVShowDetails(tvShowId)
    }
}