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
 * Values: PHOTO,INVOICE,VALIDATION_CERTIFICATE,WEIGHT_SLIP
 */

@JsonClass(generateAdapter = false)
enum class EnumWasteVerificationType(val value: kotlin.String) {

    @Json(name = "photo")
    PHOTO("photo"),

    @Json(name = "invoice")
    INVOICE("invoice"),

    @Json(name = "validationCertificate")
    VALIDATION_CERTIFICATE("validationCertificate"),

    @Json(name = "weightSlip")
    WEIGHT_SLIP("weightSlip");

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
        fun encode(data: kotlin.Any?): kotlin.String? = if (data is EnumWasteVerificationType) "$data" else null

        /**
         * Returns a valid [EnumWasteVerificationType] for [data], null otherwise.
         */
        fun decode(data: kotlin.Any?): EnumWasteVerificationType? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

