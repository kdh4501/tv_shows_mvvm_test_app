package com.dhkim.tvshows.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
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
        setupSliderIndicators(sliderImages.size)
        binding.sliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentSliderIndicator(position)
            }
            })
    }

    private fun setupSliderIndicators(count: Int) {
        var indicator = Array<ImageView?>(5) {null}
        var layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8,0,8,0)
        for (i in indicator.indices) {
            indicator[i] = ImageView(applicationContext)
            indicator[i]?.setImageDrawable(ContextCompat.getDrawable(
                applicationContext, R.drawable.background_slider_indicator_inactive
            ))
            indicator[i]?.layoutParams = layoutParams
            binding.layoutSliderIndicators.addView(indicator[i])
        }
        binding.layoutSliderIndicators.visibility = View.VISIBLE
        setCurrentSliderIndicator(0)
    }

    private fun setCurrentSliderIndicator(position: Int) {
        var childCount : Int = binding.layoutSliderIndicators.childCount
        for (i in 0 until  childCount) {
            var imageView : ImageView = binding.layoutSliderIndicators.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.background_slider_indicator_active)
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.background_slider_indicator_inactive)
                )
            }
        }
    }
}