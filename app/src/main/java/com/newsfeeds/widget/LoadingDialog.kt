package com.newsfeeds.widget

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import com.newsfeeds.R

class LoadingDialog {

    var mDialog: Dialog? = null

    fun showLoading(con: Context?) {
        if (con == null) return
        mDialog = Dialog(con, R.style.ProgressDialog)
        mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog?.window?.setBackgroundDrawableResource(R.color.nb_black_transparent)
        mDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mDialog?.setContentView(R.layout.progress_view)
        mDialog?.setCancelable(false)
        mDialog?.setCanceledOnTouchOutside(false)
        mDialog?.show()
    }
}