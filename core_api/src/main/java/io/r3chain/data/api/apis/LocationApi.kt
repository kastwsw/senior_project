package io.r3chain.data.api.apis

import io.r3chain.data.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.r3chain.data.api.models.LocationListRequestDto
import io.r3chain.data.api.models.LocationResponseEntity
import io.r3chain.data.api.models.LocationSaveRequestDto

interface LocationApi {
    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param locationListRequestDto  (optional)
     * @return [LocationResponseEntity]
     */
    @POST("api/v1/location/list")
    suspend fun apiV1LocationListPost(@Body locationListRequestDto: LocationListRequestDto? = null): Response<LocationResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param locationSaveRequestDto  (optional)
     * @return [LocationResponseEntity]
     */
    @POST("api/v1/location/save")
    suspend fun apiV1LocationSavePost(@Body locationSaveRequestDto: LocationSaveRequestDto? = null): Response<LocationResponseEntity>

}
