package com.example.moviescreen.view.Genre.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.moviescreen.R
import com.example.moviescreen.data.db.LocalGenre
import com.example.moviescreen.data.extension.throttle
import com.example.moviescreen.data.extension.throttleClicks
import com.example.moviescreen.databinding.ItemMovieListBinding
import com.example.moviescreen.utilitiesclasses.baseadapter.DataBoundAdapterClass
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

class GenreListAdapter () : DataBoundAdapterClass<LocalGenre, ItemMovieListBinding>(diffCallback) {

    /**
     * The [LayoutRes] for the RecyclerView item
     * This is used to inflate the view.
     */
    override val defaultLayoutRes: Int
        get() = R.layout.item_movie_list

    private var checkBoxClickEmitter: ObservableEmitter<LocalGenre>? = null
    val checkBoxClick: Observable<LocalGenre> = Observable.create { checkBoxClickEmitter = it }.throttle()

    override fun bind(
        bind: ItemMovieListBinding,
        itemType: LocalGenre?,
        position: Int
    ) {
        itemType?.let { response ->
            bind.item = response
            bind.generSelected.throttleClicks().subscribe{
                checkBoxClickEmitter?.onNext(response)
            }
        }
    }

    override fun map(binding: ItemMovieListBinding): LocalGenre? {
        return binding.item
    }
    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<LocalGenre>() {
                override fun areItemsTheSame(
                    oldItem: LocalGenre,
                    newItem: LocalGenre
                ): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: LocalGenre,
                    newItem: LocalGenre
                ): Boolean = (oldItem.name == newItem.name
                        && oldItem.id == newItem.id && oldItem.isCheck == newItem.isCheck)
            }
    }
}
