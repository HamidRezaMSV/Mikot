package hamid.msv.mikot.domain.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message

interface MessageRepository {
    val createNewMessageResponse : LiveData<Task<Void>>
    val updateLastMessageResponse : LiveData<Task<Void>>
    suspend fun createNewMessage(message: Message)
    suspend fun getAllMessages() : Map<String, Any>
    suspend fun updateChatLastMessage(lastMessage: LastMessage)
    suspend fun getChatsLastMessage() : Map<String, Any>
}