package com.etag.stsyn.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tzh.retrofit_module.data.network.BaseUrlProvider
import com.tzh.retrofit_module.data.settings.AppConfigModel
import com.tzh.retrofit_module.data.settings.AppConfiguration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appConfiguration: AppConfiguration,
    private val baseUrlProvider: BaseUrlProvider
) : ViewModel() {
    val appConfig = appConfiguration.appConfig

    fun updateAppConfig(appConfigModel: AppConfigModel) {
        viewModelScope.launch {
            appConfiguration.updateAppConfig(appConfigModel)
        }
    }

    fun updateBaseUrl(newBaseUrl: String) {
        baseUrlProvider.updateBaseUrl(newBaseUrl)
    }
}