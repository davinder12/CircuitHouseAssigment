package com.example.moviescreen.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * API cache database
 */
const val SCHEMA_VERSION = 4

@TypeConverters(
    ZonedDateTimeConverter::class,
    InstantConverter::class
)

@Database(
    entities = [LocalGenre::class,LocalMovie::class],
    version = SCHEMA_VERSION
)
@Suppress("TooManyFunctions", "UnnecessaryAbstractClass")
abstract class AppDb : RoomDatabase() {
    abstract fun localGenreCache(): LocalGenreCache
    abstract fun localMovieCache(): LocalMovieCache
}
