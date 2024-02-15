package com.etag.stsyn.ui.states

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

sealed interface DialogState<T> {
    val data: State<T>
    val isVisible: State<Boolean>
}

sealed interface MutableDialogState<T>: DialogState<T> {
    override val data: State<T>
    override val isVisible: State<Boolean>

    fun showDialog(data: T)
    fun showDialog()
    fun hideDialog()
}

private class MutableDialogStateImpl<T>(data: T): MutableDialogState<T> {
    private var _dialogData = mutableStateOf(data)
    private var _isVisible = mutableStateOf(false)

    override val data: State<T> = _dialogData
    override val isVisible: State<Boolean> = _isVisible

    override fun showDialog() {
        _isVisible.value = true
    }

    override fun hideDialog() {
        _isVisible.value = false
    }

    override fun showDialog(data: T) {
        _dialogData.value = data
        _isVisible.value = (data as String).trim().isNotEmpty()
    }

}

fun <T> mutableDialogStateOf(data: T): MutableDialogState<T> {
    return MutableDialogStateImpl(data)
}

@Composable
fun <T> rememberMutableDialogState(data: T) = remember {
    mutableDialogStateOf(data)
}