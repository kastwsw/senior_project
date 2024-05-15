package io.r3chain.data.repositories

import dagger.Lazy
import io.r3chain.data.api.apis.AuthApi
import io.r3chain.data.api.infrastructure.ApiClient
import io.r3chain.data.api.models.AuthLoginRequestDto
import io.r3chain.data.db.CacheDatabase
import io.r3chain.data.services.NetworkService
import io.r3chain.data.services.UserPrefsService
import io.r3chain.data.vo.UserVO
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val networkService: NetworkService,
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
        networkService.safeApiCall {
            apiClient.get()
                .createService(AuthApi::class.java)
                .apiV1AuthLoginPost(
                    AuthLoginRequestDto(email = email, password = password)
                )
        }.onSuccess { response ->
            // save user data
            response.body()?.authList?.values?.firstOrNull()?.let {
                cacheDatabase.get().userDao().insert(it)
            }
            // save auth token
            userPrefsService.get().saveAuthToken(
                response.body()?.sessionList?.values?.firstOrNull()?.token ?: ""
            )
        }.onFailure {
            if (it !is IOException) throw it
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
     * Close authorization.
     */
    suspend fun exit() {
        userPrefsService.get().saveAuthToken("")
        cacheDatabase.get().userDao().deleteAll()
    }

}