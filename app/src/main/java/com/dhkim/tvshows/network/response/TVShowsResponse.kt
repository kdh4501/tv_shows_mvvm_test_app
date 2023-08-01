package com.dhkim.tvshows.network.response

import com.dhkim.tvshows.models.TVShow
import com.google.gson.annotations.SerializedName

data class TVShowsResponse(
    @SerializedName("page")val page: Int,
    @SerializedName("pages")val totalPages: Int,
    @SerializedName("tv_shows")val tvShow: List<TVShow>
)