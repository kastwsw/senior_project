# TraceApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV1TraceListPost**](TraceApi.md#apiV1TraceListPost) | **POST** api/v1/trace/list | 





### Example
```kotlin
// Import classes:
//import io.r3chain.data.api.*
//import io.r3chain.data.api.infrastructure.*
//import io.r3chain.data.api.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(TraceApi::class.java)
val traceListRequestDto : TraceListRequestDto =  // TraceListRequestDto | 

launch(Dispatchers.IO) {
    val result : TraceResponseEntity = webService.apiV1TraceListPost(traceListRequestDto)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **traceListRequestDto** | [**TraceListRequestDto**](TraceListRequestDto.md)|  | [optional]

### Return type

[**TraceResponseEntity**](TraceResponseEntity.md)

### Authorization


Configure Bearer:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

