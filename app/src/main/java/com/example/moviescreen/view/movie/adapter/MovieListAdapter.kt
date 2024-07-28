package com.example.moviescreen.view.movie.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.moviescreen.R
import com.example.moviescreen.data.db.LocalGenre
import com.example.moviescreen.data.db.LocalMovie
import com.example.moviescreen.data.extension.throttle
import com.example.moviescreen.data.extension.throttleClicks
import com.example.moviescreen.databinding.ItemMovie2ListBinding
import com.example.moviescreen.utilitiesclasses.baseadapter.DataBoundPagedListAdapter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

class MovieListAdapter : DataBoundPagedListAdapter<LocalMovie, ItemMovie2ListBinding>(diffCallback) {

    /**
     * The [LayoutRes] for the RecyclerView item
     * This is used to inflate the view.
     */
    override val defaultLayoutRes: Int
        get() = R.layout.item_movie2_list

    private var checkBoxClickEmitter: ObservableEmitter<LocalMovie>? = null
    val checkBoxClick: Observable<LocalMovie> = Observable.create { checkBoxClickEmitter = it }.throttle()



    override fun bind(
        bind: ItemMovie2ListBinding,
        itemType: LocalMovie?,
        position: Int
    ) {
        itemType?.let { response ->
            bind.item = response
            bind.movieListSelected.throttleClicks().subscribe{
                checkBoxClickEmitter?.onNext(response)
            }
        }
    }

    override fun map(binding: ItemMovie2ListBinding): LocalMovie? {
        return binding.item
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<LocalMovie>() {
            override fun areItemsTheSame(
                oldItem: LocalMovie,
                newItem: LocalMovie
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: LocalMovie,
                newItem: LocalMovie
            ): Boolean = oldItem.id == newItem.id && oldItem.isCheck == newItem.isCheck
        }
    }
}
