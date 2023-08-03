package com.dhkim.tvshows.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dhkim.tvshows.database.TVShowDatabase
import com.dhkim.tvshows.models.TVShow
import com.dhkim.tvshows.network.response.TVShowDetailsResponse
import com.dhkim.tvshows.repositories.TVShowDetailsRepository
import io.reactivex.Completable

class TVShowDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val mostPopularTVShowsRepository: TVShowDetailsRepository = TVShowDetailsRepository()
    private val tvShowDatabase: TVShowDatabase = TVShowDatabase.getTvShowsDatabase(application)

    fun getTVShowDetails(tvShowId: String): MutableLiveData<TVShowDetailsResponse?> {
        return mostPopularTVShowsRepository.getTVShowDetails(tvShowId)
    }

    fun addToWatchlist(tvShow: TVShow): Completable {
        return tvShowDatabase.tvShowDao().addToWatchlist(tvShow)
    }
}