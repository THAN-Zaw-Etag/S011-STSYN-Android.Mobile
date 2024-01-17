package com.etag.stsyn.data.localStorage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.etag.stsyn.data.model.LocalUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataStore @Inject constructor(private val context: Context) {
    companion object {
        const val NAME = "user_prefs"
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(NAME)
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_NRIC = stringPreferencesKey("user_nric")
        val TOKEN = stringPreferencesKey("token")
        val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
    }

    suspend fun saveUser(user: LocalUser) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = user.name
            preferences[USER_ID] = user.id
            preferences[USER_NRIC] = user.nric
            preferences[TOKEN] = user.token
            preferences[IS_LOGGED_IN] = true
        }
    }

    val getUser: Flow<LocalUser> = context.dataStore.data.map { preferences ->
        val name = preferences[USER_NAME] ?: ""
        val id = preferences[USER_ID] ?: ""
        val userNric = preferences[USER_NRIC] ?: ""
        val token = preferences[TOKEN] ?: ""

        LocalUser(name = name, id = id, nric = userNric, token = token)
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map {
        val hasLoggedIn = it[IS_LOGGED_IN] ?: false
        println("hasLoggedIn: $hasLoggedIn")
        hasLoggedIn
    }
}