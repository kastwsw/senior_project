package io.r3chain.data.api.apis

import io.r3chain.data.api.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import io.r3chain.data.api.models.WasteRecordListRequestDto
import io.r3chain.data.api.models.WasteRecordResponseEntity
import io.r3chain.data.api.models.WasteRecordSaveRequestDto

interface WasteRecordApi {
    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param wasteRecordListRequestDto  (optional)
     * @return [WasteRecordResponseEntity]
     */
    @POST("api/v1/waste_record/list")
    suspend fun apiV1WasteRecordListPost(@Body wasteRecordListRequestDto: WasteRecordListRequestDto? = null): Response<WasteRecordResponseEntity>

    /**
     * 
     * 
     * Responses:
     *  - 200: Success
     *
     * @param wasteRecordSaveRequestDto  (optional)
     * @return [WasteRecordResponseEntity]
     */
    @POST("api/v1/waste_record/save")
    suspend fun apiV1WasteRecordSavePost(@Body wasteRecordSaveRequestDto: WasteRecordSaveRequestDto? = null): Response<WasteRecordResponseEntity>

}
