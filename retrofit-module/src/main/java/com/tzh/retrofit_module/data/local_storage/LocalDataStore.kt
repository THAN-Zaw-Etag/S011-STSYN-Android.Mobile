package com.tzh.retrofit_module.data.local_storage

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.domain.model.user.UserModel
import com.tzh.retrofit_module.util.log.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataStore @Inject constructor(private val context: Context) {
    companion object {
        const val TAG = "LocalDataStore"
        private const val NAME = "user_prefs"
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(NAME)
        val USER_ID = stringPreferencesKey("user_id")
        val ROLE_ID = stringPreferencesKey("role_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_NRIC = stringPreferencesKey("user_nric")
        val TOKEN = stringPreferencesKey("token")
        val IS_LOGGED_IN = booleanPreferencesKey("isLoggedIn")
        val CHECK_STATUS_ID = stringPreferencesKey("checkStatusId")

        // for epcUserModel only

        val EPC_AIRBASE = stringPreferencesKey("epc_airbase")
        val EPC_AIRBASE_ID = stringPreferencesKey("epc_airbase_id")
        val EPC_ALT_PH_NO = stringPreferencesKey("epc_alt_ph_no")
        val EPC_CONTACT_NO = stringPreferencesKey("epc_contact_no")
        val EPC_FLIGHT = stringPreferencesKey("epc_flight")
        val EPC_FLIGHT_ID = stringPreferencesKey("epc_flight_id")
        val EPC_IS_DELETED = booleanPreferencesKey("epc_is_deleted")
        val IS_ADMIN = booleanPreferencesKey("is_admin")
        val IS_SOD_INITIATE = booleanPreferencesKey("is_sod_initiate")
        val EPC_NRIC = stringPreferencesKey("epc_nric")
        val EPC_PASSWORD = stringPreferencesKey("epc_password")
        val EPC_REMARK = stringPreferencesKey("epc_remark")
        val EPC_TAG_ID = stringPreferencesKey("epc_tag_id")
        val EPC_UNIT = stringPreferencesKey("epc_unit")
        val EPC_UNIT_ID = stringPreferencesKey("epc_unit_id")
        val EPC_USER_ID = stringPreferencesKey("epc_user_id")
        val EPC_ROLE_ID = stringPreferencesKey("epc_role_id")
        val EPC_USER_NAME = stringPreferencesKey("epc_user_name")
        val EPC_USER_ROLE = stringPreferencesKey("epc_user_role")

    }

    suspend fun saveUser(user: LocalUser) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = user.name
            preferences[USER_ID] = user.userId
            preferences[ROLE_ID] = user.roleId
            preferences[USER_NRIC] = user.nric
            preferences[TOKEN] = user.token
            preferences[IS_LOGGED_IN] = true
            preferences[IS_ADMIN] = user.isAdmin
        }
    }

    suspend fun saveLoggedInStatus(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
        }
    }

    suspend fun updateIsSodInitiateStatus(isSodInitiate: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_SOD_INITIATE] = isSodInitiate
        }
    }

    suspend fun saveCheckStatusId(checkStatusId: String) {
        context.dataStore.edit {
            it[CHECK_STATUS_ID] = checkStatusId
        }
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[TOKEN] = token
        }
    }

    suspend fun saveEpcUser(userModel: UserModel) {
        context.dataStore.edit { preferences ->
            preferences[EPC_AIRBASE] = userModel.airbase
            preferences[EPC_AIRBASE_ID] = userModel.airbaseId
            preferences[EPC_ALT_PH_NO] = userModel.altPhNo
            preferences[EPC_CONTACT_NO] = userModel.contactNo
            preferences[EPC_FLIGHT] = userModel.flight
            preferences[EPC_FLIGHT_ID] = userModel.flightId
            preferences[EPC_IS_DELETED] = userModel.isDeleted
            preferences[EPC_NRIC] = userModel.nric
            preferences[IS_ADMIN] = userModel.isSysAdmin
            preferences[EPC_PASSWORD] = userModel.password
            preferences[EPC_REMARK] = userModel.remark
            preferences[EPC_TAG_ID] = userModel.tagId
            preferences[EPC_UNIT] = userModel.unit
            preferences[EPC_UNIT_ID] = userModel.unitId
            preferences[EPC_USER_ID] = userModel.userId
            preferences[EPC_ROLE_ID] = userModel.roleId
            preferences[EPC_USER_NAME] = userModel.userName
            preferences[EPC_USER_ROLE] = userModel.userRole
        }
    }

    val checkStatusId = context.dataStore.data.map { it[CHECK_STATUS_ID] }
    val isSodInitiate = context.dataStore.data.map { it[IS_SOD_INITIATE] }

    val getEpcUser: Flow<UserModel> = context.dataStore.data.map { preferences ->
        val airbase = preferences[EPC_AIRBASE] ?: "airbase"
        val airbaseId = preferences[EPC_AIRBASE_ID] ?: "airbaseId"
        val altPhNo = preferences[EPC_ALT_PH_NO] ?: "altPhNo"
        val contactNo = preferences[EPC_CONTACT_NO] ?: ""
        val flight = preferences[EPC_FLIGHT] ?: ""
        val flightId = preferences[EPC_FLIGHT_ID] ?: ""
        val isDeleted = preferences[EPC_IS_DELETED] ?: false
        val isAdmin = preferences[IS_ADMIN] ?: false
        val nric = preferences[EPC_NRIC] ?: ""
        val password = preferences[EPC_PASSWORD] ?: ""
        val remark = preferences[EPC_REMARK] ?: ""
        val tagId = preferences[EPC_TAG_ID] ?: ""
        val unit = preferences[EPC_UNIT] ?: ""
        val unitId = preferences[EPC_UNIT_ID] ?: ""
        val userId = preferences[EPC_USER_ID] ?: ""
        val roleId = preferences[EPC_ROLE_ID] ?: ""
        val userName = preferences[EPC_USER_NAME] ?: ""
        val userRole = preferences[EPC_USER_ROLE] ?: ""

        UserModel(
            airbase = airbase,
            airbaseId = airbaseId,
            altPhNo = altPhNo,
            contactNo = contactNo,
            flight = flight,
            flightId = flightId,
            isDeleted = isDeleted,
            nric = nric,
            password = password,
            remark = remark,
            tagId = tagId,
            unit = unit,
            isSysAdmin = isAdmin,
            unitId = unitId,
            userId = userId,
            roleId = roleId,
            userName = userName,
            userRole = userRole
        )
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
                preferences[IS_SOD_INITIATE] = false
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
        val isAdmin = preferences[IS_ADMIN] ?: false

        LocalUser(
            name = name,
            userId = id,
            nric = userNric,
            roleId = roleId,
            token = token,
            isLoggedIn = isLoggedIn,
            isAdmin = isAdmin,
        )
    }

}