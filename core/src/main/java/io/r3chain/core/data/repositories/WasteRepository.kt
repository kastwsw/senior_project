package io.r3chain.core.data.repositories

import io.r3chain.core.api.apis.WasteRecordApi
import io.r3chain.core.api.infrastructure.ApiClient
import io.r3chain.core.api.models.EnumPlasticType
import io.r3chain.core.api.models.EnumWasteRecordDispatchMode
import io.r3chain.core.api.models.EnumWasteRecordLinkMode
import io.r3chain.core.api.models.EnumWasteRecordStage
import io.r3chain.core.api.models.EnumWasteRecordState
import io.r3chain.core.api.models.EnumWasteRecordStatus
import io.r3chain.core.api.models.EnumWasteRecordType
import io.r3chain.core.api.models.EnumWasteVerificationType
import io.r3chain.core.api.models.PlasticWeightDto
import io.r3chain.core.api.models.WasteRecordDto
import io.r3chain.core.api.models.WasteRecordListRequestDto
import io.r3chain.core.api.models.WasteRecordSaveRequestDto
import io.r3chain.core.api.models.WasteRecordVerificationDto
import io.r3chain.core.data.services.ApiService
import javax.inject.Inject

class WasteRepository @Inject constructor(
    private val apiService: ApiService,
    private val apiClient: ApiClient
) {

    /**
     * Получает все записи пользователя.
     */
    suspend fun getRecords() {
        apiService.safeApiCall {
            apiClient
                .createService(io.r3chain.core.api.apis.WasteRecordApi::class.java)
                .apiV1WasteRecordListPost(
                    WasteRecordListRequestDto(
                        partnerTypeList = emptyList(),
                        partnerIDList = emptyList(),
                        wasteRecordIDList = emptyList()
                    )
                )
        }.onSuccess {
            it.wasteRecordList
        }
    }

    /**
     * Добавляет запись о факте сбора.
     */
    suspend fun addCollect() {
        apiService.safeApiCall {
            apiClient
                .createService(io.r3chain.core.api.apis.WasteRecordApi::class.java)
                .apiV1WasteRecordSavePost(
                    WasteRecordSaveRequestDto(
                        wasteRecord = WasteRecordDto(
                            id = 0,
                            state = EnumWasteRecordState.ACTIVE,
                            status = EnumWasteRecordStatus.DRAFT,
                            stage = EnumWasteRecordStage.COLLECTION,
                            type = EnumWasteRecordType.OUTBOUND,
                            linkMode = EnumWasteRecordLinkMode.AUTO,
                            isActiveRecord = true,
                            partnerIDList = emptyList(),
                            wasteRecordTransitionFromIDList = emptyList(),
                            wasteRecordTransitionToIDList = emptyList(),
                            wasteRecordVerificationList = listOf(
                                WasteRecordVerificationDto(
                                    id = 0,
                                    verificationType = EnumWasteVerificationType.PHOTO,
                                    plasticWeight = PlasticWeightDto(
                                        plasticTypeList = listOf(EnumPlasticType.PET),
                                        netWeight = 10.0
                                    ),
                                    commonResourceIDList = listOf(120)
                                )
                            ),
                            customTags = emptyList(),
                            plasticWeight = PlasticWeightDto(
                                plasticTypeList = listOf(EnumPlasticType.PET),
                                netWeight = 10.0
                            )
                        ),
                        parentWasteRecordIDList = emptyList(),
                        dispatchMode = EnumWasteRecordDispatchMode.AUTO
                    )
                )
        }.onSuccess {
            it.wasteRecordList
        }
    }

}