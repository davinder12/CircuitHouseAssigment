package com.example.moviescreen.data.network

import retrofit2.Call

abstract class PaginationList<LocalType, RemoteType>(appExecutors: AppExecutors) :
    NetworkListResourceBoundaryCallback<LocalType, RemoteType>(appExecutors) {
    /**
     * Load the specified page from the API.
     */
    abstract fun loadPage(page: Int): Call<RemoteType>

    /**
     * Load the specified page from the API.
     */
    abstract fun loadAfterPage(page: Int): Call<RemoteType>

    abstract fun verifyLocalData(page:Int): List<LocalType>

    override fun loadAfter(page: Int): Call<RemoteType> {
        return loadAfterPage(page)
    }

    override fun loadBefore(page: Int): Call<RemoteType> {
        return loadPage(page)
    }

    override fun checkLocalDataBase(size :Int ): List<LocalType> {
       return verifyLocalData(size)
    }




}