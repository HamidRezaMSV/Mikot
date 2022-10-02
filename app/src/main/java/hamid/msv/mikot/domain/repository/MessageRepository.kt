package hamid.msv.mikot.domain.repository

import hamid.msv.mikot.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MessageRepository {
    val messages : StateFlow<FirebaseResource<List<Message>>?>
    val sendNewMessageResponse: StateFlow<FirebaseResource<String>?>

    suspend fun sendNewMessage(message: Message, senderId: String, receiverId: String)
    fun listenForMessages(child : String)
    suspend fun getAllLastMessages(currentUserId : String): StateFlow<FirebaseResource<List<LastMessage>>?>

    suspend fun saveAllMessages(messages: List<RoomMessage>)
    suspend fun saveAllLastMessages(lastMessages: List<RoomLastMessage>)
    fun getAllMessagesFromDB(currentUserId: String): Flow<List<RoomMessage>>
    fun getAllLastMessagesFromDB(currentUserId: String): Flow<List<RoomLastMessage>>
}