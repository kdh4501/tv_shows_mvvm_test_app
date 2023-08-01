package com.dhkim.tvshows.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dhkim.tvshows.R
import com.dhkim.tvshows.databinding.ItemContainerSliderImageBinding
import com.dhkim.tvshows.databinding.ItemContainerTvShowBinding

class ImageSliderAdapter(var sliderImages: List<String> ): RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val binding: ItemContainerSliderImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_container_slider_image,
            parent,
            false
        )
        return ImageSliderAdapter.ImageSliderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return sliderImages.size
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bindSliderImage(sliderImages[position])
    }

    class ImageSliderViewHolder(private val itemContainerSliderImageBinding: ItemContainerSliderImageBinding)
        : RecyclerView.ViewHolder(itemContainerSliderImageBinding.root) {

        fun bindSliderImage(imageUrl: String) {
            itemContainerSliderImageBinding.imageURL = imageUrl
        }
    }
}