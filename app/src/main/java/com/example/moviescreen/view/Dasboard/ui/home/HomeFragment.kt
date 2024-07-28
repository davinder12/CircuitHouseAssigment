package com.example.moviescreen.view.Dasboard.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.moviescreen.R
import com.example.moviescreen.data.extension.throttleClicks
import com.example.moviescreen.databinding.FragmentHomeBinding
import com.example.moviescreen.utilitiesclasses.baseclass.BaseListFragment
import com.example.moviescreen.view.Genre.GenreActivity
import com.example.moviescreen.view.movie.MovieScreen

class HomeFragment : BaseListFragment<FragmentHomeBinding>(){
    override val layoutRes: Int
    get() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe(binding.favoriteGenres.throttleClicks()){
           Intent(context, GenreActivity::class.java).also {
               startActivity(it)
            }
        }
        subscribe(binding.favoriteMovie.throttleClicks()){
            Intent(context, MovieScreen::class.java).also {
                startActivity(it)
            }
        }
    }
}