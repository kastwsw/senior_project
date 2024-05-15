package io.r3chain.data.services

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesService @Inject constructor(
    @ApplicationContext context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = "user_preferences")

    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")

    /**
     * Flow для получения токена.
     */
    val authToken = context.dataStore.data.map {
        it[TOKEN_KEY]
    }

    /**
     * Flow для получения состояния опции "запомнить меня".
     */
    val rememberMe = context.dataStore.data.map {
        // по умолчанию значение true
        it[REMEMBER_ME_KEY] ?: true
    }

//    /**
//     * Очистка данных.
//     */
//    suspend fun clearAuthData() {
//        context.dataStore.edit { preferences ->
//            preferences.remove(TOKEN_KEY)
//            preferences.remove(REMEMBER_ME_KEY)
//        }
//    }

}