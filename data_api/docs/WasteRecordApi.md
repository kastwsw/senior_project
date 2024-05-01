# WasteRecordApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV1WasteRecordListPost**](WasteRecordApi.md#apiV1WasteRecordListPost) | **POST** api/v1/waste_record/list | 
[**apiV1WasteRecordSavePost**](WasteRecordApi.md#apiV1WasteRecordSavePost) | **POST** api/v1/waste_record/save | 





### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(WasteRecordApi::class.java)
val wasteRecordListRequestDto : WasteRecordListRequestDto =  // WasteRecordListRequestDto | 

launch(Dispatchers.IO) {
    val result : WasteRecordResponseEntity = webService.apiV1WasteRecordListPost(wasteRecordListRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **wasteRecordListRequestDto** | [**WasteRecordListRequestDto**](WasteRecordListRequestDto.md)|  | [optional]

### Return type

[**WasteRecordResponseEntity**](WasteRecordResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json




### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(WasteRecordApi::class.java)
val wasteRecordSaveRequestDto : WasteRecordSaveRequestDto =  // WasteRecordSaveRequestDto | 

launch(Dispatchers.IO) {
    val result : WasteRecordResponseEntity = webService.apiV1WasteRecordSavePost(wasteRecordSaveRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **wasteRecordSaveRequestDto** | [**WasteRecordSaveRequestDto**](WasteRecordSaveRequestDto.md)|  | [optional]

### Return type

[**WasteRecordResponseEntity**](WasteRecordResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

