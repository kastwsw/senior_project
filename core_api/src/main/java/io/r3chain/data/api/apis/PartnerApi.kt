package io.r3chain.data.api.apis

import io.r3chain.data.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.r3chain.data.api.models.PartnerListRequestDto
import io.r3chain.data.api.models.PartnerResponseEntity
import io.r3chain.data.api.models.PartnerSaveRequestDto

interface PartnerApi {
    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param partnerListRequestDto  (optional)
     * @return [PartnerResponseEntity]
     */
    @POST("api/v1/partner/list")
    suspend fun apiV1PartnerListPost(@Body partnerListRequestDto: PartnerListRequestDto? = null): Response<PartnerResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param partnerSaveRequestDto  (optional)
     * @return [PartnerResponseEntity]
     */
    @POST("api/v1/partner/save")
    suspend fun apiV1PartnerSavePost(@Body partnerSaveRequestDto: PartnerSaveRequestDto? = null): Response<PartnerResponseEntity>

}
