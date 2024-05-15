package io.r3chain.data.services

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPrefsService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = "user_preferences")

    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")


    /**
     * Flow for "remember me" option.
     */
    val rememberMe = context.dataStore.data.map {
        // по умолчанию значение true
        it[REMEMBER_ME_KEY] ?: true
    }

    /**
     * Save remember me value.
     */
    suspend fun saveRememberMe(value: Boolean) {
        context.dataStore.edit {
            it[REMEMBER_ME_KEY] = value
        }
    }

    /**
     * Token in memory, when don't need to remember user.
     */
    private val authTokenInMemory = MutableStateFlow("")

    /**
     * Flow for token data.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val authToken = rememberMe.flatMapLatest {
        if (it) {
            // take token from the DataStore
            context.dataStore.data.map {
                it[TOKEN_KEY] ?: ""
            }
        } else {
            // take token from the app memory
            authTokenInMemory
        }
    }

    /**
     * Save token value.
     */
    suspend fun saveAuthToken(value: String) {
        if (rememberMe.first()) {
            // save token by the DataStore
            context.dataStore.edit {
                it[TOKEN_KEY] = value
            }
            // clear token in memory
            authTokenInMemory.emit("")
        } else {
            // save token to the app memory
            authTokenInMemory.emit(value)
            // clear token in the DataStore
            context.dataStore.edit {
                it.remove(TOKEN_KEY)
            }
        }
    }


    /**
     * Clear all data.
     */
    suspend fun clear() {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(REMEMBER_ME_KEY)
        }
    }

}