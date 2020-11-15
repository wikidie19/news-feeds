package com.newsfeeds.helper

import android.text.TextUtils
import android.util.Log
import androidx.annotation.IntRange
import com.google.gson.Gson

object LoggerHelper {

    private const val VERBOSE = Log.VERBOSE
    private const val DEBUG = Log.DEBUG
    private const val INFO = Log.INFO
    private const val WARN = Log.WARN
    private const val ERROR = Log.ERROR
    private const val WTF = Log.ASSERT

    var TAG = "com.newsfeeds"

    fun log(@IntRange(from = VERBOSE.toLong(), to = WTF.toLong()) level: Int, msg: String?) {

        val elements = Throwable().stackTrace
        var callerClassName = "?"
        var callerMethodName = "?"
        var callerLineNumber = "?"
        if (elements.size >= 3) {
            callerClassName = elements[2].className
            callerClassName = callerClassName.substring(callerClassName.lastIndexOf('.') + 1)
            if (callerClassName.indexOf("$") > 0) {
                callerClassName = callerClassName.substring(0, callerClassName.indexOf("$"))
            }
            callerMethodName = elements[2].methodName
            callerMethodName = callerMethodName.substring(callerMethodName.lastIndexOf('_') + 1)
            if (callerMethodName == "<init>") {
                callerMethodName = callerClassName
            }
            callerLineNumber = elements[2].lineNumber.toString()
        }

        val stack =
            "[" + callerClassName + "." + callerMethodName + "():" + callerLineNumber + "]" + if (TextUtils.isEmpty(
                    msg
                )
            ) "" else " "
        when (level) {
            VERBOSE -> Log.v(TAG, stack + msg!!)
            DEBUG -> Log.d(TAG, stack + msg!!)
            INFO -> Log.i(TAG, stack + msg!!)
            WARN -> Log.w(TAG, stack + msg!!)
            ERROR -> Log.e(TAG, stack + msg!!)
            WTF -> Log.wtf(TAG, stack + msg!!)
            else -> {
            }
        }
    }

    fun debug(`object`: Any) {
        log(DEBUG, jsonify(`object`))
    }

    fun debug(msg: String) {
        log(DEBUG, msg)
    }

    fun verbose(`object`: Any) {
        log(VERBOSE, jsonify(`object`))
    }

    fun verbose(msg: String) {
        log(VERBOSE, msg)
    }

    fun info(`object`: Any) {
        log(INFO, jsonify(`object`))
    }

    fun info(msg: String) {
        log(INFO, msg)
    }

    fun warning(`object`: Any) {
        log(WARN, jsonify(`object`))
    }

    fun warning(msg: String) {
        log(WARN, msg)
    }

    fun error(`object`: Any) {
        log(ERROR, jsonify(`object`))
    }

    fun error(msg: String) {
        log(ERROR, msg)
    }

    fun wtf(item: Any) {
        log(WTF, jsonify(item))
    }

    fun wtf(msg: String) {
        log(WTF, msg)
    }

    fun jsonify(item: Any): String {
        return Gson().toJson(item, item.javaClass)
    }

}
