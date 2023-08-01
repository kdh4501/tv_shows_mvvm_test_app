package com.dhkim.tvshows.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dhkim.tvshows.R
import com.dhkim.tvshows.databinding.ActivityMainBinding
import com.dhkim.tvshows.viewmodels.MostPopularTVShowViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mostPopularTVShowViewModel: MostPopularTVShowViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mostPopularTVShowViewModel = ViewModelProvider(this)[MostPopularTVShowViewModel::class.java]
        getMostPopularTVShows()
    }

    fun getMostPopularTVShows() {
        mostPopularTVShowViewModel.getMostPopularTVShow(0).observe(this) {
                mostPopularTVShowsResponse ->
            Toast.makeText(this, "Total Pages: ${mostPopularTVShowsResponse?.totalPages}", Toast.LENGTH_SHORT).show()
        }
    }
}