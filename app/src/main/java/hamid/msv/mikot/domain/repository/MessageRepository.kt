package hamid.msv.mikot.domain.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message
import kotlinx.coroutines.flow.StateFlow

interface MessageRepository {
    val updateLastMessageResponse : LiveData<Task<Void>>

    val messages : StateFlow<FirebaseResource<List<Message>>?>
    val sendNewMessageResponse: StateFlow<FirebaseResource<String>?>

    suspend fun sendNewMessage(message: Message, senderId: String, receiverId: String)
    fun listenForMessages(child : String)
    suspend fun updateChatLastMessage(lastMessage: LastMessage)
    suspend fun getChatsLastMessage() : Map<String, Any>
}