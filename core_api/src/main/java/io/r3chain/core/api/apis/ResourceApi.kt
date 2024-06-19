package io.r3chain.core.api.apis

import io.r3chain.core.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.r3chain.core.api.models.ResourceListRequestDto
import io.r3chain.core.api.models.ResourceResponseEntity
import io.r3chain.core.api.models.ResourceSaveRequestDto
import io.r3chain.core.api.models.ResourceUploadRequestDto
import io.r3chain.core.api.models.StringStringValuesKeyValuePair

import io.r3chain.core.api.models.*
import okhttp3.MultipartBody

interface ResourceApi {
    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param resourceListRequestDto  (optional)
     * @return [ResourceResponseEntity]
     */
    @POST("api/v1/resource/list")
    suspend fun apiV1ResourceListPost(@Body resourceListRequestDto: ResourceListRequestDto? = null): Response<ResourceResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param resourceSaveRequestDto  (optional)
     * @return [ResourceResponseEntity]
     */
    @POST("api/v1/resource/save")
    suspend fun apiV1ResourceSavePost(@Body resourceSaveRequestDto: ResourceSaveRequestDto? = null): Response<ResourceResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param resourceList  (optional)
     * @param fileList  (optional)
     * @return [ResourceResponseEntity]
     */
    @Multipart
    @POST("api/v1/resource/upload")
    suspend fun apiV1ResourceUploadPost(
        @Query("resourceList") resourceList: kotlin.collections.List<ResourceUploadDto>? = null,
        @Part("fileList") fileList: kotlin.collections.List<StringStringValuesKeyValuePair>? = null
    ): Response<ResourceResponseEntity>


    @Multipart
    @POST("api/v1/resource/upload")
    suspend fun apiV1MediaUploadPost(
        @Part file: MultipartBody.Part,
        @Part("requestDto") requestDto: RequestBody
    ): Response<ResourceResponseEntity>

}
