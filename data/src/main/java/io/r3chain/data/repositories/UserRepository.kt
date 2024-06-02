package io.r3chain.data.repositories

import android.content.Context
import android.net.Uri
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import io.r3chain.data.api.apis.AuthApi
import io.r3chain.data.api.apis.ResourceApi
import io.r3chain.data.api.infrastructure.ApiClient
import io.r3chain.data.api.models.AuthDto
import io.r3chain.data.api.models.AuthLoginRequestDto
import io.r3chain.data.api.models.AuthResponseEntity
import io.r3chain.data.api.models.AuthSaveRequestDto
import io.r3chain.data.api.models.ResourceUploadDto
import io.r3chain.data.api.models.ResourceUploadRequestDto
import io.r3chain.data.db.CacheDatabase
import io.r3chain.data.services.ApiService
import io.r3chain.data.services.UserPrefsService
import io.r3chain.data.vo.ResourceVO
import io.r3chain.data.vo.UserExtVO
import io.r3chain.data.vo.UserVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import okio.source
import javax.inject.Inject

class UserRepository @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val apiService: ApiService,
    private val apiClient: Lazy<ApiClient>,
    private val cacheDatabase: Lazy<CacheDatabase>,
    private val userPrefsService: Lazy<UserPrefsService>
) {

    /**
     * Flow данных авторизованного пользователя.
     */
    fun getUserFlow() = cacheDatabase.get().userDao().getAll().map { list ->
        list.firstOrNull()?.let { authDto ->
            // convert to VO
            UserVO().createByApi(authDto)
        }
    }

    /**
     * Flow дополнительных данных авторизованного пользователя.
     */
    fun getUserExtFlow() = cacheDatabase.get().userExtDao().getAll().map { list ->
        list.firstOrNull()
    }


    /**
     * Авторизовать пользователя по соответствующим данным.
     *
     * @param email Адрес электронной почты.
     * @param password Пароль.
     */
    suspend fun authUser(email: String, password: String) {
        apiService.safeApiCall {
            apiClient.get()
                .createService(AuthApi::class.java)
                .apiV1AuthLoginPost(
                    AuthLoginRequestDto(email = email, password = password)
                )
        }.onSuccess {
            // delete last user data
            cacheDatabase.get().clearAllTables()
            // handle result data
            handleAuthResult(it)
        }
    }


    /**
     * Data for "remember me" option.
     *
     * @return True - user auth restores after app restart.
     */
    fun getRememberMeFlow() = userPrefsService.get().rememberMe

    suspend fun saveRememberMe(value: Boolean) {
        userPrefsService.get().saveRememberMe(value)
    }

    /**
     * Data for "remember me" option.
     *
     * @return Null - no authorization.
     */
    fun getAuthTokenFlow() = userPrefsService.get().authToken


    private suspend fun handleAuthResult(response: AuthResponseEntity) {
        // save user data
        response.authList?.values?.firstOrNull()?.also { dto ->
            // insert to db
            cacheDatabase.get().userDao().apply {
                insert(dto)
            }
            // link and other
            UserVO().createByApi(dto).also { user ->
                UserExtVO(
                    id = user.id,
                    avatarLink = response.responseResourceList?.map {
                        ResourceVO().createByApi(it.value)
                    }?.find {
                        it.id == user.imageResourceID
                    }?.posterLink ?: ""
                ).also { data ->
                    // insert to db
                    cacheDatabase.get().userExtDao().apply {
                        insert(data)
                    }
                }
            }
        }

        // save auth token
        userPrefsService.get().saveAuthToken(
            response.sessionList?.values?.firstOrNull()?.token ?: ""
        )
    }

    /**
     * Возвращает из БД текущие данные пользоватея, полученные от сервера.
     * Предполагается, что данные там есть. Иначе будет ошибка.
     *
     * @return Данные из БД.
     */
    private suspend fun getCurrentUserDto() = withContext(Dispatchers.IO) {
        cacheDatabase.get().userDao().getAll().first().first()
    }

    /**
     * Обновляет данные пользователя, оптравляя запрос на сервер.
     *
     * @param newData Новые данные, которые заменят старые.
     */
    private suspend fun updateUserData(newData: AuthDto) {
        apiService.safeApiCall {
            apiClient.get()
                .createService(AuthApi::class.java)
                .apiV1AuthSavePost(
                    authSaveRequestDto = AuthSaveRequestDto(newData)
                )
        }.onSuccess {
            handleAuthResult(it)
        }
    }


    /**
     * Обноляет флаг отправки нотификаций.
     *
     * @param enabledEmail Отправлять или нет.
     */
    suspend fun updateUserNotification(enabledEmail: Boolean) {
        updateUserData(
            newData = getCurrentUserDto().copy(
                sendEmailNotifications = enabledEmail
            )
        )
    }


    /**
     * Exit from the app.
     *
     * @return True - выход, подтверждён сервером.
     */
    suspend fun exit(): Boolean {
        val result = apiService.safeApiCall {
            apiClient.get()
                .createService(AuthApi::class.java)
                .apiV1AuthLogoutPost()
        }
        userPrefsService.get().saveAuthToken("")
        // NOTE: удалять данные, которые не учавствуют в UI экрана пользователя
//        cacheDatabase.get().userDao().deleteAll()
        return result.isSuccess
    }

    /**
     * Refresh user data.
     */
    suspend fun refresh() {
        apiService.safeApiCall {
            apiClient.get()
                .createService(AuthApi::class.java)
                .apiV1AuthVerifyPost()
        }.onSuccess {
            handleAuthResult(it)
        }
    }

    /**
     * Upload avatar picture.
     */
    suspend fun uploadAvatarImage(uri: Uri) {
        withContext(Dispatchers.IO) {
            // file data for request
            val fileBody = getFileMultipartBody(uri)
            // file meta for request
            val dto = getFileDtoMultipartBody(
                ResourceUploadRequestDto(listOf(ResourceUploadDto()))
            )
            // send to server
            apiService.safeApiCall {
                apiClient.get()
                    .createService(ResourceApi::class.java)
                    .apiV1MediaUploadPost(fileBody, dto)
            }.onSuccess { responseEntity ->
                responseEntity.resourceList!!.values.first().id.also { resourceId ->
                    updateUserData(
                        newData = getCurrentUserDto().copy(
                            imageResourceID = resourceId
                        )
                    )
                }
            }
        }
    }


    /**
     * Создаёт мальтипарт-дата файла для отправки на сервер.
     */
    private fun getFileMultipartBody(uri: Uri) = MultipartBody.Part.createFormData(
        name = "file",
        filename = uri.lastPathSegment ?: "upload_media",
        // чтобы не создавать лишний файл
        body = object : RequestBody() {
            override fun contentType() = "image/*".toMediaTypeOrNull()

            override fun writeTo(sink: BufferedSink) {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    sink.writeAll(inputStream.source())
                }
            }
        }
    )

    /**
     * Создаёт мальтипарт-дата описания файла для отправки на сервер.
     */
    private fun getFileDtoMultipartBody(data: ResourceUploadRequestDto) = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(ResourceUploadRequestDto::class.java)
        .toJson(data)
        .toRequestBody("application/json".toMediaTypeOrNull())

}