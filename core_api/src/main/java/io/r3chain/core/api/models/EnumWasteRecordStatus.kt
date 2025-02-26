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
 * Values: EMPTY,IN_PROCESS,NEED_TO_LINK,READY_TO_SHIP,DRAFT,SHIPPED
 */

@JsonClass(generateAdapter = false)
enum class EnumWasteRecordStatus(val value: kotlin.String) {

    @Json(name = "empty")
    EMPTY("empty"),

    @Json(name = "inProcess")
    IN_PROCESS("inProcess"),

    @Json(name = "needToLink")
    NEED_TO_LINK("needToLink"),

    @Json(name = "readyToShip")
    READY_TO_SHIP("readyToShip"),

    @Json(name = "draft")
    DRAFT("draft"),

    @Json(name = "shipped")
    SHIPPED("shipped");

    /**
     * Override [toString()] to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): kotlin.String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: kotlin.Any?): kotlin.String? = if (data is EnumWasteRecordStatus) "$data" else null

        /**
         * Returns a valid [EnumWasteRecordStatus] for [data], null otherwise.
         */
        fun decode(data: kotlin.Any?): EnumWasteRecordStatus? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

