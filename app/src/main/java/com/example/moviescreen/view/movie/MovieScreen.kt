package com.example.moviescreen.view.movie

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.moviescreen.R
import com.example.moviescreen.data.extension.throttle
import com.example.moviescreen.databinding.ActivityMovieScreenBinding
import com.example.moviescreen.utilitiesclasses.baseclass.DataBindingActivity
import com.example.moviescreen.view.movie.adapter.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieScreen : DataBindingActivity<ActivityMovieScreenBinding>()  {

    private val viewModel: MovieScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = initAdapter(MovieListAdapter(), binding.listOfMovies, viewModel.movieList)
        subscribe(adapter.checkBoxClick.throttle()){

            var count = viewModel.getCount()
            if(count < 10 ) {
                if(it.isCheck){ count--; }else { count++; }
                it.isCheck = !it.isCheck
                viewModel.updateMovie(it,count)
            }else {
                adapter.submitList(adapter.currentList)
                showMessage("You Already exceed the limit of 10")
            }

        }
        binding.movieToolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.selectedItem.observe(this){
            binding.selectedItem.text = it
        }

    }



    override val layoutRes: Int
        get() = R.layout.activity_movie_screen
}