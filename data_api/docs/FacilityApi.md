# FacilityApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV1FacilityListPost**](FacilityApi.md#apiV1FacilityListPost) | **POST** api/v1/facility/list | 
[**apiV1FacilitySavePost**](FacilityApi.md#apiV1FacilitySavePost) | **POST** api/v1/facility/save | 





### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(FacilityApi::class.java)
val facilityListRequestDto : FacilityListRequestDto =  // FacilityListRequestDto | 

launch(Dispatchers.IO) {
    val result : FacilityResponseEntity = webService.apiV1FacilityListPost(facilityListRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **facilityListRequestDto** | [**FacilityListRequestDto**](FacilityListRequestDto.md)|  | [optional]

### Return type

[**FacilityResponseEntity**](FacilityResponseEntity.md)

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
val webService = apiClient.createWebservice(FacilityApi::class.java)
val facilitySaveRequestDto : FacilitySaveRequestDto =  // FacilitySaveRequestDto | 

launch(Dispatchers.IO) {
    val result : FacilityResponseEntity = webService.apiV1FacilitySavePost(facilitySaveRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **facilitySaveRequestDto** | [**FacilitySaveRequestDto**](FacilitySaveRequestDto.md)|  | [optional]

### Return type

[**FacilityResponseEntity**](FacilityResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

