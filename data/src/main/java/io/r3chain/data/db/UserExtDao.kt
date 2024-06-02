package io.r3chain.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.r3chain.data.vo.UserExtVO
import kotlinx.coroutines.flow.Flow

@Dao
interface UserExtDao {

    @Query("SELECT * FROM userextvo LIMIT 1")
    fun getAll(): Flow<List<UserExtVO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserExtVO)

    @Query("DELETE FROM userextvo")
    suspend fun deleteAll()

}