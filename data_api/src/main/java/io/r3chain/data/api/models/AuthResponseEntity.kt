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

import io.r3chain.data.api.models.ApiStatusCode
import io.r3chain.data.api.models.AuthDto
import io.r3chain.data.api.models.ErrorEntity
import io.r3chain.data.api.models.OrganizationDto
import io.r3chain.data.api.models.OrganizationUserDto
import io.r3chain.data.api.models.ResourceDto
import io.r3chain.data.api.models.SessionDto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param statusCode 
 * @param message 
 * @param errorList 
 * @param responseResourceList 
 * @param sessionList 
 * @param organizationList 
 * @param organizationUserList 
 * @param authList 
 */


data class AuthResponseEntity (

    @Json(name = "statusCode")
    val statusCode: ApiStatusCode? = null,

    @Json(name = "message")
    val message: kotlin.String? = null,

    @Json(name = "errorList")
    val errorList: kotlin.collections.List<ErrorEntity>? = null,

    @Json(name = "responseResourceList")
    val responseResourceList: kotlin.collections.Map<kotlin.String, ResourceDto>? = null,

    @Json(name = "sessionList")
    val sessionList: kotlin.collections.Map<kotlin.String, SessionDto>? = null,

    @Json(name = "organizationList")
    val organizationList: kotlin.collections.Map<kotlin.String, OrganizationDto>? = null,

    @Json(name = "organizationUserList")
    val organizationUserList: kotlin.collections.Map<kotlin.String, OrganizationUserDto>? = null,

    @Json(name = "authList")
    val authList: kotlin.collections.Map<kotlin.String, AuthDto>? = null

)

