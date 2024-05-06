package io.r3chain.data.api.apis

import io.r3chain.data.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.r3chain.data.api.models.TraceListRequestDto
import io.r3chain.data.api.models.TraceResponseEntity

interface TraceApi {
    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param traceListRequestDto  (optional)
     * @return [TraceResponseEntity]
     */
    @POST("api/v1/trace/list")
    suspend fun apiV1TraceListPost(@Body traceListRequestDto: TraceListRequestDto? = null): Response<TraceResponseEntity>

}
