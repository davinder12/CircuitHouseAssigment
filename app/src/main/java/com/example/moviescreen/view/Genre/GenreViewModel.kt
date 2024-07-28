package com.example.moviescreen.view.Genre
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.moviescreen.R
import com.example.moviescreen.data.db.LocalGenre
import com.example.moviescreen.data.db.LocalGenreCache
import com.example.moviescreen.data.extension.postUpdate
import com.example.moviescreen.data.repository.MovieRepository
import com.example.moviescreen.data.service.PreferenceService
import com.example.moviescreen.utilitiesclasses.ResourceViewModel
import com.example.moviescreen.utilitiesclasses.baseclass.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(private val movieRepository : MovieRepository,
                                         private val cache: LocalGenreCache,private val preferenceService: PreferenceService) : BaseViewModel() {

    val language = MutableLiveData<String>()

    val merchantResource = ResourceViewModel(language) {
        movieRepository.getMovieList()
    }
    val genresList = merchantResource.listData.map {
       it
    }

    var selectedItem = MutableLiveData<String>().also {
        it.value = "Selected Item: "+getCount()
    }

    fun getCount() :Int {
        return preferenceService.getInt(R.string.selected_Item)
    }

    fun updateGenre(genre: LocalGenre,count :Int) {
        CoroutineScope(Dispatchers.IO).launch {
            cache.updateGenre(genre)
            preferenceService.putInt(R.string.selected_Item,count)
            selectedItem.postUpdate("Selected Item: "+count)
        }
    }
}

