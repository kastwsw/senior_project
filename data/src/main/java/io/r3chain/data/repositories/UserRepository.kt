package io.r3chain.data.repositories

import dagger.Lazy
import io.r3chain.data.api.apis.AuthApi
import io.r3chain.data.api.infrastructure.ApiClient
import io.r3chain.data.api.models.AuthLoginRequestDto
import io.r3chain.data.db.CacheDatabase
import io.r3chain.data.vo.UserVO
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val cacheDatabase: Lazy<CacheDatabase>,
    private val apiClient: Lazy<ApiClient>
) {

    fun getUserFlow() = cacheDatabase.get().userDao().getAll().map { list ->
        list.firstOrNull()?.let {
            UserVO().createByApi(it)
        }
    }

    suspend fun fetchUser(email: String, password: String) {
        // TODO: обработка ошибок и всё такое
        val response = apiClient.get()
            .createService(AuthApi::class.java)
            .apiV1AuthLoginPost(
                AuthLoginRequestDto(email = email, password = password)
            )
//        val token = response.body()?.sessionList?.values?.firstOrNull()?.token ?: ""
        response.body()?.authList?.values?.firstOrNull()?.let {
            cacheDatabase.get().userDao().insert(it)
        }
    }
}