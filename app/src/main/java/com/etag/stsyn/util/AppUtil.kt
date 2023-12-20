package com.etag.stsyn.util

import android.content.Context

object AppUtil {
    fun getVersionCode(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: Exception) {
            "-1"
        }
    }
}