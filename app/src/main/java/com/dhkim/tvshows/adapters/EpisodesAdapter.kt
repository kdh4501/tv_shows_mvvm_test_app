package com.dhkim.tvshows.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dhkim.tvshows.R
import com.dhkim.tvshows.databinding.ItemContainerEpisodeBinding
import com.dhkim.tvshows.databinding.ItemContainerTvShowBinding
import com.dhkim.tvshows.models.Episode

class EpisodesAdapter(private val episodes: List<Episode>): RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding: ItemContainerEpisodeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_container_episode,
            parent,
            false
        )
        return EpisodeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return episodes.size
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bindEpisode(episodes[position])
    }

    class EpisodeViewHolder(private val itemContainerEpisodeBinding: ItemContainerEpisodeBinding)
        : RecyclerView.ViewHolder(itemContainerEpisodeBinding.root) {

        fun bindEpisode(episode: Episode) {
            var title = "S"
            var season: String = episode.season
            if (season.length == 1) {
                season = "0$season"
            }
            var episodeNumber: String = episode.episode
            if (episodeNumber.length == 1) {
                episodeNumber = "0$episodeNumber"
            }
            episodeNumber = "E$episodeNumber"
            title = title + season + episodeNumber
            itemContainerEpisodeBinding.title = title
            itemContainerEpisodeBinding.name = episode.name
            itemContainerEpisodeBinding.airDate = episode.airDate
        }
    }

}