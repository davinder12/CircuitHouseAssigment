package com.example.moviescreen.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("FunctionMaxLength", "TooManyFunctions")
@InstallIn(SingletonComponent::class)
@Module
class DbModule {
    @Provides
    @Singleton
    fun provideAppCache(context: Context): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, "MovieAssignment.db")
            .fallbackToDestructiveMigrationOnDowngrade()
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun localGenerCache(appDb: AppDb) = appDb.localGenreCache()

    @Provides
    @Singleton
    fun localMovieCache(appDb: AppDb) = appDb.localMovieCache()

}
