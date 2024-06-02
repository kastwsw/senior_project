package io.r3chain.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.r3chain.data.api.models.AuthDto
import io.r3chain.data.vo.UserExtVO

@Database(
    entities = [AuthDto::class, UserExtVO::class],
    version = 2,
    exportSchema = false
)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun userExtDao(): UserExtDao
}