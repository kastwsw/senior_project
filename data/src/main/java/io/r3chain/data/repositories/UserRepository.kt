package io.r3chain.data.repositories

import dagger.Lazy
import io.r3chain.data.api.apis.AuthApi
import io.r3chain.data.api.infrastructure.ApiClient
import io.r3chain.data.api.models.AuthLoginRequestDto
import io.r3chain.data.db.CacheDatabase
import io.r3chain.data.services.NetworkService
import io.r3chain.data.vo.UserVO
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val networkService: NetworkService,
    private val apiClient: Lazy<ApiClient>,
    private val cacheDatabase: Lazy<CacheDatabase>
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
            // TODO: запомнить токен (как долго?)
//        val token = response.body()?.sessionList?.values?.firstOrNull()?.token ?: ""
            response.body()?.authList?.values?.firstOrNull()?.let {
                cacheDatabase.get().userDao().insert(it)
            }
        }.onFailure {
            if (it !is IOException) throw it
        }
    }

    /**
     * Закрыть авторизацию.
     */
    suspend fun exit() {
        cacheDatabase.get().userDao().deleteAll()
    }

}