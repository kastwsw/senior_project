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

import io.r3chain.data.api.models.OrganizationUserDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param organizationUser 
 */


data class OrganizationUserSaveRequestDto (

    @Json(name = "organizationUser")
    val organizationUser: OrganizationUserDto

)

