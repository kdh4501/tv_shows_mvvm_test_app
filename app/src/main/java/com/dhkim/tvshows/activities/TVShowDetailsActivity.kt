package com.dhkim.tvshows.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.dhkim.tvshows.R
import com.dhkim.tvshows.adapters.ImageSliderAdapter
import com.dhkim.tvshows.databinding.ActivityTvshowDetailsBinding
import com.dhkim.tvshows.viewmodels.TVShowDetailsViewModel
import java.util.Locale

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
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }
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
                binding.tvShowImageURL =
                    tvShowDetailsPesponse.getTvShowDetail().imagePath
                binding.imageTVShow.visibility = View.VISIBLE
                binding.description = HtmlCompat.fromHtml(
                    tvShowDetailsPesponse.getTvShowDetail().description,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                ).toString()
                binding.textDescription.visibility = View.VISIBLE
                binding.textReadMore.visibility = View.VISIBLE
                binding.textReadMore.setOnClickListener {
                    if (binding.textReadMore.text.toString() == "Read More") {
                        binding.textDescription.maxLines = Int.MAX_VALUE
                        binding.textDescription.ellipsize = null
                        binding.textReadMore.text = getString(R.string.read_less)
                    } else {
                        binding.textDescription.maxLines = 4
                        binding.textDescription.ellipsize = TextUtils.TruncateAt.END
                        binding.textReadMore.text = getString(R.string.read_more)
                    }
                }
                binding.rating = String.format(
                    Locale.getDefault(), "%.2f",
                    tvShowDetailsPesponse.getTvShowDetail().rating.toDouble()
                )
                if (tvShowDetailsPesponse.tvShowDetails.genres != null) {
                    binding.genre = tvShowDetailsPesponse.getTvShowDetail().genres[0]
                } else {
                    binding.genre = "N/A"
                }
                binding.runtime = tvShowDetailsPesponse.getTvShowDetail().runtime + " Min"
                binding.viewDivider1.visibility = View.VISIBLE
                binding.layoutMisc.visibility = View.VISIBLE
                binding.viewDivider2.visibility = View.VISIBLE
                loadBasicTVShowDetails()
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

    private fun loadBasicTVShowDetails() {
        binding.tvShowName = intent.getStringExtra("name")
        binding.networkCountry = intent.getStringExtra("network") + " (" +
                intent.getStringExtra("country") + ")"
        binding.status = intent.getStringExtra("status")
        binding.startedDate = intent.getStringExtra("startDate")
        binding.textName.visibility = View.VISIBLE
        binding.textNetworkCountry.visibility = View.VISIBLE
        binding.textStatus.visibility = View.VISIBLE
        binding.textStarted.visibility = View.VISIBLE
    }
}