package com.dhkim.tvshows.network

import com.dhkim.tvshows.network.response.TVShowDetailsResponse
import com.dhkim.tvshows.network.response.TVShowsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("most-popular")
    fun getMostPopularTVShows(
        @Query(value = "page", encoded = true)
        page: Int, /* required */
    ): Call<TVShowsResponse>

    @GET("show-details")
    fun getTVShowDetails(
        @Query(value = "q", encoded = true)
        tvShowId: String,
    ): Call<TVShowDetailsResponse>

    @GET("search")
    fun searchTVShow(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Call<TVShowsResponse>

}