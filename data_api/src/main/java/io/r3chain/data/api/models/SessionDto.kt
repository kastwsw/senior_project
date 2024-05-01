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


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param id 
 * @param token 
 * @param authID 
 * @param uiId 
 */


data class SessionDto (

    @Json(name = "id")
    val id: kotlin.Int,

    @Json(name = "token")
    val token: kotlin.String,

    @Json(name = "authID")
    val authID: kotlin.Int,

    @Json(name = "ui_id")
    val uiId: kotlin.String? = null

)

