package com.dhkim.tvshows.network.response

import com.dhkim.tvshows.models.TVShowDetails
import com.google.gson.annotations.SerializedName

data class TVShowDetailsResponse (
    @SerializedName("tvShow") val tvShowDetails: TVShowDetails,
) {
    fun getTvShowDetail(): TVShowDetails {
        return tvShowDetails
    }
}