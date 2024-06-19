package io.r3chain.core.api.apis

import io.r3chain.core.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.r3chain.core.api.models.FacilityListRequestDto
import io.r3chain.core.api.models.FacilityResponseEntity
import io.r3chain.core.api.models.FacilitySaveRequestDto

interface FacilityApi {
    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param facilityListRequestDto  (optional)
     * @return [FacilityResponseEntity]
     */
    @POST("api/v1/facility/list")
    suspend fun apiV1FacilityListPost(@Body facilityListRequestDto: FacilityListRequestDto? = null): Response<FacilityResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param facilitySaveRequestDto  (optional)
     * @return [FacilityResponseEntity]
     */
    @POST("api/v1/facility/save")
    suspend fun apiV1FacilitySavePost(@Body facilitySaveRequestDto: FacilitySaveRequestDto? = null): Response<FacilityResponseEntity>

}
