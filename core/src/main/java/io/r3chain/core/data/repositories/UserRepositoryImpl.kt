package io.r3chain.core.data.repositories

import android.net.Uri
import dagger.Lazy
import io.r3chain.core.api.apis.AuthApi
import io.r3chain.core.api.infrastructure.ApiClient
import io.r3chain.core.api.models.AuthDto
import io.r3chain.core.api.models.AuthLoginRequestDto
import io.r3chain.core.api.models.AuthResponseEntity
import io.r3chain.core.api.models.AuthSaveRequestDto
import io.r3chain.core.data.db.CacheDatabase
import io.r3chain.core.data.services.ApiService
import io.r3chain.core.data.services.UserPrefsService
import io.r3chain.core.data.vo.ResourceEntity
import io.r3chain.core.data.vo.UserExtVO
import io.r3chain.core.data.vo.UserVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val apiClient: Lazy<ApiClient>,
    private val cacheDatabase: Lazy<CacheDatabase>,
    private val userPrefsService: Lazy<UserPrefsService>,
    private val resourcesGateway: Lazy<ResourcesGateway>
) : UserRepository {

    /**
     * Flow данных авторизованного пользователя.
     */
    override fun getUserFlow() = cacheDatabase.get().userDao().getAll().map { list ->
        list.firstOrNull()?.let { authDto ->
            // convert to VO
            UserVO().createByApi(authDto)
        }
    }

    /**
     * Flow дополнительных данных авторизованного пользователя.
     */
    override fun getUserExtFlow() = cacheDatabase.get().userExtDao().getAll().map { list ->
        list.firstOrNull()
    }


    /**
     * Авторизовать пользователя по соответствующим данным.
     *
     * @param email Адрес электронной почты.
     * @param password Пароль.
     */
    override suspend fun authUser(email: String, password: String) {
        apiService.safeApiCall {
            apiClient.get()
                .createService(io.r3chain.core.api.apis.AuthApi::class.java)
                .apiV1AuthLoginPost(
                    AuthLoginRequestDto(email = email, password = password)
                )
        }.onSuccess {
            // delete last user data
            withContext(Dispatchers.IO) {
                cacheDatabase.get().clearAllTables()
            }
            // handle result data
            handleAuthResult(it)
        }
    }


    /**
     * Data for "remember me" option.
     *
     * @return True - user auth restores after app restart.
     */
    override fun getRememberMeFlow() = userPrefsService.get().rememberMe

    override suspend fun saveRememberMe(value: Boolean) {
        userPrefsService.get().saveRememberMe(value)
    }

    /**
     * Data for "remember me" option.
     *
     * @return Null - no authorization.
     */
    override fun getAuthTokenFlow() = userPrefsService.get().authToken


    private suspend fun handleAuthResult(response: AuthResponseEntity) {
        // save user data
        withContext(Dispatchers.IO) {
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
                            ResourceEntity().createByApi(it.value)
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
                .createService(io.r3chain.core.api.apis.AuthApi::class.java)
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
    override suspend fun updateUserNotification(enabledEmail: Boolean) {
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
    override suspend fun exit(): Boolean {
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
    override suspend fun refresh() {
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
    override suspend fun uploadAvatarImage(uri: Uri) {
        resourcesGateway.get().sendFile(uri)
            .onSuccess {
                updateUserData(
                    newData = getCurrentUserDto().copy(
                        imageResourceID = it.id
                    )
                )
            }
    }

}