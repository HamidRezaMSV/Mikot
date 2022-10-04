package hamid.msv.mikot.data.source.local

import hamid.msv.mikot.domain.model.RoomLastMessage
import hamid.msv.mikot.domain.model.RoomMessage
import hamid.msv.mikot.domain.model.RoomUser
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun addAllMessages(messages: List<RoomMessage>)

    fun getAllMessages(path: String): Flow<List<RoomMessage>>

    suspend fun addAllLastMessages(lastMessages: List<RoomLastMessage>)

    fun getAllLastMessages(currentUserId: String): Flow<List<RoomLastMessage>>

    suspend fun addAllUsers(users: List<RoomUser>)

    fun getAllUsers(): Flow<List<RoomUser>>

    fun getUserById(userId: String): Flow<RoomUser>

    suspend fun deleteAllUsers()

    suspend fun deleteAllMessages()

    suspend fun deleteAllLastMessages()

}