package com.dhkim.tvshows.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dhkim.tvshows.R
import com.dhkim.tvshows.adapters.WatchlistAdapter
import com.dhkim.tvshows.databinding.ActivityWatchlistBinding
import com.dhkim.tvshows.listeners.WatchlistListener
import com.dhkim.tvshows.models.TVShow
import com.dhkim.tvshows.viewmodels.WatchlistViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WatchlistActivity : AppCompatActivity(), WatchlistListener {

    private lateinit var binding: ActivityWatchlistBinding
    private lateinit var viewModel: WatchlistViewModel
    private lateinit var watchlistAdapter: WatchlistAdapter
    private var watchlist: MutableList<TVShow> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_watchlist)
        doInit()
    }

    private fun doInit() {
        viewModel = ViewModelProvider(this)[WatchlistViewModel::class.java]
        binding.imageBack.setOnClickListener{onBackPressed()}
    }

    private fun loadWatchlist() {
        binding.isLoading = true
        var compositeDisposable = CompositeDisposable()
        compositeDisposable.add(viewModel.loadWatchlist().subscribeOn(Schedulers.computation())
            .subscribe { tvShows ->
                runOnUiThread {
                    binding.isLoading = false
                    if (watchlist.size > 0) {
                        watchlist.clear()
                    }
                    watchlist.addAll(tvShows)
                    println("000000  ${tvShows}")
                    watchlistAdapter = WatchlistAdapter(tvShows, this)
                    binding.watchlistRecyclerView.adapter = watchlistAdapter
                    binding.watchlistRecyclerView.visibility = View.VISIBLE
                    compositeDisposable.dispose()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        loadWatchlist()
    }

    override fun onTVShowClicked(tvShow: TVShow) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShow", tvShow)
        startActivity(intent)
    }

    override fun removeTVShowFromWatchlist(tvShow: TVShow, position: Int) {
    }
}