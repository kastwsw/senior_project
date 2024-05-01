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

import io.r3chain.data.api.models.EnumUserOrganizationState

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param id 
 * @param firstName 
 * @param lastName 
 * @param email 
 * @param phone 
 * @param organizationID 
 * @param title 
 * @param imageResourceID 
 * @param state 
 * @param uiId 
 */


data class OrganizationUserDto (

    @Json(name = "id")
    val id: kotlin.Int,

    @Json(name = "firstName")
    val firstName: kotlin.String,

    @Json(name = "lastName")
    val lastName: kotlin.String,

    @Json(name = "email")
    val email: kotlin.String,

    @Json(name = "phone")
    val phone: kotlin.String,

    @Json(name = "organizationID")
    val organizationID: kotlin.Int,

    @Json(name = "title")
    val title: kotlin.String,

    @Json(name = "imageResourceID")
    val imageResourceID: kotlin.Int,

    @Json(name = "state")
    val state: EnumUserOrganizationState,

    @Json(name = "ui_id")
    val uiId: kotlin.String? = null

)

