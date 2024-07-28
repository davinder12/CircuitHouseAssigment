package com.example.moviescreen.view.Dasboard.ui.favoritemovie

import com.example.moviescreen.data.db.LocalGenre
import com.example.moviescreen.data.db.LocalMovie
import com.example.moviescreen.data.db.LocalMovieCache
import com.example.moviescreen.data.extension.map
import com.example.moviescreen.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel  @Inject constructor(private val cache: LocalMovieCache)
    : BaseViewModel(){

    var favMovieList  = cache.getFavMoiveList(true).map {
        it
    }

    fun updateMovie(movie: LocalMovie) {
        CoroutineScope(Dispatchers.IO).launch {
            cache.updateRecord(movie)
        }
    }
}