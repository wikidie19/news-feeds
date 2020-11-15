package com.newsfeeds.helper

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.EXTRA_NETWORK_TYPE
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast

object NetworkHelper {

    const val NETWORK_NOT_CONNECTED = -1
    const val NETWORK_CONNECTED = 1

    //this variable is equal with -> ConnectivityManager.EXTRA_INET_CONDITION
    private val EXTRA_INET_CONDITION = "inetCondition"

    fun isLackOfConnection(intent: Intent): Boolean {
        return intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
    }

    fun getNetworkConnectionType(intent: Intent): Int {
        return intent.getIntExtra(EXTRA_NETWORK_TYPE, -1)
    }

    fun getInetCondition(intent: Intent): String {
        return intent.getIntExtra(EXTRA_INET_CONDITION, 0).toString() + "%"
    }

    fun isWifi(type: Int): Boolean {
        return type == ConnectivityManager.TYPE_WIFI
    }

    fun isBroadband(type: Int): Boolean {
        return type == ConnectivityManager.TYPE_MOBILE
    }

    fun isConnected(context: Context): Boolean {
        val status = getConnectivityStatus(context)
        val isConnection = status == NETWORK_CONNECTED
        return isConnection
    }

    fun showErrorMessageOnUiThread(context: Context, errorMessage: String) {
        val mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        val message = mHandler.obtainMessage(-1, errorMessage)
        message.sendToTarget()
    }

    fun getConnectionManager(context: Context): ConnectivityManager? {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun getNetworkInfo(connectivityManager: ConnectivityManager): NetworkInfo? {
        return connectivityManager.activeNetworkInfo
    }

    fun getConnectivityStatus(context: Context): Int {
        val cm = getConnectionManager(context)!!
        val activeNetwork = getNetworkInfo(cm)
        if (null != activeNetwork) {
            println("active network: ${activeNetwork.type}")
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                return NETWORK_CONNECTED

            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                return NETWORK_CONNECTED
        }
        return NETWORK_NOT_CONNECTED
    }

}
