package io.r3chain.feature.inventory.ui

import android.net.Uri
import io.r3chain.core.data.vo.FileAttachEntity
import io.r3chain.core.data.vo.WasteDocEntity
import io.r3chain.core.data.vo.WasteDocType
import io.r3chain.core.data.vo.WasteEntity
import io.r3chain.core.data.vo.WasteRecordType
import io.r3chain.core.data.vo.WasteType

val dummyWasteRecord by lazy {
    WasteEntity(
        recordType = WasteRecordType.COLLECT,
        geoLatLong = 0.0 to 0.0,
        time = 0,
        grams = 1002,
        materialTypes = listOf(WasteType.HDPE, WasteType.PVC, WasteType.PP),
        documents = listOf(
            WasteDocEntity(type = WasteDocType.SLIP),
            WasteDocEntity(type = WasteDocType.INVOICE),
            WasteDocEntity(type = WasteDocType.CERT)
        )
    )
}

val dummyWasteDoc by lazy {
    WasteDocEntity(
        type = WasteDocType.SLIP,
        files = listOf(
            FileAttachEntity(uri = Uri.EMPTY, isLoading = true),
            FileAttachEntity(uri = Uri.EMPTY, isLoading = true)
        )
    )
}