package com.example.moviescreen.data.repository

import androidx.lifecycle.LiveData
import com.example.moviescreen.data.api.MovieApi
import com.example.moviescreen.data.db.*
import com.example.moviescreen.data.models.GenerList
import com.example.moviescreen.data.models.ResponseValidator
import com.example.moviescreen.data.models.MovieList
import com.example.moviescreen.data.network.*
import io.reactivex.Single
import retrofit2.Call
import javax.inject.Inject
import retrofit2.Response
import com.example.moviescreen.utilitiesclasses.IListResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val movieApi: MovieApi,
    private val localCache : LocalGenreCache,
    private val localMovieCache: LocalMovieCache
) {

    fun getMovieList() : IResource<LocalGenre>{
        return NetworkResource(appExecutors, object : IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<LocalGenre, GenerList> {
            override fun createNetworkRequest(): Single<Response<GenerList>> {
               return movieApi.getGenerList();
            }
            override fun getResponseStatus(response: Response<GenerList>): ResponseValidator {
               return ResponseValidator(response.code(),response.message())
            }
            override fun mapToLocal(response: GenerList): List<LocalGenre> {
                return response.genres.map { LocalGenre.Mapper.from(it) }
            }
            override fun onSuccessPostMap(it: List<LocalGenre>) {
                CoroutineScope(Dispatchers.IO).launch {
                    localCache.insert(it)
                }
            }
            override fun onLocalStorageData(): LiveData<List<LocalGenre>> {
              return localCache.getGenerList()
            }
            override fun onLocalStorageRecordExist(): Int {
               return localCache.getGenreCount()
            }
        })
    }

    fun getMovieList2(): IListResource<LocalMovie> {
        return PagedListNetworkCall(object : PaginationList<LocalMovie, MovieList>(appExecutors) {
            override fun mapToLocal(items: MovieList): List<LocalMovie> {
               return items.results.map { LocalMovie.Mapper.from(items.page,it) }
            }
            override fun getResponseStatus(items: MovieList): ResponseValidator {
                return ResponseValidator(200, "working")
            }
            override fun loadPage(page: Int): Call<MovieList> {
                return movieApi.getMovieList(page = page)
            }
            override fun loadAfterPage(page: Int): Call<MovieList> {
                return movieApi.getMovieList(page = page)
            }
            override fun sessionExpired() {
            }
            override fun localStorage(items: List<LocalMovie>) {
                CoroutineScope(Dispatchers.IO).launch {
                    localMovieCache.insert(items)
                }
            }
            override fun verifyLocalData(page: Int): List<LocalMovie> {
                return localMovieCache.getMovieList(page)
            }
        })
    }

}











