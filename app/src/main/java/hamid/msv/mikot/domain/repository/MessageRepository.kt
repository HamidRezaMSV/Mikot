package hamid.msv.mikot.domain.repository

import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.Message
import kotlinx.coroutines.flow.StateFlow

interface MessageRepository {
    val messages : StateFlow<FirebaseResource<List<Message>>?>
    val sendNewMessageResponse: StateFlow<FirebaseResource<String>?>

    suspend fun sendNewMessage(message: Message, senderId: String, receiverId: String)
    fun listenForMessages(child : String)
    suspend fun getChatsLastMessage() : Map<String, Any>
}