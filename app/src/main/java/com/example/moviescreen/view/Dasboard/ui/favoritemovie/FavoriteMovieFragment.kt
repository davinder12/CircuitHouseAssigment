package com.example.moviescreen.view.Dasboard.ui.favoritemovie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.moviescreen.R
import com.example.moviescreen.databinding.FragmentFavMovieBinding
import com.example.moviescreen.utilitiesclasses.baseclass.BaseListFragment
import com.example.moviescreen.view.Dasboard.DashBoardScreen
import com.example.moviescreen.view.Dasboard.ui.favoritegenre.FavoriteGenersViewModel
import com.example.moviescreen.view.Genre.adapter.GenreListAdapter
import com.example.moviescreen.view.movie.adapter.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMovieFragment : BaseListFragment<FragmentFavMovieBinding>(){

    private val viewModel: FavoriteMovieViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.fragment_fav_movie

    var count = 0;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        count = (activity as DashBoardScreen).viewModel.getCount()
        var adapter = initAdapter(FavMovieListAdapter(), binding.favMovieList, viewModel.favMovieList)
        subscribe(adapter.checkBoxClick){
            it.isCheck = !it.isCheck
            viewModel.updateMovie(it)
            if(it.isCheck){ count++; }else { count--; }
            (activity as DashBoardScreen).viewModel.updateRecord(count)
        }
    }

}