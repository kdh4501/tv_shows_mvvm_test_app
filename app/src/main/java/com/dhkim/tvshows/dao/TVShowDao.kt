package com.dhkim.tvshows.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhkim.tvshows.models.TVShow
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface TVShowDao {

    @Query("SELECT * FROM tvShows")
    fun getWatchlist(): Flowable<List<TVShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToWatchlist(tvShow: TVShow): Completable

    @Delete
    fun removeFromWatchlist(tvShow: TVShow)

}