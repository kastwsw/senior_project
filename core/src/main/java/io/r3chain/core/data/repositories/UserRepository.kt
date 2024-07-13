package io.r3chain.core.data.repositories

import android.net.Uri
import io.r3chain.core.data.vo.UserExtVO
import io.r3chain.core.data.vo.UserVO
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    /**
     * Flow данных авторизованного пользователя.
     */
    fun getUserFlow(): Flow<UserVO?>

    /**
     * Flow дополнительных данных авторизованного пользователя.
     */
    fun getUserExtFlow(): Flow<UserExtVO?>

    /**
     * Авторизовать пользователя по соответствующим данным.
     *
     * @param email Адрес электронной почты.
     * @param password Пароль.
     */
    suspend fun authUser(email: String, password: String)

    /**
     * Data for "remember me" option.
     *
     * @return True - user auth restores after app restart.
     */
    fun getRememberMeFlow(): Flow<Boolean>

    suspend fun saveRememberMe(value: Boolean)

    /**
     * Data for "remember me" option.
     *
     * @return Null - no authorization.
     */
    fun getAuthTokenFlow(): Flow<String>

    /**
     * Обноляет флаг отправки нотификаций.
     *
     * @param enabledEmail Отправлять или нет.
     */
    suspend fun updateUserNotification(enabledEmail: Boolean)

    /**
     * Exit from the app.
     *
     * @return True - выход, подтверждён сервером.
     */
    suspend fun exit(): Boolean

    /**
     * Refresh user data.
     */
    suspend fun refresh()

    /**
     * Upload avatar picture.
     */
    suspend fun uploadAvatarImage(uri: Uri)
}
