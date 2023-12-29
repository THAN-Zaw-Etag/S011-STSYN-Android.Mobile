package com.etag

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.etag.stsyn.core.BaseViewModel

@Composable
fun ReaderLifeCycle(
    viewModel: BaseViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val lifecycleEventObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    Log.d("TAG", "ReaderLifeCycle: OnCreate")
                    viewModel.setRfidListener()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.removeListener()
                }

                Lifecycle.Event.ON_DESTROY -> {

                }

                else -> {

                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleEventObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleEventObserver)
        }
    }
}