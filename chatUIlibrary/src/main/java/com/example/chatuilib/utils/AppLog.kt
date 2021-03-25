package com.example.chatuilib.utils

import android.util.Log
import com.example.chatuilib.BuildConfig

/**
 * This Log class is used to print logs in debug build only
 */
object AppLog {
    private val TAG = AppLog::class.java.simpleName
    private val DO_LOG: Boolean = BuildConfig.DEBUG

    /**
     * This method is used to print message
     * @param msg the text to print
     */
    fun print(msg: String) {
        if (DO_LOG) {
            println(msg)
        }
    }

    /**
     * This method is used to print title and message
     * @param title the title to print
     * @param msg the message to print
     */
    fun print(title: String, msg: String) {
        if (DO_LOG) {
            println("$title :: $msg")
        }
    }

    /**
     * This method is used to print tag, title and message
     * @param tag the name of class to print
     * @param title the title to print
     * @param msg the message to print
     */
    fun print(tag: String, title: String, msg: String) {
        if (DO_LOG) {
            println("$tag :: $title :: $msg")
        }
    }

    /**
     * This method is used to print verbose log
     * @param title the title to print
     * @param msg the message to print
     */
    fun v(title: String?, msg: String) {
        if (DO_LOG) {
            Log.v(title, msg)
        }
    }

    /**
     * This method is used to print verbose log
     * @param title the title to print
     * @param msg the message to print
     * @param throwable Throwable to print
     */
    fun v(
        title: String?,
        msg: String,
        throwable: Throwable?
    ) {
        if (DO_LOG) {
            Log.v(title, msg, throwable)
        }
    }

    /**
     * This method is used to print debug log
     * @param title the title to print
     * @param msg the message to print
     */
    fun d(title: String?, msg: String) {
        if (DO_LOG) {
            Log.d(title, msg)
        }
    }

    /**
     * This method is used to print debug log
     * @param title the title to print
     * @param msg the message to print
     * @param throwable Throwable to print
     */
    fun d(
        title: String?,
        msg: String,
        throwable: Throwable?
    ) {
        if (DO_LOG) {
            Log.d(title, msg, throwable)
        }
    }

    /**
     * This method is used to print warn log
     * @param title the title to print
     * @param msg the message to print
     */
    fun w(title: String?, msg: String) {
        if (DO_LOG) {
            Log.w(title, msg)
        }
    }

    /**
     * This method is used to print warn log
     * @param title the title to print
     * @param throwable Throwable to print
     */
    fun w(title: String?, throwable: Throwable?) {
        if (DO_LOG) {
            Log.w(title, throwable)
        }
    }

    /**
     * This method is used to print warn log
     * @param title the title to print
     * @param msg the message to print
     * @param throwable Throwable to print
     */
    fun w(
        title: String?,
        msg: String,
        throwable: Throwable?
    ) {
        if (DO_LOG) {
            Log.w(title, msg, throwable)
        }
    }

    /**
     * This method is used to print info log
     * @param title the title to print
     * @param msg the message to print
     * @param throwable Throwable to print
     */
    fun i(
        title: String?,
        msg: String,
        throwable: Throwable?
    ) {
        if (DO_LOG) {
            Log.i(title, msg, throwable)
        }
    }

    /**
     * This method is used to print info log
     * @param title the title to print
     * @param msg the message to print
     */
    fun i(title: String?, msg: String) {
        if (DO_LOG) {
            Log.i(title, msg)
        }
    }

    /**
     * This method is used to print error log
     * @param title the title to print
     * @param msg the message to print
     */
    fun e(title: String?, msg: String) {
        if (DO_LOG) {
            Log.e(title, msg)
        }
    }

    /**
     * This method is used to print error log
     * @param title the title to print
     * @param e Exception to print
     */
    fun e(title: String?, e: Exception) {
        if (DO_LOG) {
            Log.e(title, "Exception :: $e")
        }
    }

    /**
     * This method is used to print error log
     * @param title the title to print
     * @param t Throwable to print
     */
    fun e(title: String?, t: Throwable) {
        if (DO_LOG) {
            Log.e(title, "Throwable :: $t")
        }
    }

    /**
     * This method is used to print error log
     * @param title the title to print
     * @param msg the message to print
     * @param e Exception to print
     */
    fun e(title: String?, msg: String, e: Exception?) {
        if (DO_LOG) {
            Log.e(title, msg, e)
        }
    }

    /**
     * This method is used to print error log
     * @param title the title to print
     * @param msg the message to print
     * @param t Throwable to print
     */
    fun e(title: String?, msg: String, t: Throwable?) {
        if (DO_LOG) {
            Log.e(title, msg, t)
        }
    }
}
