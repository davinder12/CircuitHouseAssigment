package com.example.moviescreen.utilitiesclasses.baseclass

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.moviescreen.utilitiesclasses.PagedListViewModel
import com.example.moviescreen.utilitiesclasses.ResourceViewModel
import com.example.moviescreen.utilitiesclasses.baseadapter.DataBoundAdapterClass
import com.example.moviescreen.utilitiesclasses.baseadapter.DataBoundPagedListAdapter
import io.reactivex.rxkotlin.plusAssign

/**
 * [BaseFragment] that adds helper methods for displaying lists.
 */
abstract class BaseListFragment<TBinding : ViewDataBinding> : BaseFragment<TBinding>() {


    protected fun <X : DataBoundAdapterClass<T, *>, T> initAdapter(
        adapter: X,
        recycler: RecyclerView,
        list: LiveData<List<T>>,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        recycler.adapter = adapter
        list.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
        clickHandler?.let { subscribe(adapter.clicks, it) }
        return adapter
    }


    // TODO Need to refactor retryClick  funtionality
    @SuppressLint("CheckResult")
    protected fun <X : DataBoundAdapterClass<T, *>, T, R> initAdapter(
        adapter: X,
        recycler: RecyclerView,
        list: LiveData<List<T>?>,
        viewModel: ResourceViewModel<R>,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        recycler.adapter = adapter
        list.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        adapter.retryClicks.subscribe {
            viewModel.retry(it)
        }
        viewModel.networkState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.setNetworkState(it)
        })
        clickHandler?.let { subscribe(adapter.clicks, it) }
        return adapter
    }


    protected fun <X : DataBoundPagedListAdapter<T, *>, T> initAdapter(
        adapter: X,
        recyclerView: RecyclerView,
        viewModel: PagedListViewModel<T>,
        hasFixedSize: Boolean = true,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        recyclerView.setHasFixedSize(hasFixedSize)
        recyclerView.adapter = adapter
        subscriptions += adapter.retryClicks.subscribe(viewModel::retry)
        clickHandler?.let { subscribe(adapter.clicks, it) }
        viewModel.items.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.submitList(it)
        })
        viewModel.frontLoadingState.observe(viewLifecycleOwner, Observer {
            adapter.setNetworkState(it)
        })
        viewModel.endLoadingState.observe(viewLifecycleOwner, Observer {
            adapter.setNetworkState(it)
        })

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) recyclerView.scrollToPosition(0)
            }
        })
        return adapter
    }

    fun <X : DataBoundAdapterClass<T, *>, T> initAdapter(
        adapter: X,
        viewPager: ViewPager2,
        data: LiveData<List<T>?>,
        listSize : (Int)->Unit,
        clickHandler: ((T) -> Unit)? = null
    ) {
        viewPager.adapter = adapter
        data.observe(viewLifecycleOwner, Observer {
           it?.let { listSize.invoke(it.size) }
            adapter.submitList(it)

        })
        clickHandler?.let { subscribe(adapter.clicks, it) }
    }
}