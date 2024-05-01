# OrganizationApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV1OrganizationListPost**](OrganizationApi.md#apiV1OrganizationListPost) | **POST** api/v1/organization/list | 
[**apiV1OrganizationSavePost**](OrganizationApi.md#apiV1OrganizationSavePost) | **POST** api/v1/organization/save | 





### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(OrganizationApi::class.java)

launch(Dispatchers.IO) {
    val result : OrganizationResponseEntity = webService.apiV1OrganizationListPost()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**OrganizationResponseEntity**](OrganizationResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json




### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(OrganizationApi::class.java)
val organizationSaveRequestDto : OrganizationSaveRequestDto =  // OrganizationSaveRequestDto | 

launch(Dispatchers.IO) {
    val result : OrganizationResponseEntity = webService.apiV1OrganizationSavePost(organizationSaveRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **organizationSaveRequestDto** | [**OrganizationSaveRequestDto**](OrganizationSaveRequestDto.md)|  | [optional]

### Return type

[**OrganizationResponseEntity**](OrganizationResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

