package io.r3chain.data.repositories

import dagger.Lazy
import io.r3chain.data.api.apis.AuthApi
import io.r3chain.data.api.infrastructure.ApiClient
import io.r3chain.data.api.models.AuthLoginRequestDto
import io.r3chain.data.vo.UserVO
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiClient: Lazy<ApiClient>
) {

    suspend fun getUser(email: String, password: String): UserVO? {
        // TODO: обработка ошибок и всё такое
        val response = apiClient.get()
            .createService(AuthApi::class.java)
            .apiV1AuthLoginPost(
                AuthLoginRequestDto(email = email, password = password)
            )
        // TODO: кэш в БД
//        val token = response.body()?.sessionList?.values?.firstOrNull()?.token ?: ""
        return response.body()?.authList?.values?.firstOrNull()?.let {
            UserVO().createByApi(it)
        }
    }
}