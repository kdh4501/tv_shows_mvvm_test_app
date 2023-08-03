package com.dhkim.tvshows.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dhkim.tvshows.dao.TVShowDao
import com.dhkim.tvshows.models.TVShow

@Database(entities = [TVShow::class], version = 1, exportSchema = false)
abstract class TVShowDatabase: RoomDatabase() {
    abstract fun tvShowDao(): TVShowDao
    companion object {
        private var tvShowDatabase: TVShowDatabase? = null
        fun getTvShowsDatabase(context: Context): TVShowDatabase {
            if (tvShowDatabase == null) {
                tvShowDatabase = Room.databaseBuilder(
                    context,
                    TVShowDatabase::class.java,
                    "tv_shows_db"
                ).build()
            }
            return tvShowDatabase as TVShowDatabase
        }
    }
}