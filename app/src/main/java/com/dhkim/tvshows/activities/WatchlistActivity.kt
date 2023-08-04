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
import com.dhkim.tvshows.utils.TempDataHolder
import com.dhkim.tvshows.viewmodels.WatchlistViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
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
        loadWatchlist()
    }

    private fun loadWatchlist() {
        binding.isLoading = true
        var compositeDisposable = CompositeDisposable()
        compositeDisposable.add(viewModel.loadWatchlist()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tvShows ->
                binding.isLoading = false
                if (watchlist.isNotEmpty()) {
                    watchlist.clear()
                }
                watchlist.addAll(tvShows)
                watchlistAdapter = WatchlistAdapter(tvShows, this)
                binding.watchlistRecyclerView.adapter = watchlistAdapter
                binding.watchlistRecyclerView.visibility = View.VISIBLE
                compositeDisposable.dispose()
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if (TempDataHolder.IS_WATCHLIST_UPDATED) {
            loadWatchlist()
            TempDataHolder.IS_WATCHLIST_UPDATED = false
        }
    }

    override fun onTVShowClicked(tvShow: TVShow) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShow", tvShow)
        startActivity(intent)
    }

    override fun removeTVShowFromWatchlist(tvShow: TVShow, position: Int) {
        if (watchlist.isEmpty() || position < 0 || position >= watchlist.size) {
            // 빈 리스트이거나 올바르지 않은 position일 경우 처리
            return
        }

        var compositeDisposableForDelete = CompositeDisposable()
        compositeDisposableForDelete.add(viewModel.removeTVShowFromWatchlist(tvShow)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                watchlist.removeAt(position)
                watchlistAdapter.notifyItemRemoved(position)
                watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.itemCount)
                loadWatchlist()     // remove해도 adapter의 아이템이 갱신 안되어서 호출했음.. 좀 더 연구 필요
                compositeDisposableForDelete.dispose()
            })
    }
}