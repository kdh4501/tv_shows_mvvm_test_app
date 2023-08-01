package com.dhkim.tvshows.repositories

import androidx.lifecycle.MutableLiveData
import com.dhkim.tvshows.network.ApiClient
import com.dhkim.tvshows.network.ApiService
import com.dhkim.tvshows.network.response.TVShowDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TVShowDetailsRepository {
    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    fun getTVShowDetails(tvShowId: String): MutableLiveData<TVShowDetailsResponse?> {
        var data = MutableLiveData<TVShowDetailsResponse?>()
        try {
            apiService.getTVShowDetails(tvShowId).enqueue(object: Callback<TVShowDetailsResponse> {
                override fun onResponse(
                    call: Call<TVShowDetailsResponse>,
                    response: Response<TVShowDetailsResponse>
                ) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<TVShowDetailsResponse>, t: Throwable) {
                    data.value = null
                }
            })
            return data
        } catch (e: Exception) {

        }
        return data
    }
}