# AuthApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV1AuthLoginPost**](AuthApi.md#apiV1AuthLoginPost) | **POST** api/v1/auth/login | 
[**apiV1AuthLogoutPost**](AuthApi.md#apiV1AuthLogoutPost) | **POST** api/v1/auth/logout | 
[**apiV1AuthSavePost**](AuthApi.md#apiV1AuthSavePost) | **POST** api/v1/auth/save | 
[**apiV1AuthVerifyPost**](AuthApi.md#apiV1AuthVerifyPost) | **POST** api/v1/auth/verify | 





### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(AuthApi::class.java)
val authLoginRequestDto : AuthLoginRequestDto =  // AuthLoginRequestDto | 

launch(Dispatchers.IO) {
    val result : AuthResponseEntity = webService.apiV1AuthLoginPost(authLoginRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authLoginRequestDto** | [**AuthLoginRequestDto**](AuthLoginRequestDto.md)|  | [optional]

### Return type

[**AuthResponseEntity**](AuthResponseEntity.md)

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
val webService = apiClient.createWebservice(AuthApi::class.java)

launch(Dispatchers.IO) {
    val result : GeneralResponseEntity = webService.apiV1AuthLogoutPost()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**GeneralResponseEntity**](GeneralResponseEntity.md)

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
val webService = apiClient.createWebservice(AuthApi::class.java)
val authSaveRequestDto : AuthSaveRequestDto =  // AuthSaveRequestDto | 

launch(Dispatchers.IO) {
    val result : AuthResponseEntity = webService.apiV1AuthSavePost(authSaveRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authSaveRequestDto** | [**AuthSaveRequestDto**](AuthSaveRequestDto.md)|  | [optional]

### Return type

[**AuthResponseEntity**](AuthResponseEntity.md)

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
val webService = apiClient.createWebservice(AuthApi::class.java)

launch(Dispatchers.IO) {
    val result : GeneralResponseEntity = webService.apiV1AuthVerifyPost()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**GeneralResponseEntity**](GeneralResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

