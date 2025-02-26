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

package io.r3chain.core.api.models

import io.r3chain.core.api.models.EnumWasteRecordTransitionType

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param id 
 * @param type 
 * @param wasteRecordTransitionFromID 
 * @param wasteRecordTransitionTransitionToID 
 * @param uiId 
 * @param resourceIDList 
 */


data class WasteRecordTransitionDto (

    @Json(name = "id")
    val id: kotlin.Int,

    @Json(name = "type")
    val type: EnumWasteRecordTransitionType,

    @Json(name = "wasteRecordTransitionFromID")
    val wasteRecordTransitionFromID: kotlin.Int,

    @Json(name = "wasteRecordTransitionTransitionToID")
    val wasteRecordTransitionTransitionToID: kotlin.Int,

    @Json(name = "ui_id")
    val uiId: kotlin.String? = null,

    @Json(name = "resourceIDList")
    val resourceIDList: kotlin.collections.List<kotlin.Int>? = null

)

