package com.dhkim.tvshows.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dhkim.tvshows.R
import com.dhkim.tvshows.databinding.ActivityWatchlistBinding
import com.dhkim.tvshows.viewmodels.WatchlistViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WatchlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatchlistBinding
    private lateinit var viewModel: WatchlistViewModel

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
                    Toast.makeText(applicationContext, "Watchlist: " + tvShows.size, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        loadWatchlist()
    }
}