package com.newsfeeds.modul.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newsfeeds.R
import com.newsfeeds.base.BaseFragment

class ProfileFragment: BaseFragment<ProfilePresenter>(), IProfileView {

    lateinit var mView: View

    override fun attachPresenter(): ProfilePresenter {
        return ProfilePresenter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_profile, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewInit()
    }

    override fun onViewInit() {

    }

    override fun onError(message: String?) {
        showError(message)
    }
}