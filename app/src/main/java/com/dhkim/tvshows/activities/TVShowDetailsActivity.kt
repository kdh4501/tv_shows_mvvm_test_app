package com.dhkim.tvshows.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dhkim.tvshows.R
import com.dhkim.tvshows.databinding.ActivityMainBinding
import com.dhkim.tvshows.databinding.ActivityTvshowDetailsBinding
import com.dhkim.tvshows.viewmodels.MostPopularTVShowViewModel
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
            Toast.makeText(this, tvShowDetailsPesponse?.getTvShowDetail()?.url, Toast.LENGTH_SHORT).show()
        }
    }
}