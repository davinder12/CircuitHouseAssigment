package com.example.moviescreen.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.moviescreen.R
import com.example.moviescreen.utilitiesclasses.IListResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


abstract class PageListNetworkResource<LocalType> : PageKeyedDataSource<Int, LocalType>() {
    abstract val networkState: LiveData<NetworkState>
    abstract val isRefreshing: LiveData<Boolean>
    abstract val networkStateBefore: LiveData<NetworkState>
    abstract val networkStateAfter: LiveData<NetworkState>
    abstract fun refresh(showIndicator: Boolean = false)
    abstract fun retry()
}

abstract class PagedListNetworkResource<LocalType, Key>(
    private val dataSourceFactory: DataSource.Factory<Key, LocalType>,
    private val pagingCallback: PageListNetworkResource<LocalType>,
    config: PagedList.Config = DEFAULT_CONFIG
) :
    IListResource<LocalType> {
    override val networkStateBefore: LiveData<NetworkState> = pagingCallback.networkStateBefore
    override val networkStateAfter: LiveData<NetworkState> = pagingCallback.networkStateAfter
    override fun refresh() {
        pagingCallback.invalidate()
    }

    //final override val data: LiveData<PagedList<LocalType>>
   final override val data: LiveData<PagedList<LocalType>>

    override val dataList: LiveData<List<PagedList<LocalType>>>
        get() = TODO("Not yet implemented")

    override val isRefreshing: LiveData<Boolean>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val networkState: LiveData<NetworkState> = pagingCallback.networkState

    override fun retry(networkState: NetworkState) = pagingCallback.retry()

    init {
        val builder = LivePagedListBuilder(dataSourceFactory, config)
        data = builder.build()
    }

    companion object {
        val DEFAULT_CONFIG: PagedList.Config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(1)
            .setPageSize(1)
            .build()
    }
}


abstract class NetworkListResourceBoundaryCallback<LocalType, RemoteType>(private val appExecutors: AppExecutors) :
    PageListNetworkResource<LocalType>() {
    override val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    override val networkStateBefore: MutableLiveData<NetworkState> = MutableLiveData()
    override val networkStateAfter: MutableLiveData<NetworkState> = MutableLiveData()
    private var retry: (() -> Any)? = null
    override fun refresh(showIndicator: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retry() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            appExecutors.networkIO().execute {
                it.invoke()
            }
        }
    }

    protected abstract fun loadAfter(page: Int): Call<RemoteType>
    protected abstract fun loadBefore(page: Int): Call<RemoteType>
    protected abstract fun mapToLocal(items: RemoteType): List<LocalType>
    protected abstract fun localStorage(items: List<LocalType>)
    protected abstract fun checkLocalDataBase(size:Int): List<LocalType>
   // protected abstract fun getResponseStatus(items: RemoteType): ResponseValidator
  //  protected abstract fun sessionExpired()


    private fun checkIfGenresExist(size:Int,callback: (List<LocalType>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = checkLocalDataBase(size)
            callback(list)
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, LocalType>
    ) {
       checkIfGenresExist(params.requestedLoadSize) {
           if (it.isEmpty()) {
               networkState.postValue(NetworkState.loading)
               networkStateBefore.postValue(NetworkState.loading)
               val request = loadBefore(params.requestedLoadSize)
               try {
                   val response = request.execute()
                   retry = null
                   if (response.isSuccessful) {
                       networkState.postValue(NetworkState.success)
                       networkStateBefore.postValue(NetworkState.success)

                       response.body()?.let {
                           val localData = mapToLocal(it)
                           callback.onResult(localData, null, params.requestedLoadSize + 1)
                           localStorage(localData)
                       }
                   } else {
                       retry = { loadInitial(params, callback) }
                       networkState.postValue(NetworkState.error(R.string.network_error_unknown))
                       networkStateBefore.postValue(NetworkState.error(R.string.network_error_unknown))
                   }
               } catch (ioException: IOException) {
                   retry = { loadInitial(params, callback) }
                   networkState.postValue(
                       NetworkState.error(
                           ioException.message,
                           R.string.network_error_unknown
                       )
                   )
                   networkStateBefore.postValue(NetworkState.error(ioException.localizedMessage))
               }
           }else{
               callback.onResult(it, null, 1)
           }
       }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, LocalType>) {
        checkIfGenresExist(params.key) {
        if(it.isEmpty()) {
            networkState.postValue(NetworkState.loading)
            networkStateAfter.postValue(NetworkState.loading)
            loadAfter(params.key).enqueue(object : retrofit2.Callback<RemoteType> {
                override fun onFailure(call: Call<RemoteType>, t: Throwable) {
                    retry = { loadAfter(params, callback) }
                    networkState.postValue(
                        NetworkState.error(
                            t.message,
                            R.string.network_error_unknown
                        )
                    )
                    networkStateAfter.postValue(NetworkState.error(t.localizedMessage))
                }

                override fun onResponse(call: Call<RemoteType>, response: Response<RemoteType>) {
                    when {
                        response.isSuccessful -> {
                            response.body()?.let {
                                val localData = mapToLocal(it)
                                callback.onResult(localData, params.key + 1)
                                localStorage(localData)
                            }

                            networkState.postValue(NetworkState.success)
                            networkStateAfter.postValue(NetworkState.success)
                        }
                        else -> {
                            networkState.postValue(NetworkState.error(R.string.network_error_unknown))
                            networkStateAfter.postValue(NetworkState.error(R.string.network_error_unknown))
                        }
                    }
                }
            })
        } else{
            callback.onResult(it, params.key + 1)
        }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, LocalType>) {
    }
}

