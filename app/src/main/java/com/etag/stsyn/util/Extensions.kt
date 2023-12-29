package com.etag.stsyn.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

fun Context.isBluetoothPermissionGranted(): Boolean {
    val blueToothPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ActivityCompat.checkSelfPermission(
            this, Manifest.permission.BLUETOOTH_SCAN
        )
    } else {
        PackageManager.PERMISSION_GRANTED
    }
    return blueToothPermission == PackageManager.PERMISSION_GRANTED
}