package hamid.msv.mikot.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hamid.msv.mikot.domain.model.RoomLastMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface LastMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllLastMessages(lastMessages: List<RoomLastMessage>)

    @Query("SELECT * FROM last_message_table WHERE `key` LIKE :currentUserId || '%'")
    fun getAllLastMessages(currentUserId: String): Flow<List<RoomLastMessage>>

}