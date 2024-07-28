package com.example.moviescreen.dependencyinjection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.moviescreen.data.db.DbModule
import com.example.moviescreen.data.service.PreferenceService
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        DbModule::class,
        ApiModule::class
    ]
)
class AppModule {

    @Provides
    @Singleton
    fun providePreferenceService(@ApplicationContext context: Context): PreferenceService {
        return PreferenceService(context)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}

