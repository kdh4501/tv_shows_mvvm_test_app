package com.dhkim.tvshows.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.dhkim.tvshows.R
import com.dhkim.tvshows.adapters.TVShowsAdapter
import com.dhkim.tvshows.databinding.ActivitySearchBinding
import com.dhkim.tvshows.listeners.TVShowListener
import com.dhkim.tvshows.models.TVShow
import com.dhkim.tvshows.viewmodels.SearchViewModel
import java.util.Timer
import java.util.TimerTask

class SearchActivity : AppCompatActivity(), TVShowListener {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private var tvShows: MutableList<TVShow> = mutableListOf()
    private lateinit var tvShowsAdapter: TVShowsAdapter
    private var currentPage: Int = 1;
    private var totalAvailablePages: Int = 1
    private var timer: Timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        doInit()
    }

    private fun doInit() {
        binding.imageBack.setOnClickListener { onBackPressed() }
        binding.tvShowsRecyclerView.setHasFixedSize(true)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        tvShowsAdapter = TVShowsAdapter(tvShows, this)
        binding.tvShowsRecyclerView.adapter = tvShowsAdapter
        binding.inputSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                timer.cancel()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim().isNotEmpty()) {
                    timer = Timer()
                    timer.schedule(object: TimerTask() {
                        override fun run() {
                            Handler(Looper.getMainLooper()).post {
                                currentPage = 1
                                totalAvailablePages = 1
                                searchTVShow(s.toString())
                            }
                        }
                    }, 800)
                } else {
                    tvShows.clear()
                    tvShowsAdapter.notifyDataSetChanged()
                }
            }
        })
        binding.tvShowsRecyclerView.addOnScrollListener(object: OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.tvShowsRecyclerView.canScrollVertically(1)) {
                    if (binding.inputSearch.text.toString().isNotEmpty()) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1
                            searchTVShow(binding.inputSearch.text.toString())
                        }
                    }
                }
            }
        })
        binding.inputSearch.requestFocus()
    }

    private fun searchTVShow(query: String) {
        toggleLoading()
        viewModel.searchTVShow(query, currentPage).observe(this) { tvShowsResponse ->
            toggleLoading()
            if (tvShowsResponse != null) {
                totalAvailablePages = tvShowsResponse.totalPages
                if (tvShowsResponse.tvShow != null) {
                    val oldCount = tvShows.size
                    tvShows.addAll(tvShowsResponse.tvShow)
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
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShow", tvShow)
        startActivity(intent)
    }
}