
# FacilityDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **kotlin.Int** |  | 
**name** | **kotlin.String** |  | 
**type** | [**EnumFacilityType**](EnumFacilityType.md) |  | 
**state** | [**EnumFacilityState**](EnumFacilityState.md) |  | 
**locationID** | **kotlin.Int** |  | 
**customTags** | [**kotlin.collections.List&lt;KeyValuePairDto&gt;**](KeyValuePairDto.md) |  | 
**resourceIDList** | **kotlin.collections.List&lt;kotlin.Int&gt;** |  | 
**inboundWasteTypeList** | [**kotlin.collections.List&lt;EnumPlasticType&gt;**](EnumPlasticType.md) |  | 
**outboundWasteTypeList** | [**kotlin.collections.List&lt;EnumPlasticType&gt;**](EnumPlasticType.md) |  | 
**capacity** | **kotlin.Double** |  | 
**capacityUnit** | [**EnumCapacityUnit**](EnumCapacityUnit.md) |  | 
**uiId** | **kotlin.String** |  |  [optional]
**certificateValidityAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional]
**certificateResourceID** | **kotlin.Int** |  |  [optional]
**collectionType** | [**EnumCollectionType**](EnumCollectionType.md) |  |  [optional]
**collectionSource** | **kotlin.String** |  |  [optional]
**processingType** | [**EnumFacilityProcessingType**](EnumFacilityProcessingType.md) |  |  [optional]
**processingMethod** | [**EnumFacilityProcessingMethod**](EnumFacilityProcessingMethod.md) |  |  [optional]
**processingForm** | [**kotlin.collections.List&lt;EnumPlasticType&gt;**](EnumPlasticType.md) |  |  [optional]
**deliveryToFacilityID** | **kotlin.Int** |  |  [optional]
**deliveryToPartnerID** | **kotlin.Int** |  |  [optional]



