package com.dhkim.tvshows.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dhkim.tvshows.database.TVShowDatabase
import com.dhkim.tvshows.models.TVShow
import io.reactivex.Flowable

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {
    private val tvShowDatabase: TVShowDatabase = TVShowDatabase.getTvShowsDatabase(application)

    fun loadWatchlist(): Flowable<List<TVShow>> {
        return tvShowDatabase.tvShowDao().getWatchlist()
    }
}