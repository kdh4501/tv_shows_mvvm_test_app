package com.dhkim.tvshows.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.dhkim.tvshows.R
import com.dhkim.tvshows.adapters.TVShowsAdapter
import com.dhkim.tvshows.databinding.ActivityMainBinding
import com.dhkim.tvshows.listeners.TVShowListener
import com.dhkim.tvshows.models.TVShow
import com.dhkim.tvshows.viewmodels.MostPopularTVShowViewModel

class MainActivity : AppCompatActivity(), TVShowListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mostPopularTVShowViewModel: MostPopularTVShowViewModel
    private var tvShows: MutableList<TVShow> = mutableListOf()
    private lateinit var tvShowsAdapter: TVShowsAdapter
    private var currentPage: Int = 1
    private var totalAvailablePages: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        doInit()
    }

    fun doInit() {
        binding.tvShowsRecyclerView.setHasFixedSize(true)
        mostPopularTVShowViewModel = ViewModelProvider(this)[MostPopularTVShowViewModel::class.java]
        tvShowsAdapter = TVShowsAdapter(tvShows, this)
        binding.tvShowsRecyclerView.adapter = tvShowsAdapter
        binding.tvShowsRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.tvShowsRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1;
                        getMostPopularTVShows()
                    }
                }
            }
        })
        getMostPopularTVShows()
    }

    private fun getMostPopularTVShows() {
        toggleLoading()
        mostPopularTVShowViewModel.getMostPopularTVShow(currentPage).observe(this) {
                mostPopularTVShowsResponse ->
            toggleLoading()
            if (mostPopularTVShowsResponse != null) {
                totalAvailablePages = mostPopularTVShowsResponse.totalPages
                if (mostPopularTVShowsResponse.tvShow != null) {
                    var oldCount: Int = tvShows.size
                    tvShows.addAll(mostPopularTVShowsResponse.tvShow)
                    tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size)
                }
            }
        }
    }

    private fun toggleLoading() {
        if (currentPage == 1) {
            binding.isLoading = !(binding.isLoading != null && binding.isLoading as Boolean)
        } else {
            binding.isLoadingMore = !(binding.isLoadingMore != null && binding.isLoadingMore as Boolean)
        }
    }

    override fun onTVShowClicked(tvShow: TVShow) {
        var intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShow", tvShow)
        startActivity(intent)
    }
}