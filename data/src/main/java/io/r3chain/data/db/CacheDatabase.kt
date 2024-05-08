package io.r3chain.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.r3chain.data.api.models.AuthDto

@Database(entities = [AuthDto::class], version = 1, exportSchema = false)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}