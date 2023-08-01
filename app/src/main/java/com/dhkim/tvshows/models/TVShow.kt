package com.dhkim.tvshows.models

import com.google.gson.annotations.SerializedName

data class TVShow constructor(
    @SerializedName("id")private val id: Int,
    @SerializedName("name")private val name: String,
    @SerializedName("start_date")private val startDate: String,
    @SerializedName("country")private val country: String,
    @SerializedName("network")private val network: String,
    @SerializedName("status")private val status: String,
    @SerializedName("image_thumbnail_path")private val thumbnail: String
) {

}