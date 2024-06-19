package io.r3chain.core.api.apis

import io.r3chain.core.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.r3chain.core.api.models.OrganizationResponseEntity
import io.r3chain.core.api.models.OrganizationSaveRequestDto

interface OrganizationApi {
    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @return [OrganizationResponseEntity]
     */
    @POST("api/v1/organization/list")
    suspend fun apiV1OrganizationListPost(): Response<OrganizationResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param organizationSaveRequestDto  (optional)
     * @return [OrganizationResponseEntity]
     */
    @POST("api/v1/organization/save")
    suspend fun apiV1OrganizationSavePost(@Body organizationSaveRequestDto: OrganizationSaveRequestDto? = null): Response<OrganizationResponseEntity>

}
