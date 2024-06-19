package io.r3chain.core.api.apis

import io.r3chain.core.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.r3chain.core.api.models.OrganizationUserResponseEntity
import io.r3chain.core.api.models.OrganizationUserSaveRequestDto

interface OrganizationUserApi {
    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param body  (optional)
     * @return [OrganizationUserResponseEntity]
     */
    @POST("api/v1/user/list")
    suspend fun apiV1UserListPost(@Body body: kotlin.Any? = null): Response<OrganizationUserResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param organizationUserSaveRequestDto  (optional)
     * @return [OrganizationUserResponseEntity]
     */
    @POST("api/v1/user/save")
    suspend fun apiV1UserSavePost(@Body organizationUserSaveRequestDto: OrganizationUserSaveRequestDto? = null): Response<OrganizationUserResponseEntity>

}
