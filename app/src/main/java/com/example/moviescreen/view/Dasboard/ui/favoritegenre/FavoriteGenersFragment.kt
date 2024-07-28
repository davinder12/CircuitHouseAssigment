package com.example.moviescreen.view.Dasboard.ui.favoritegenre

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.moviescreen.R
import com.example.moviescreen.databinding.FragmentFavGenreBinding
import com.example.moviescreen.utilitiesclasses.baseclass.BaseListFragment
import com.example.moviescreen.view.Dasboard.DashBoardScreen
import com.example.moviescreen.view.Genre.adapter.GenreListAdapter
import com.example.moviescreen.view.movie.MovieScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteGenersFragment : BaseListFragment<FragmentFavGenreBinding>(){

    private val viewModel: FavoriteGenersViewModel by viewModels()


    override val layoutRes: Int
        get() = R.layout.fragment_fav_genre

    var count = 0;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adatper =  initAdapter(GenreListAdapter(), binding.favgenreList, viewModel.generList)
        count = (activity as DashBoardScreen).viewModel.getCount()
        subscribe(adatper.checkBoxClick){
           it.isCheck = !it.isCheck
           viewModel.updateGenre(it)
           if(it.isCheck){ count++; }else { count--; }
           (activity as DashBoardScreen).viewModel.updateRecord(count)
       }
    }
}
