package com.example.moviescreen.dependencyinjection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import com.example.moviescreen.BuildConfig
import com.example.moviescreen.data.api.MovieApi


@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        @Named("api_url") baseUrl: String, httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return buildApiClient(retrofit, MovieApi.MODULE_PATH).create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
       // authenticationInterceptor: AuthenticationInterceptor,
    ): OkHttpClient {
        val httpClient = OkHttpClient.Builder().apply {
            readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
        }
        httpClient.addNetworkInterceptor(loggingInterceptor)
       // httpClient.addNetworkInterceptor(authenticationInterceptor)
        httpClient.addInterceptor { chain ->
            val ongoing = chain.request().newBuilder()
            ongoing.addHeader("Authorization", BuildConfig.TOKEN)
            ongoing.addHeader("Content-Type", "application/json;charset=utf-8")
            ongoing.addHeader("Accept", "application/json; application/problem+json")
            val response = chain.proceed(ongoing.build())
            response
        }
        return httpClient.build()
    }


    @Singleton
    @Provides
    @Named("api_url")
    fun provideApiUrl(): String {
        return BuildConfig.API_URL
    }

    private fun buildApiClient(retrofit: Retrofit, path: String): Retrofit {
        return retrofit.newBuilder().baseUrl(retrofit.baseUrl().toUrl().toString() + path)
            .validateEagerly(true).build()
    }

    companion object {
        private const val HTTP_TIMEOUT = 40L
    }


    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}
