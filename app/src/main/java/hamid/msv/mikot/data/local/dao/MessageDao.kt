package hamid.msv.mikot.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hamid.msv.mikot.domain.model.RoomMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMessages(messages: List<RoomMessage>)

    @Query("SELECT * FROM message_table WHERE `key` = :path")
    fun getAllMessages(path: String): Flow<List<RoomMessage>>

    @Query("DELETE FROM message_table")
    suspend fun deleteAllMessages()

}