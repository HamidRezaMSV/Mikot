package hamid.msv.mikot.domain.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MessageRepository {
    val createNewMessageResponse : LiveData<Task<Void>>
    val updateLastMessageResponse : LiveData<Task<Void>>

    val messages : StateFlow<List<Message>>

    suspend fun createNewMessage(message: Message , child : String)
    suspend fun listenForMessages(child : String)
    suspend fun updateChatLastMessage(lastMessage: LastMessage)
    suspend fun getChatsLastMessage() : Map<String, Any>
}