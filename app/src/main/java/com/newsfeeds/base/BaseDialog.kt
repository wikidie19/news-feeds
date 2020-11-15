package com.newsfeeds.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.newsfeeds.R
import com.newsfeeds.helper.SharedHelper
import com.newsfeeds.widget.LoadingDialog

abstract class BaseDialog<T : BasePresenter<*>> : DialogFragment() {

    protected var presenter: T? = null
    open var sharedValue: SharedHelper? = null
    private var loadingDialog: LoadingDialog? = null
    private var isLoading = false


    protected val parentView: View
        get() = requireActivity().findViewById(android.R.id.content)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.presenter = attachPresenter()
        this.sharedValue = SharedHelper(requireContext())
        loadingDialog = LoadingDialog()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppCompat_Dialog)
    }

    abstract fun attachPresenter(): T

    fun showError(error: String?) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    fun showLoadingDialog() {
        if (context == null) return
        if (loadingDialog != null && !isLoading) {
            isLoading = true
            loadingDialog?.showLoading(context)
        }
    }

    fun dismissLoadingDialog() {
        if (loadingDialog?.mDialog != null && isLoading) {
            isLoading = false
            loadingDialog?.mDialog?.dismiss()
        }
    }

}