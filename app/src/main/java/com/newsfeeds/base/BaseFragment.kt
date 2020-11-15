package com.newsfeeds.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.newsfeeds.helper.SharedHelper
import com.newsfeeds.widget.LoadingDialog

/**
 * This class is base view for fragment view and extend from BaseActivity function
 * @see BaseActivity
 */
abstract class BaseFragment<T : BasePresenter<*>> : Fragment() {

    protected var presenter: T? = null
    protected var dActivity: BaseActivity<*>? = null
    protected var currentContext: Context? = null
    protected var inflater: LayoutInflater? = null
    var shared: SharedHelper? = null
    private var loadingDialog: LoadingDialog? = null
    private var isLoading = false

    protected val parentView: View
        get() = requireActivity().findViewById(android.R.id.content)


    abstract override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inflater = LayoutInflater.from(context)
        shared = SharedHelper(context)
        presenter = attachPresenter()
        loadingDialog = LoadingDialog()
    }

    abstract fun attachPresenter(): T

    fun getdActivity(): BaseActivity<*> {
        if (dActivity == null) {
            dActivity = activity as BaseActivity<*>
        }
        return dActivity!!
    }

    override fun onDetach() {
        super.onDetach()
        dActivity = null
    }

    fun finish() {
        if (isAdded)
            requireActivity().finish()
    }

    fun showAlert(
        title: String?,
        message: String,
        listener: DialogInterface.OnClickListener? = null
    ) {
        val baseAct = activity as BaseActivity<*>
        baseAct.showAlert(title, message, listener)
    }

    fun showLoading() {
        val baseAct = activity as? BaseActivity<*>
        baseAct?.showLoading()
    }

    fun showError(error: String?) {
        if (!isAdded)
            return
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    fun dismissLoading() {
        val baseAct = activity as? BaseActivity<*>
        baseAct?.dismissLoading()
    }

    fun doLogout() {
        val baseAct = activity as? BaseActivity<*>
        baseAct?.doLogout()
    }

    fun collapseSearchView(searchView: SearchView) {
        searchView.onActionViewCollapsed()
    }

    fun expandSearchView(searchView: SearchView) {
        searchView.onActionViewExpanded()
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