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


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param key 
 * @param `value` 
 */


data class StringStringValuesKeyValuePair (

    @Json(name = "key")
    val key: kotlin.String? = null,

    @Json(name = "value")
    val `value`: kotlin.collections.List<kotlin.String>? = null

)

