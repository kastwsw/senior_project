# OrganizationUserApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV1UserListPost**](OrganizationUserApi.md#apiV1UserListPost) | **POST** api/v1/user/list | 
[**apiV1UserSavePost**](OrganizationUserApi.md#apiV1UserSavePost) | **POST** api/v1/user/save | 





### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(OrganizationUserApi::class.java)
val body : kotlin.Any = Object // kotlin.Any | 

launch(Dispatchers.IO) {
    val result : OrganizationUserResponseEntity = webService.apiV1UserListPost(body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | **kotlin.Any**|  | [optional]

### Return type

[**OrganizationUserResponseEntity**](OrganizationUserResponseEntity.md)

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
val webService = apiClient.createWebservice(OrganizationUserApi::class.java)
val organizationUserSaveRequestDto : OrganizationUserSaveRequestDto =  // OrganizationUserSaveRequestDto | 

launch(Dispatchers.IO) {
    val result : OrganizationUserResponseEntity = webService.apiV1UserSavePost(organizationUserSaveRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **organizationUserSaveRequestDto** | [**OrganizationUserSaveRequestDto**](OrganizationUserSaveRequestDto.md)|  | [optional]

### Return type

[**OrganizationUserResponseEntity**](OrganizationUserResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

