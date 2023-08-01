package com.dhkim.tvshows.listeners

import com.dhkim.tvshows.models.TVShow

interface TVShowListener {
    fun onTVShowClicked(tvShow: TVShow)
}