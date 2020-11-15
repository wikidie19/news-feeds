package com.newsfeeds.modul.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.newsfeeds.R
import com.newsfeeds.base.BaseActivity
import com.newsfeeds.modul.search.SearchArticleActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainPresenter>(), IMainView {

    private lateinit var navController: NavController

    override fun attachPresenter(): MainPresenter {
        return MainPresenter(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onViewInit()
    }

    override fun onViewInit() {

        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(bnvMain, navController)

    }

    override fun onError(message: String?) {
        showError(message)
    }
}