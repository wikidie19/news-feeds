package com.newsfeeds.base

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.newsfeeds.helper.ApplicationHelper
import com.newsfeeds.helper.LocaleHelper
import com.newsfeeds.helper.SharedHelper
import com.newsfeeds.widget.LoadingDialog

abstract class BaseActivity<T : BasePresenter<*>> : AppCompatActivity() {

    var presenter: T? = null
    private var dialog: AlertDialog? = null
    private var appHelper: ApplicationHelper? = null
    private var loadingDialog: LoadingDialog? = null
    private var isLoading = false
    var sharedValue: SharedHelper? = null

    val contentView: View
        get() = findViewById(android.R.id.content)

    protected val parentView: View
        get() = findViewById(android.R.id.content)

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        loadingDialog = LoadingDialog()
        this.appHelper = ApplicationHelper(this)
        this.presenter = attachPresenter()
        this.sharedValue = SharedHelper(this)

    }

    abstract fun attachPresenter(): T

    override fun onStart() {
        super.onStart()
        BaseApplication.activityOnStart()
    }

    override fun onResume() {
        super.onResume()
        BaseApplication.activityResumed()
    }

    override fun onPause() {
        super.onPause()
        BaseApplication.activityPaused()
    }

    fun showAlert(
        title: String?,
        message: String,
        listener: DialogInterface.OnClickListener? = null
    ) {
        if (message.isEmpty()) {
            return
        }

        if (this.dialog != null) {
            if (this.dialog!!.isShowing) {
                return
            }
        }

        val alert = AlertDialog.Builder(this)
        alert.setMessage(message)
        alert.setPositiveButton("OK", listener)
        if (title != null)
            alert.setTitle(title)

        alert.show()

        this.dialog = alert.create()
    }

    /**
     * Show error message
     */
    fun showError(error: String?) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    /**
     * Logout apps and clear all service and data
     */
    fun doLogout() {
        presenter?.sessionLogout()
        finishAffinity()
    }

    open fun showLoading(isCancelable: Boolean) {
        if (applicationContext == null) return
        if (loadingDialog != null && !isLoading) {
            isLoading = true
            loadingDialog?.showLoading(this)
            loadingDialog?.mDialog?.setCancelable(isCancelable)
            loadingDialog?.mDialog?.setCanceledOnTouchOutside(isCancelable)
        }
    }

    open fun showLoading() {
        if (applicationContext == null) return
        if (loadingDialog != null && !isLoading) {
            isLoading = true
            loadingDialog?.showLoading(this)
        }
    }

    open fun dismissLoading() {
        if (loadingDialog?.mDialog != null && isLoading) {
            isLoading = false
            loadingDialog?.mDialog?.dismiss()
        }
    }

    override fun onDestroy() {
        dismissLoading()
        super.onDestroy()
    }

}