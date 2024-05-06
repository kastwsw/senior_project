# ResourceApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV1ResourceListPost**](ResourceApi.md#apiV1ResourceListPost) | **POST** api/v1/resource/list | 
[**apiV1ResourceSavePost**](ResourceApi.md#apiV1ResourceSavePost) | **POST** api/v1/resource/save | 
[**apiV1ResourceUploadPost**](ResourceApi.md#apiV1ResourceUploadPost) | **POST** api/v1/resource/upload | 





### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ResourceApi::class.java)
val resourceListRequestDto : ResourceListRequestDto =  // ResourceListRequestDto | 

launch(Dispatchers.IO) {
    val result : ResourceResponseEntity = webService.apiV1ResourceListPost(resourceListRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **resourceListRequestDto** | [**ResourceListRequestDto**](ResourceListRequestDto.md)|  | [optional]

### Return type

[**ResourceResponseEntity**](ResourceResponseEntity.md)

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
val webService = apiClient.createWebservice(ResourceApi::class.java)
val resourceSaveRequestDto : ResourceSaveRequestDto =  // ResourceSaveRequestDto | 

launch(Dispatchers.IO) {
    val result : ResourceResponseEntity = webService.apiV1ResourceSavePost(resourceSaveRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **resourceSaveRequestDto** | [**ResourceSaveRequestDto**](ResourceSaveRequestDto.md)|  | [optional]

### Return type

[**ResourceResponseEntity**](ResourceResponseEntity.md)

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
val webService = apiClient.createWebservice(ResourceApi::class.java)
val requestDto : ResourceUploadRequestDto =  // ResourceUploadRequestDto | 
val fileList : kotlin.collections.List<StringStringValuesKeyValuePair> =  // kotlin.collections.List<StringStringValuesKeyValuePair> | 

launch(Dispatchers.IO) {
    val result : ResourceResponseEntity = webService.apiV1ResourceUploadPost(requestDto, fileList)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **requestDto** | [**ResourceUploadRequestDto**](.md)|  |
 **fileList** | [**kotlin.collections.List&lt;StringStringValuesKeyValuePair&gt;**](StringStringValuesKeyValuePair.md)|  | [optional]

### Return type

[**ResourceResponseEntity**](ResourceResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: text/plain, application/json, text/json

