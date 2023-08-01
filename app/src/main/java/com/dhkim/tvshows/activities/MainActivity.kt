package com.dhkim.tvshows.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dhkim.tvshows.R
import com.dhkim.tvshows.adapters.TVShowsAdapter
import com.dhkim.tvshows.databinding.ActivityMainBinding
import com.dhkim.tvshows.models.TVShow
import com.dhkim.tvshows.viewmodels.MostPopularTVShowViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mostPopularTVShowViewModel: MostPopularTVShowViewModel
    private var tvShows: MutableList<TVShow> = mutableListOf()
    private lateinit var tvShowsAdapter: TVShowsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        doInit()
    }

    fun doInit() {
        binding.tvShowsRecyclerView.setHasFixedSize(true)
        mostPopularTVShowViewModel = ViewModelProvider(this)[MostPopularTVShowViewModel::class.java]
        tvShowsAdapter = TVShowsAdapter(tvShows)
        binding.tvShowsRecyclerView.adapter = tvShowsAdapter
        getMostPopularTVShows()
    }

    fun getMostPopularTVShows() {
        binding.isLoading = true
        mostPopularTVShowViewModel.getMostPopularTVShow(0).observe(this) {
                mostPopularTVShowsResponse ->
            binding.isLoading = false
            if (mostPopularTVShowsResponse != null) {
                if (mostPopularTVShowsResponse.tvShow != null) {
                    tvShows.addAll(mostPopularTVShowsResponse.tvShow)
                    tvShowsAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}