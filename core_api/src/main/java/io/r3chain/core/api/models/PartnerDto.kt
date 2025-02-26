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

import io.r3chain.core.api.models.EnumPartnerInboundSource
import io.r3chain.core.api.models.EnumPartnerInboundType
import io.r3chain.core.api.models.EnumPartnerOutboundMethod
import io.r3chain.core.api.models.EnumPartnerOutboundType
import io.r3chain.core.api.models.EnumPartnerState
import io.r3chain.core.api.models.EnumPartnerType
import io.r3chain.core.api.models.EnumPlasticType
import io.r3chain.core.api.models.KeyValuePairDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param id 
 * @param name 
 * @param type 
 * @param state 
 * @param locationID 
 * @param contact 
 * @param customTags 
 * @param resourceIDList 
 * @param uiId 
 * @param organizationOwnerID 
 * @param inboundType 
 * @param inboundSource 
 * @param inboundWasteTypeList 
 * @param inboundIsSorting 
 * @param outboundType 
 * @param outboundMethod 
 * @param outboundMaterialList 
 */


data class PartnerDto (

    @Json(name = "id")
    val id: kotlin.Int,

    @Json(name = "name")
    val name: kotlin.String,

    @Json(name = "type")
    val type: EnumPartnerType,

    @Json(name = "state")
    val state: EnumPartnerState,

    @Json(name = "locationID")
    val locationID: kotlin.Int,

    @Json(name = "contact")
    val contact: kotlin.String,

    @Json(name = "customTags")
    val customTags: kotlin.collections.List<KeyValuePairDto>,

    @Json(name = "resourceIDList")
    val resourceIDList: kotlin.collections.List<kotlin.Int>,

    @Json(name = "ui_id")
    val uiId: kotlin.String? = null,

    @Json(name = "organizationOwnerID")
    val organizationOwnerID: kotlin.Int? = null,

    @Json(name = "inboundType")
    val inboundType: EnumPartnerInboundType? = null,

    @Json(name = "inboundSource")
    val inboundSource: EnumPartnerInboundSource? = null,

    @Json(name = "inboundWasteTypeList")
    val inboundWasteTypeList: kotlin.collections.List<EnumPlasticType>? = null,

    @Json(name = "inboundIsSorting")
    val inboundIsSorting: kotlin.Boolean? = null,

    @Json(name = "outboundType")
    val outboundType: EnumPartnerOutboundType? = null,

    @Json(name = "outboundMethod")
    val outboundMethod: EnumPartnerOutboundMethod? = null,

    @Json(name = "outboundMaterialList")
    val outboundMaterialList: kotlin.collections.List<EnumPlasticType>? = null

)

