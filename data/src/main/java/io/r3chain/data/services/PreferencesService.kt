package io.r3chain.data.services

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = "user_preferences")

    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")


    /**
     * Flow for token data.
     */
    val authToken = context.dataStore.data.map {
        it[TOKEN_KEY]
    }

    /**
     * Save token value.
     */
    suspend fun saveAuthToken(value: String) {
        context.dataStore.edit {
            it[TOKEN_KEY] = value
        }
    }


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
     * Clear all data.
     */
    suspend fun clear() {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(REMEMBER_ME_KEY)
        }
    }

}