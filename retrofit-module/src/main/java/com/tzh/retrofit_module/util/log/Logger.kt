package com.tzh.retrofit_module.util.log

import android.util.Log

/**
 * Logger that prints log with auto tag (caller file name)**/
object Logger {
    private val stackTrace = Thread.currentThread().stackTrace
    private val tag = try {
        stackTrace[3].fileName.replace(".kt","")
    } catch (e: Exception) {
        e.printStackTrace()
        "UnknownClass"
    }
    fun d(message: String) {
        Log.d(tag,message)
    }

    fun i(message: String) {
        Log.i(tag,message)
    }

    fun w(message: String) {
        Log.w(tag,message)
    }

    fun e(message: String) {
        Log.e(tag,message)
    }
}