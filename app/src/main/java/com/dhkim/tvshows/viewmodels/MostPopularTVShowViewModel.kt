package com.dhkim.tvshows.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhkim.tvshows.network.response.TVShowsResponse
import com.dhkim.tvshows.repositories.MostPopularTVSHowsRepository

class MostPopularTVShowViewModel: ViewModel() {
    private val mostPopularTVShowsRepository: MostPopularTVSHowsRepository = MostPopularTVSHowsRepository()

        fun getMostPopularTVShow(page: Int): MutableLiveData<TVShowsResponse?> {
            return mostPopularTVShowsRepository.getMostPopularTVShows(page)
        }
}