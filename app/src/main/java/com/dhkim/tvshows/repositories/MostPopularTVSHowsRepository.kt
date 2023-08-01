package com.dhkim.tvshows.repositories

import androidx.lifecycle.MutableLiveData
import com.dhkim.tvshows.network.ApiClient
import com.dhkim.tvshows.network.ApiService
import com.dhkim.tvshows.network.response.TVShowsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MostPopularTVSHowsRepository {
    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    fun getMostPopularTVShows(page: Int): MutableLiveData<TVShowsResponse?> {
        var data = MutableLiveData<TVShowsResponse?>()
        try {
            apiService.getMostPopularTVShows(page).enqueue(object: Callback<TVShowsResponse> {
                override fun onResponse(
                    call: Call<TVShowsResponse>,
                    response: Response<TVShowsResponse>
                ) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<TVShowsResponse>, t: Throwable) {
                    data.value = null
                }
            })
            return data
        } catch (e: Exception) {

        }
        return data
    }
}