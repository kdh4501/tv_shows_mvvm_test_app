package com.dhkim.tvshows.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dhkim.tvshows.R
import com.dhkim.tvshows.adapters.ImageSliderAdapter
import com.dhkim.tvshows.databinding.ActivityTvshowDetailsBinding
import com.dhkim.tvshows.viewmodels.TVShowDetailsViewModel

class TVShowDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTvshowDetailsBinding
    private lateinit var tvShowDetailsViewModel: TVShowDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details)

        doInit()
    }

    private fun doInit() {
        tvShowDetailsViewModel = ViewModelProvider(this)[TVShowDetailsViewModel::class.java]
        getTVShowDetails()
    }

    private fun getTVShowDetails() {
        binding.isLoading = true
        var tvShowId: String = intent.getIntExtra("id", -1).toString()
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(this) { tvShowDetailsPesponse ->
            binding.isLoading = false
            if (tvShowDetailsPesponse?.getTvShowDetail() != null) {
                if (tvShowDetailsPesponse.getTvShowDetail().pictures != null) {
                    loadImageSlider(tvShowDetailsPesponse.getTvShowDetail().pictures)
                }
            }
        }
    }

    private fun loadImageSlider(sliderImages: List<String>) {
        binding.sliderViewPager.offscreenPageLimit = 1
        binding.sliderViewPager.adapter = ImageSliderAdapter(sliderImages)
        binding.sliderViewPager.visibility = View.VISIBLE
        binding.viewFadingEdge.visibility = View.VISIBLE
    }
}