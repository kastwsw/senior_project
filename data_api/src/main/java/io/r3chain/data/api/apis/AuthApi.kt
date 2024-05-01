package io.r3chain.data.api.apis

import io.r3chain.data.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.r3chain.data.api.models.AuthLoginRequestDto
import io.r3chain.data.api.models.AuthResponseEntity
import io.r3chain.data.api.models.AuthSaveRequestDto
import io.r3chain.data.api.models.GeneralResponseEntity

interface AuthApi {
    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param authLoginRequestDto  (optional)
     * @return [AuthResponseEntity]
     */
    @POST("api/v1/auth/login")
    suspend fun apiV1AuthLoginPost(@Body authLoginRequestDto: AuthLoginRequestDto? = null): Response<AuthResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @return [GeneralResponseEntity]
     */
    @POST("api/v1/auth/logout")
    suspend fun apiV1AuthLogoutPost(): Response<GeneralResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param authSaveRequestDto  (optional)
     * @return [AuthResponseEntity]
     */
    @POST("api/v1/auth/save")
    suspend fun apiV1AuthSavePost(@Body authSaveRequestDto: AuthSaveRequestDto? = null): Response<AuthResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @return [GeneralResponseEntity]
     */
    @POST("api/v1/auth/verify")
    suspend fun apiV1AuthVerifyPost(): Response<GeneralResponseEntity>

}
