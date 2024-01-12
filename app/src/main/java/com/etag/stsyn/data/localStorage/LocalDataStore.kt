package com.etag.stsyn.data.localStorage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.etag.stsyn.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataStore @Inject constructor(private val context: Context) {
    companion object {
        const val NAME = "user_prefs"
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(NAME)
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = user.name
            preferences[USER_ID] = user.rfidId
        }
    }

    val getUser: Flow<User> = context.dataStore.data.map { preferences ->
        val name = preferences[USER_NAME] ?: ""
        val id = preferences[USER_ID] ?: ""

        User(name = name, rfidId = id, "", "")
    }
}