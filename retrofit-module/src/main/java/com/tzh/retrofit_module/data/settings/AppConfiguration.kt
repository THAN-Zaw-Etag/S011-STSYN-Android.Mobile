package com.tzh.retrofit_module.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppConfiguration @Inject constructor(private val context: Context) {
    companion object {
        private const val CONFIG_NAME = "settings"
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(CONFIG_NAME)

        val READER_ID = stringPreferencesKey("reader_id")
        val READER_SERIAL_NO = stringPreferencesKey("reader_serial_no")
        val API_URL = stringPreferencesKey("api_url")
        val POWER = longPreferencesKey("reader_power")
        val STORE_TYPE = stringPreferencesKey("store_type")
        val CS_NO = stringPreferencesKey("cs_no")
        val NEED_LOCATION = booleanPreferencesKey("need_location")
    }

    suspend fun updateAppConfig(appConfig: AppConfigModel) {
        context.dataStore.edit {
            it[READER_ID] = appConfig.handheldReaderId
            it[READER_SERIAL_NO] = appConfig.handheldReaderSerialNo
            it[API_URL] = appConfig.apiUrl
            it[POWER] = appConfig.power
            it[STORE_TYPE] = appConfig.store.toString()
            it[CS_NO] = appConfig.csNo
            it[NEED_LOCATION] = appConfig.needLocation
        }
    }

    val appConfig: Flow<AppConfigModel> = context.dataStore.data.map {
        val readerId = it[READER_ID] ?: "1"
        val serialNo = it[READER_SERIAL_NO] ?: "1"
        val apiUrl = it[API_URL] ?: ""
        val power = it[POWER] ?: 0L
        val storeType = StoreType.valueOf(it[STORE_TYPE] ?: StoreType.DCS.toString())
        val csNo = it[CS_NO] ?: "1"
        val needLocation = it[NEED_LOCATION] ?: false

        AppConfigModel(
            handheldReaderId = readerId,
            handheldReaderSerialNo = serialNo,
            apiUrl = apiUrl,
            power = power,
            store = storeType,
            csNo = csNo,
            needLocation = needLocation
        )
    }
}