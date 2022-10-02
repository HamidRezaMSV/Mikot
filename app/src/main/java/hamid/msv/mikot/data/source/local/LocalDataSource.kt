package hamid.msv.mikot.data.source.local

import hamid.msv.mikot.domain.model.RoomLastMessage
import hamid.msv.mikot.domain.model.RoomMessage
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun addAllMessages(messages: List<RoomMessage>)

    fun getAllMessages(currentUserId: String): Flow<List<RoomMessage>>

    suspend fun addAllLastMessages(lastMessages: List<RoomLastMessage>)

    fun getAllLastMessages(currentUserId: String): Flow<List<RoomLastMessage>>

}