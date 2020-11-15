package com.nabati.sfa.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

open class BasePageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var models : MutableList<FragmentViewModel> = mutableListOf()

    companion object {
        fun newInstance(fm: FragmentManager?, models:MutableList<FragmentViewModel>) : BasePageAdapter? {
            val page = fm?.let { BasePageAdapter(it) }
            page?.models = models
            return page
        }
    }

    override fun getItem(position: Int): Fragment {
        return models[position].fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence {
        return models[position].title!!
    }

    override fun getCount(): Int {
        return models.size
    }
}

class FragmentViewModel(fragment: Fragment, title:String){
    var fragment: Fragment? = null
    var title:String? = null

    init {
        this.fragment = fragment
        this.title = title
    }
}