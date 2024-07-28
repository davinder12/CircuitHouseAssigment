package com.example.moviescreen.view.Dasboard.model
import android.util.Log
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
import java.util.prefs.Preferences
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val preferenceService: PreferenceService) : BaseViewModel() {

    var selectedItem = MutableLiveData<Int>()


    fun updateRecord(count : Int){
       preferenceService.putInt(R.string.selected_Item,count)
        selectedItem.value = count;
    }

    fun refreshRecord(){
        selectedItem.postUpdate(preferenceService.getInt(R.string.selected_Item))
    }

    fun getCount() : Int {
      return  preferenceService.getInt(R.string.selected_Item)
    }
}

