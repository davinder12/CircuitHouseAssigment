package com.example.moviescreen.data.api

import com.example.moviescreen.data.models.GenerList
import com.example.moviescreen.data.models.MovieList
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApi {

    companion object {
        const val MODULE_PATH = "3/"
        const val URL = ""
    }

    @GET("genre/movie/list?language=en")
    fun getGenerList(): Single<Response<GenerList>>

    @GET("discover/movie")
    fun getMovieList(
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Call<MovieList>


}