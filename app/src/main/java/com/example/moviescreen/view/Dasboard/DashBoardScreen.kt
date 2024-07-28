package com.example.moviescreen.view.Dasboard

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.moviescreen.R
import com.example.moviescreen.databinding.ActivityDashBoardScreenBinding
import com.example.moviescreen.utilitiesclasses.baseclass.BaseActivity
import com.example.moviescreen.utilitiesclasses.baseclass.DataBindingActivity
import com.example.moviescreen.view.Dasboard.model.DashboardViewModel
import com.example.moviescreen.view.Genre.GenreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_dash_board_screen.*

@AndroidEntryPoint
class DashBoardScreen : DataBindingActivity<ActivityDashBoardScreenBinding>(){

    val viewModel: DashboardViewModel by viewModels()

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDrawerBottomSheet()
        viewModel.selectedItem.observe(this){
            binding.selectedItem.text = "Selected Item: ($it)"
        }
   }

    private fun initDrawerBottomSheet() {
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment_activity_dash_board_screen)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //navView.itemIconTintList = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshRecord()
    }

    override val layoutRes: Int
        get() = R.layout.activity_dash_board_screen
}