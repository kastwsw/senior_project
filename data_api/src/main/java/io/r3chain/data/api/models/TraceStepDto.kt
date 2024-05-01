/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package io.r3chain.data.api.models

import io.r3chain.data.api.models.EnumPartnerType
import io.r3chain.data.api.models.PlasticWeightDto
import io.r3chain.data.api.models.TraceWasteTypeRecordDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param stepType 
 * @param traceTypeWasteRecordListDto 
 * @param weightDto 
 */


data class TraceStepDto (

    @Json(name = "stepType")
    val stepType: EnumPartnerType? = null,

    @Json(name = "traceTypeWasteRecordListDto")
    val traceTypeWasteRecordListDto: kotlin.collections.List<TraceWasteTypeRecordDto>? = null,

    @Json(name = "weightDto")
    val weightDto: PlasticWeightDto? = null

)

