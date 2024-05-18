package io.r3chain.data.services

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import io.r3chain.data.api.infrastructure.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPrefsService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiClient: Lazy<ApiClient>
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
        withContext(Dispatchers.IO) {
            context.dataStore.edit {
                it[REMEMBER_ME_KEY] = value
            }
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
    val authToken = rememberMe
        .flatMapLatest { enabled ->
            if (enabled) {
                // take token from the DataStore
                context.dataStore.data.map {
                    it[TOKEN_KEY] ?: ""
                }
            } else {
                // take token from the app memory
                authTokenInMemory
            }
        }
        .distinctUntilChanged()
        .onEach {
            setTokenToApiClient(it)
        }

    /**
     * Save token value.
     */
    suspend fun saveAuthToken(value: String) {
        withContext(Dispatchers.IO) {
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
    }

    /**
     * Set token to API client object.
     */
    private suspend fun setTokenToApiClient(token: String) {
        withContext(Dispatchers.IO) {
            Log.d("Token flow", "Set token [$token]")
            apiClient.get().setBearerToken(token)
        }
    }

    /**
     * Clear all data.
     */
    suspend fun clear() {
        withContext(Dispatchers.IO) {
            context.dataStore.edit {
                it.remove(TOKEN_KEY)
                it.remove(REMEMBER_ME_KEY)
            }
        }
    }

}