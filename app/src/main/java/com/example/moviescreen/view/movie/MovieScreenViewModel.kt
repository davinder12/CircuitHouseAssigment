package com.example.moviescreen.view.movie
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.moviescreen.R
import com.example.moviescreen.data.db.LocalGenre
import com.example.moviescreen.data.db.LocalGenreCache
import com.example.moviescreen.data.db.LocalMovie
import com.example.moviescreen.data.db.LocalMovieCache
import com.example.moviescreen.data.extension.postUpdate
import com.example.moviescreen.data.repository.MovieRepository
import com.example.moviescreen.data.service.PreferenceService
import com.example.moviescreen.utilitiesclasses.IListResource
import com.example.moviescreen.utilitiesclasses.PagedListViewModel
import com.example.moviescreen.utilitiesclasses.ResourceViewModel
import com.example.moviescreen.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieScreenViewModel @Inject constructor(private val movieRepository : MovieRepository,
                                               private val cache: LocalMovieCache,
  private val preferenceService: PreferenceService) : BaseViewModel() {

    val pageNumber = MutableLiveData<Int>().also {
        it.value = 1
    }

    var selectedItem = MutableLiveData<String>().also {
        it.value = "Selected Item: "+preferenceService.getInt(R.string.selected_Item)
    }

    val movieList = PagedListViewModel(pageNumber){
        movieRepository.getMovieList()
    }

    fun getCount() :Int {
        return preferenceService.getInt(R.string.selected_Item)
    }


    fun updateMovie(genre: LocalMovie,count :Int) {
        CoroutineScope(Dispatchers.IO).launch {
            cache.updateRecord(genre)
            preferenceService.putInt(R.string.selected_Item,count)
            selectedItem.postUpdate("Selected Item: "+count)
        }
    }
}

