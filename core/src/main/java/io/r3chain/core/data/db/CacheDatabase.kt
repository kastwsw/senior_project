package io.r3chain.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.r3chain.core.api.models.AuthDto
import io.r3chain.core.data.vo.UserExtVO

@Database(
    entities = [AuthDto::class, UserExtVO::class],
    version = 2,
    exportSchema = false
)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun userDao(): io.r3chain.core.data.db.UserDao

    abstract fun userExtDao(): io.r3chain.core.data.db.UserExtDao
}