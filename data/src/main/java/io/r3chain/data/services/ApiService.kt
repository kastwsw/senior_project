package io.r3chain.data.services

import io.r3chain.data.api.models.ApiStatusCode
import io.r3chain.data.api.models.AuthResponseEntity
import io.r3chain.data.api.models.ErrorEntity
import io.r3chain.data.api.models.GeneralResponseEntity
import io.r3chain.data.exceptions.ApiCallException
import io.r3chain.data.exceptions.AuthException
import io.r3chain.data.exceptions.NetworkIOException
import io.r3chain.data.exceptions.NoInternetException
import io.r3chain.data.exceptions.SurpriseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Оборачивает обращения к серверному API, для корректрой обработки в архитектуре данных.
 *
 * Содержит Flow как сетевых ошибок, так и ошибок возращаемых API.
 */
@Singleton
class ApiService @Inject constructor(
    private val networkService: NetworkService,
    private val userPrefsService: UserPrefsService
) {

    private val _exceptionsFlow = MutableSharedFlow<IOException>(
        // 0 - новые подписчики не получат предыдущие ошибки
        replay = 0,
        // хранить не более 10 ошибок
        extraBufferCapacity = 10,
        // удалять старые ошибоки в случае переполнения буфера
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Flow сетевых ошибок обращения к серверу.
     */
    val exceptionsFlow = _exceptionsFlow.asSharedFlow()

    /**
     * Запуск обращений к API сервера с обработкой ошибок и результата ответа.
     */
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ) = withContext(Dispatchers.IO) {
        // проверить доступ в интернет
        networkService.checkCurrentNetworkState()
        if (!networkService.internetAvailableFlow.value) {
            val e = NoInternetException()
            _exceptionsFlow.emit(e)
            Result.failure(e)
        } else {
            // сделать запрос к серверу
            try {
                getResultByResponse(apiCall())
            } catch (e: IOException) {
                // проверить доступ в интернет
                networkService.checkCurrentNetworkState()
                val error = if (networkService.internetAvailableFlow.value) {
                    // вероятно, серевер недоступен при активном доступе в Интернет
                    NetworkIOException(cause = e)
                } else {
                    // ошибка в работе api-клиента
                    e
                }
                // добавить ошибку в flow ошибок
                _exceptionsFlow.emit(error)
                Result.failure(error)
            } catch (e: Throwable) {
                // иная ошибка
                Result.failure(e)
            }
        }
    }.onFailure {
        if (it !is IOException) throw it
    }

    /**
     * Возвращает результат проверки объекта ответа API сервера.
     *
     * @param response Объект ответа API.
     * @return Объект результата.
     */
    private suspend fun <T> getResultByResponse(
        response: Response<T>
    ): Result<T> = if (!response.isSuccessful) {
        // проблемный код ответа сервера
        val error = ApiCallException(response.code(), response.errorBody()?.string())
        _exceptionsFlow.emit(error)
        Result.failure(error)
    } else {
        // успешный код ответа сервера
        val data = response.body()
        if (data == null) {
            // нет тела ответа
            val error = SurpriseException("Received null body from API.")
            _exceptionsFlow.emit(error)
            Result.failure(error)
        } else {
            // есть тело ответа
            getResultByEntity(data)
        }
    }

    /**
     * Возвращает результат проверки сущностей ответа API сервера.
     *
     * @param data Данные сущности ответа.
     * @return Объект результата.
     */
    private suspend fun <T> getResultByEntity(data: T): Result<T> = when (data) {
        // авторизаци и данные юзера
        is AuthResponseEntity -> getResultByCode(data, data.statusCode, data.message, data.errorList)
        is GeneralResponseEntity -> getResultByCode(data, data.statusCode, data.message, data.errorList)
        // other
        else -> Result.success(data)
    }

    /**
     * Возвращает результат проверки кода ответа API сервера.
     *
     * @param data Данные сущности ответа.
     * @param statusCode Код ответа.
     * @param message Сообщение ответа.
     * @param errorList Список ошибок в ответе.
     * @return Объект результата.
     */
    private suspend fun <T> getResultByCode(
        data: T,
        statusCode: ApiStatusCode?,
        message: String?,
        errorList: List<ErrorEntity>?
    ): Result<T> = when (statusCode) {
        ApiStatusCode.OK -> Result.success(data)
        else -> when (statusCode) {
            // юзер не найден
            ApiStatusCode.UNAUTHORIZED -> AuthException.Type.UNAUTHORIZED
            // кривые параметры
            ApiStatusCode.VALIDATION_ERROR -> AuthException.Type.VALIDATION_ERROR
            else -> null
        }.let { type ->
            val error = when (type) {
                is AuthException.Type -> {
                    // clear token
                    userPrefsService.saveAuthToken("")
                    // exception
                    AuthException(
                        type = type,
                        message = message,
                        errors = convertErrors(errorList)
                    )
                }
                else -> SurpriseException(message)
            }
            _exceptionsFlow.emit(error)
            Result.failure(error)
        }
    }

    private fun convertErrors(list: List<ErrorEntity>?) = list?.map {
        "${it.code}: ${it.message}"
    }

}