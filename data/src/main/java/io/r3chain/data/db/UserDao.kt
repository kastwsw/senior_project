package io.r3chain.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.r3chain.data.api.models.AuthDto
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM authdto LIMIT 1")
    fun getAll(): Flow<List<AuthDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: AuthDto)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(vararg users: AuthDto)

//    @Delete
//    suspend fun delete(user: AuthDto)

    @Query("DELETE FROM authdto")
    suspend fun deleteAll()

}