package com.example.moviescreen.utilitiesclasses

import androidx.lifecycle.LiveData
import com.example.moviescreen.data.extension.liveData
import com.example.moviescreen.data.extension.map
import com.example.moviescreen.data.extension.switchMap
import com.example.moviescreen.data.network.IResource
import com.example.moviescreen.data.network.NetworkState


/**
 * A wrapper for [IResource] meant to be used in a ViewModel.
 * It provides [LiveData] for data, refresh status and network status.
 * It also provides functions to retry any failed requests and to refresh the resource.
 */
class ResourceViewModel<EntityType>(private var resource: LiveData<IResource<EntityType>>) {

    constructor(resource: IResource<EntityType>) : this(liveData(resource))

    companion object {

        /**
         * Initialize with a [LiveData] trigger and a lambda that returns an [IResource].
         */
        operator fun <TriggerType, EntityType> invoke(
            src: LiveData<TriggerType>,
            f: (TriggerType) -> IResource<EntityType>
        ): ResourceViewModel<EntityType> {
            return ResourceViewModel(src.map(f))
//            return ResourceViewModel(src.switchMap {
//                asLiveData(f(it))
//            })
        }

    }


    /**
     * True if the resource is refreshing it's data.
     */
    val isRefreshing: LiveData<Boolean> by lazy {
        resource.switchMap {
            it.isRefreshing
        }
//        Transformations.switchMap(resource) {
//            it.isRefreshing
//        }
    }

    /**
     * The network state of the resource.
     */
    val networkState: LiveData<NetworkState> by lazy {
        resource.switchMap {
            it.networkState
        }
       // Transformations.switchMap(resource) { it.networkState }
    }

    /**
     * The resource data.
     */
    val listData: LiveData<List<EntityType>> = resource.switchMap {
        it.dataList
    }

    /**
     * The resource data.
     */
    val data: LiveData<EntityType> = resource.switchMap {
        it.data
    }



    fun retry(networkState: NetworkState) {
        resource.value?.retry(networkState)
    }

    /**
     * Refresh resource data.
     */
    fun refresh() {
        resource.value?.refresh()
    }
}
