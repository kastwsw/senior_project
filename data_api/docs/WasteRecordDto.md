
# WasteRecordDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **kotlin.Int** |  | 
**state** | [**EnumWasteRecordState**](EnumWasteRecordState.md) |  | 
**status** | [**EnumWasteRecordStatus**](EnumWasteRecordStatus.md) |  | 
**stage** | [**EnumWasteRecordStage**](EnumWasteRecordStage.md) |  | 
**type** | [**EnumWasteRecordType**](EnumWasteRecordType.md) |  | 
**linkMode** | [**EnumWasteRecordLinkMode**](EnumWasteRecordLinkMode.md) |  | 
**isActiveRecord** | **kotlin.Boolean** |  | 
**partnerIDList** | **kotlin.collections.List&lt;kotlin.Int&gt;** |  | 
**wasteRecordTransitionFromIDList** | **kotlin.collections.List&lt;kotlin.Int&gt;** |  | 
**wasteRecordTransitionToIDList** | **kotlin.collections.List&lt;kotlin.Int&gt;** |  | 
**wasteRecordVerificationList** | [**kotlin.collections.List&lt;WasteRecordVerificationDto&gt;**](WasteRecordVerificationDto.md) |  | 
**customTags** | [**kotlin.collections.List&lt;KeyValuePairDto&gt;**](KeyValuePairDto.md) |  | 
**plasticWeight** | [**PlasticWeightDto**](PlasticWeightDto.md) |  | 
**uiId** | **kotlin.String** |  |  [optional]
**resourceIDList** | **kotlin.collections.List&lt;kotlin.Int&gt;** |  |  [optional]
**locationID** | **kotlin.Int** |  |  [optional]
**at** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional]
**processingMethod** | **kotlin.String** |  |  [optional]
**processingMaterialForm** | **kotlin.String** |  |  [optional]
**processingColor** | **kotlin.String** |  |  [optional]



