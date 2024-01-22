package com.tzh.retrofit_module.data.local_storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tzh.retrofit_module.data.model.LocalUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataStore @Inject constructor(private val context: Context) {
    companion object {
        const val NAME = "user_prefs"
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(NAME)
        val USER_ID = stringPreferencesKey("user_id")
        val ROLE_ID = stringPreferencesKey("role_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_NRIC = stringPreferencesKey("user_nric")
        val TOKEN = stringPreferencesKey("token")
        val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
    }

    suspend fun saveUser(user: LocalUser) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = user.name
            preferences[USER_ID] = user.userId
            preferences[ROLE_ID] = user.roleId
            preferences[USER_NRIC] = user.nric
            preferences[TOKEN] = user.token
            preferences[IS_LOGGED_IN] = true
        }
    }

    suspend fun updateLoggedInStatus(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn

            // when logout, reset saved data
            if (!isLoggedIn) {
                preferences[USER_NAME] = ""
                preferences[USER_ID] = ""
                preferences[ROLE_ID] = ""
                preferences[USER_NRIC] = ""
                preferences[TOKEN] = ""
            }
        }
    }

    val getUser: Flow<LocalUser> = context.dataStore.data.map { preferences ->
        val name = preferences[USER_NAME] ?: ""
        val id = preferences[USER_ID] ?: ""
        val roleId = preferences[ROLE_ID] ?: ""
        val userNric = preferences[USER_NRIC] ?: ""
        val token = preferences[TOKEN] ?: ""
        val isLoggedIn = preferences[IS_LOGGED_IN] ?: false

        LocalUser(
            name = name,
            userId = id,
            nric = userNric,
            roleId = roleId,
            token = token,
            isLoggedIn = isLoggedIn
        )
    }

}