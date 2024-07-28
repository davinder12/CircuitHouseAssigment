package com.example.moviescreen.data.network

import androidx.lifecycle.LiveData
import io.reactivex.Maybe

/**
 * Interface for UI interaction with resources
 */
interface IResource<T> {
    /**
     * Refresh the resource
     */
    fun refresh()

    /**
     * The resource data
     */
    val data: LiveData<T>


    /**
     * The resource data
     */
    val dataList: LiveData<List<T>>


    //val pageData : LiveData<T>

    /**
     * The refresh status of the resource
     * Refresh also triggers networkState
     */
    val isRefreshing: LiveData<Boolean>

    /**
     * The network state of the resource,
     */
    val networkState: LiveData<NetworkState>

    fun retry(networkState: NetworkState)
}


/**
 * Interface for UI interaction with resources
 */
interface IRequest<T> {
    /**
     * The network state of the resource,
     */
    val networkState: LiveData<NetworkState>

    //val responseData: MutableLiveData<T>

    val request: Maybe<T>


    fun retry(networkState: NetworkState)
}

