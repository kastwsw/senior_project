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
 * Values: MANUAL_PICKING_LINES,MECHANICAL_RECYCLING,CHEMICAL_RECYCLING,OTHER
 */

@JsonClass(generateAdapter = false)
enum class EnumFacilityProcessingMethod(val value: kotlin.String) {

    @Json(name = "manualPickingLines")
    MANUAL_PICKING_LINES("manualPickingLines"),

    @Json(name = "mechanicalRecycling")
    MECHANICAL_RECYCLING("mechanicalRecycling"),

    @Json(name = "chemicalRecycling")
    CHEMICAL_RECYCLING("chemicalRecycling"),

    @Json(name = "other")
    OTHER("other");

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
        fun encode(data: kotlin.Any?): kotlin.String? = if (data is EnumFacilityProcessingMethod) "$data" else null

        /**
         * Returns a valid [EnumFacilityProcessingMethod] for [data], null otherwise.
         */
        fun decode(data: kotlin.Any?): EnumFacilityProcessingMethod? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

