# LocationApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV1LocationListPost**](LocationApi.md#apiV1LocationListPost) | **POST** api/v1/location/list | 
[**apiV1LocationSavePost**](LocationApi.md#apiV1LocationSavePost) | **POST** api/v1/location/save | 





### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(LocationApi::class.java)
val locationListRequestDto : LocationListRequestDto =  // LocationListRequestDto | 

launch(Dispatchers.IO) {
    val result : LocationResponseEntity = webService.apiV1LocationListPost(locationListRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **locationListRequestDto** | [**LocationListRequestDto**](LocationListRequestDto.md)|  | [optional]

### Return type

[**LocationResponseEntity**](LocationResponseEntity.md)

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
val webService = apiClient.createWebservice(LocationApi::class.java)
val locationSaveRequestDto : LocationSaveRequestDto =  // LocationSaveRequestDto | 

launch(Dispatchers.IO) {
    val result : LocationResponseEntity = webService.apiV1LocationSavePost(locationSaveRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **locationSaveRequestDto** | [**LocationSaveRequestDto**](LocationSaveRequestDto.md)|  | [optional]

### Return type

[**LocationResponseEntity**](LocationResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

