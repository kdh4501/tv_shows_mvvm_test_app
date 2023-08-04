package com.dhkim.tvshows.listeners

import com.dhkim.tvshows.models.TVShow

interface WatchlistListener {

    fun onTVShowClicked(tvShow: TVShow)

    fun removeTVShowFromWatchlist(tvShow: TVShow, position: Int)
}