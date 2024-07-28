package com.example.moviescreen.view.Genre

import android.os.Bundle
import androidx.activity.viewModels
import com.example.moviescreen.R
import com.example.moviescreen.databinding.ActivityMainBinding
import com.example.moviescreen.utilitiesclasses.baseclass.DataBindingActivity
import com.example.moviescreen.view.Genre.adapter.GenreListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenreActivity : DataBindingActivity<ActivityMainBinding>() {

    private val viewModel: GenreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.language.value = "en";
        val adapter =  initAdapter(GenreListAdapter(), binding.movielist, viewModel.genresList,viewModel.merchantResource)
        subscribe(adapter.checkBoxClick){
            var count = viewModel.getCount()
            if(count < 10 ) {
                if(it.isCheck){ count--; }else { count++; }
                it.isCheck = !it.isCheck
                viewModel.updateGenre(it,count)
            }else {
                showMessage("You already exceed the limit of 10")
                adapter.submitList(adapter.currentList)
            }
        }

        binding.genreToolbar.setNavigationOnClickListener {
            finish()
        }
        viewModel.selectedItem.observe(this){
            binding.selectedItem.text = it
        }
    }




    override val layoutRes: Int
        get() = R.layout.activity_main
}