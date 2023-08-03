package com.dhkim.tvshows.activities

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.dhkim.tvshows.R
import com.dhkim.tvshows.adapters.EpisodesAdapter
import com.dhkim.tvshows.adapters.ImageSliderAdapter
import com.dhkim.tvshows.databinding.ActivityTvshowDetailsBinding
import com.dhkim.tvshows.databinding.LayoutEpisodesBottomSheetBinding
import com.dhkim.tvshows.models.TVShow
import com.dhkim.tvshows.viewmodels.TVShowDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Locale

class TVShowDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTvshowDetailsBinding
    private lateinit var tvShowDetailsViewModel: TVShowDetailsViewModel
    private lateinit var episodesBottomSheetDialog: BottomSheetDialog
    private lateinit var layoutEpisodesBottomSheetBinding: LayoutEpisodesBottomSheetBinding
    private lateinit var tvShow: TVShow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details)

        doInit()
    }

    private fun doInit() {
        tvShowDetailsViewModel = ViewModelProvider(this)[TVShowDetailsViewModel::class.java]
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }
        tvShow = intent.getSerializableExtra("tvShow") as TVShow
        getTVShowDetails()
    }

    private fun getTVShowDetails() {
        binding.isLoading = true
        var tvShowId: String = tvShow.id.toString()
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
                binding.buttonWebsite.setOnClickListener {
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(tvShowDetailsPesponse.getTvShowDetail().url)
                    startActivity(intent)
                }
                binding.buttonWebsite.visibility = View.VISIBLE
                binding.buttonEpisodes.visibility = View.VISIBLE
                binding.buttonEpisodes.setOnClickListener {
                    episodesBottomSheetDialog = BottomSheetDialog(this)
                    layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(this),
                        R.layout.layout_episodes_bottom_sheet,
                        findViewById(R.id.episodesContainer),
                        false
                    )
                    episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.root)
                    layoutEpisodesBottomSheetBinding.episodesRecyclerView.adapter =
                        EpisodesAdapter(tvShowDetailsPesponse.getTvShowDetail().episodes)
                    layoutEpisodesBottomSheetBinding.textTitle.text =
                        String.format("Episodes | %s", tvShow.name)
                    layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener {
                        episodesBottomSheetDialog.dismiss()
                    }
                    // Optional section start
                    val frameLayout: FrameLayout? = episodesBottomSheetDialog.findViewById(
                        com.google.android.material.R.id.design_bottom_sheet
                    )
                    if (frameLayout != null) {
                        val bottomSheetBehavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(frameLayout)
                        bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    episodesBottomSheetDialog.show()
                }
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
        binding.tvShowName = tvShow.name
        binding.networkCountry = tvShow.network + " (" +
                tvShow.country + ")"
        binding.status = tvShow.status
        binding.startedDate = tvShow.startDate
        binding.textName.visibility = View.VISIBLE
        binding.textNetworkCountry.visibility = View.VISIBLE
        binding.textStatus.visibility = View.VISIBLE
        binding.textStarted.visibility = View.VISIBLE
    }
}