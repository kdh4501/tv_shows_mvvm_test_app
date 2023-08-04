package com.dhkim.tvshows.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dhkim.tvshows.R
import com.dhkim.tvshows.databinding.ItemContainerTvShowBinding
import com.dhkim.tvshows.listeners.WatchlistListener
import com.dhkim.tvshows.models.TVShow

class WatchlistAdapter(
    var tvShows : List<TVShow>,
    var watchlistListener: WatchlistListener
): RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        println("1111111  ${tvShows.size}")
        val binding: ItemContainerTvShowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_container_tv_show,
            parent,
            false
        )

        return TVShowViewHolder(binding)
    }

    override fun getItemCount(): Int {
        println("22222  ${tvShows}")
        return tvShows.size
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        println("3333333  ${tvShows}")
        holder.bindTVShow(tvShows[position], watchlistListener)
    }

    class TVShowViewHolder(private val itemContainerTvShowBinding: ItemContainerTvShowBinding)
        : RecyclerView.ViewHolder(itemContainerTvShowBinding.root) {

        fun bindTVShow(tvShow: TVShow, watchlistListener: WatchlistListener) {
            itemContainerTvShowBinding.tvShow = tvShow
            itemContainerTvShowBinding.executePendingBindings()
            itemContainerTvShowBinding.root.setOnClickListener {
                watchlistListener.onTVShowClicked(tvShow)
            }
            itemContainerTvShowBinding.imageDelete.setOnClickListener {
                watchlistListener.removeTVShowFromWatchlist(tvShow, adapterPosition)
            }
            itemContainerTvShowBinding.imageDelete.visibility = View.VISIBLE
        }
    }
}