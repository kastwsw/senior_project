package io.r3chain.data

import io.r3chain.DiContainer
import io.r3chain.data.api.apis.AuthApi
import io.r3chain.data.api.models.AuthLoginRequestDto
import io.r3chain.domain.vo.UserVO

class UserRepository {

    suspend fun getUser(email: String, password: String): UserVO? {
        // TODO: обработка ошибок и всё такое
        val response = DiContainer.apiClientGateway
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