package hamid.msv.mikot.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hamid.msv.mikot.domain.model.RoomUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllUsers(users: List<RoomUser>)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): Flow<List<RoomUser>>

}