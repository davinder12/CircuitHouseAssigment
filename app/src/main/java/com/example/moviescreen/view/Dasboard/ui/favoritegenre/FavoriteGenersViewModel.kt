package com.example.moviescreen.view.Dasboard.ui.favoritegenre

import com.example.moviescreen.data.db.LocalGenre
import com.example.moviescreen.data.db.LocalGenreCache
import com.example.moviescreen.data.extension.map
import com.example.moviescreen.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteGenersViewModel @Inject constructor(private val cache: LocalGenreCache)
    : BaseViewModel(){

    var generList = cache.getFavGenerList(true).map {
           it
    }

    fun updateGenre(genre: LocalGenre) {
        CoroutineScope(Dispatchers.IO).launch {
            cache.updateGenre(genre)
        }
    }
}

