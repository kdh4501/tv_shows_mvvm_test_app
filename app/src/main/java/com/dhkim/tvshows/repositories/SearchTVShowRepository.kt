package com.dhkim.tvshows.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhkim.tvshows.network.ApiClient
import com.dhkim.tvshows.network.ApiService
import com.dhkim.tvshows.network.response.TVShowsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class SearchTVShowRepository {

    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    fun serachTVShow(query: String, page: Int): LiveData<TVShowsResponse> {
        var data = MutableLiveData<TVShowsResponse>()
        apiService.searchTVShow(query, page).enqueue(object : Callback<TVShowsResponse>{
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
    }

}