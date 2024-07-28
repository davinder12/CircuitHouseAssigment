package com.example.moviescreen.data.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toLiveData
import androidx.room.Entity
import androidx.room.Index
import com.example.moviescreen.data.extension.liveData
import com.example.moviescreen.data.extension.mediatorLiveData
import com.example.moviescreen.data.extension.postUpdate

import io.reactivex.Flowable
import io.reactivex.Maybe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit


/**
 * Represents a basic network resource that provides a mechanism for retry, refresh and network status.
 * */
open class NetworkResource<LocalType, RemoteType, ResponseType>
@MainThread constructor(
    private val appExecutors: AppExecutors,
    private val cb: INetworkResourceCallback<LocalType, RemoteType, ResponseType>,
    init: Boolean = true
) : IResource<LocalType> {

    private val networkRequest = NetworkRequest(appExecutors, cb)

    final override val dataList = MediatorLiveData<List<LocalType>>()

    final override val networkState = networkRequest.networkState


    private val _isRefreshing: MutableLiveData<Boolean> =
        mediatorLiveData(networkState) {
            it?.let { if (it.status != NetworkState.Status.RUNNING) postValue(false) }
        }

    override val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    init {
        checkIfGenresExist {
           if(it){
              val request = cb.onLocalStorageData()
               dataList.addSource(request) {
                   dataList.value = it
                   dataList.removeSource(request)
               }
           } else if (init) {
                val request = prepareNetworkRequest()
                dataList.addSource(request) {
                    dataList.value = it
                    dataList.removeSource(request)
                }
            }
        }
    }


    override fun refresh() {
        _isRefreshing.postUpdate(true)
        val request = prepareNetworkRequest()
        // TODO: refactor executor/threading layer
        appExecutors.mainThread().execute {
            dataList.addSource(request) {
                dataList.removeSource(request)
                dataList.value = it
            }
        }
    }

    override fun retry(networkState: NetworkState) {
        val request = prepareNetworkRequest()
        dataList.addSource(request) {
            dataList.removeSource(request)
            dataList.value = it
        }
    }

    private fun checkIfGenresExist(callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = cb.onLocalStorageRecordExist()
            withContext(Dispatchers.Main) { callback(count > 0) }
        }
    }

    private fun prepareNetworkRequest(): LiveData<List<LocalType>> {
        var request  = createNetworkRequest()
        return toLiveData(toFlowable(request))
    }



    protected open fun checkLocalStroageMethod(): Maybe<List<LocalType>> {
        // TODO
        // return a new maybe, and then on onSubscribe we either attach the request,
        // or return immediately depending on network limiter
        return networkRequest.request
            .map { cb.extractData(it) }
            .doOnSuccess { cb.onSuccessPreMap(it) }
            .map { cb.mapToLocal(it) }
            .doOnSuccess { cb.onSuccessPostMap(it) }
    }

    protected open fun createNetworkRequest(): Maybe<List<LocalType>> {
        // TODO
        // return a new maybe, and then on onSubscribe we either attach the request,
        // or return immediately depending on network limiter
        return networkRequest.request
            .map { cb.extractData(it) }
            .doOnSuccess { cb.onSuccessPreMap(it) }
            .map { cb.mapToLocal(it) }
            .doOnSuccess { cb.onSuccessPostMap(it) }
    }

    /**
     * Convert the [Maybe] to [Flowable].
     * May be used to add additional operations to the chain.
     */
    protected open fun toFlowable(request: Maybe<List<LocalType>>): Flowable<List<LocalType>> {
        return request.toFlowable()
    }

    protected open fun toLiveData(flowable: Flowable<List<LocalType>>): LiveData<List<LocalType>> {
        return flowable.toLiveData()
    }

    override val data: LiveData<LocalType>
        get() = TODO("Not yet implemented")


}

/**
 * Callback for [NetworkResource]
 */
interface INetworkResourceCallback<LocalType, RemoteType, ResponseType> :
    INetworkRequestCallback<ResponseType> {

//    var networkLimiter: NetworkLimiter?
//    var networkId: Int


    /**
     * Called on success before mapping
     */

    fun onLocalStorageRecordExist(): Int

    /**
     * Called on success before mapping
     */
    @MainThread
    fun onLocalStorageData(): LiveData<List<LocalType>>
    /**
     * Called on success before mapping
     */
    fun onSuccessPreMap(it: RemoteType) {}

    /**
     * Called on success after mapping
     */
    fun onSuccessPostMap(it: List<LocalType>) {}

    /**
     * Map the [RemoteType] to [LocalType]
     */
    @WorkerThread
    fun mapToLocal(response: RemoteType): List<LocalType>

    /**
     * Extract and return the response data.
     * @return The response data
     */
    fun extractData(response: ResponseType): RemoteType


  }
