package io.r3chain.data.repositories

import dagger.Lazy
import io.r3chain.data.api.apis.AuthApi
import io.r3chain.data.api.infrastructure.ApiClient
import io.r3chain.data.api.models.AuthLoginRequestDto
import io.r3chain.data.db.CacheDatabase
import io.r3chain.data.services.ApiService
import io.r3chain.data.services.UserPrefsService
import io.r3chain.data.vo.UserVO
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val apiClient: Lazy<ApiClient>,
    private val cacheDatabase: Lazy<CacheDatabase>,
    private val userPrefsService: Lazy<UserPrefsService>
) {

    /**
     * Flow данных авторизованного пользователя.
     */
    fun getUserFlow() = cacheDatabase.get().userDao().getAll().map { list ->
        list.firstOrNull()?.let {
            UserVO().createByApi(it)
        }
    }

    /**
     * Авторизовать пользователя по соответствующим данным.
     *
     * @param email Адрес электронной почты.
     * @param password Пароль.
     */
    suspend fun authUser(email: String, password: String) {
        apiService.safeApiCall {
            apiClient.get()
                .createService(AuthApi::class.java)
                .apiV1AuthLoginPost(
                    AuthLoginRequestDto(email = email, password = password)
                )
        }.onSuccess { response ->
            // save user data
            response.authList?.values?.firstOrNull()?.let {
                cacheDatabase.get().userDao().insert(it)
            }
            // save auth token
            userPrefsService.get().saveAuthToken(
                response.sessionList?.values?.firstOrNull()?.token ?: ""
            )
        }
    }


    /**
     * Data for "remember me" option.
     *
     * @return True - user auth restores after app restart.
     */
    fun getRememberMeFlow() = userPrefsService.get().rememberMe

    suspend fun saveRememberMe(value: Boolean) {
        userPrefsService.get().saveRememberMe(value)
    }

    /**
     * Data for "remember me" option.
     *
     * @return Null - no authorization.
     */
    fun getAuthTokenFlow() = userPrefsService.get().authToken

    /**
     * Exit from the app.
     */
    suspend fun exit() {
        apiService.safeApiCall {
            apiClient.get()
                .createService(AuthApi::class.java)
                .apiV1AuthLogoutPost()
        }
        userPrefsService.get().saveAuthToken("")
        cacheDatabase.get().userDao().deleteAll()
    }

    /**
     * Refresh user data.
     */
    suspend fun refresh() {
        apiService.safeApiCall {
            apiClient.get()
                .createService(AuthApi::class.java)
                .apiV1AuthVerifyPost()
        }
    }

}