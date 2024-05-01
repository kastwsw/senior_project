# PartnerApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV1PartnerListPost**](PartnerApi.md#apiV1PartnerListPost) | **POST** api/v1/partner/list | 
[**apiV1PartnerSavePost**](PartnerApi.md#apiV1PartnerSavePost) | **POST** api/v1/partner/save | 





### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(PartnerApi::class.java)
val partnerListRequestDto : PartnerListRequestDto =  // PartnerListRequestDto | 

launch(Dispatchers.IO) {
    val result : PartnerResponseEntity = webService.apiV1PartnerListPost(partnerListRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **partnerListRequestDto** | [**PartnerListRequestDto**](PartnerListRequestDto.md)|  | [optional]

### Return type

[**PartnerResponseEntity**](PartnerResponseEntity.md)

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
val webService = apiClient.createWebservice(PartnerApi::class.java)
val partnerSaveRequestDto : PartnerSaveRequestDto =  // PartnerSaveRequestDto | 

launch(Dispatchers.IO) {
    val result : PartnerResponseEntity = webService.apiV1PartnerSavePost(partnerSaveRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **partnerSaveRequestDto** | [**PartnerSaveRequestDto**](PartnerSaveRequestDto.md)|  | [optional]

### Return type

[**PartnerResponseEntity**](PartnerResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

