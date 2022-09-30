package hamid.msv.mikot.data.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import hamid.msv.mikot.data.source.remote.RemoteDataSource
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.repository.MessageRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MessageRepositoryImpl
@Inject constructor(private val remoteDataSource: RemoteDataSource) : MessageRepository {

    override val updateLastMessageResponse: LiveData<Task<Void>>
        get() = remoteDataSource.updateLastMessageResponse

    override val messages = remoteDataSource.messages
    override val sendNewMessageResponse = remoteDataSource.sendNewMessageResponse

    override suspend fun sendNewMessage(message: Message, senderId: String, receiverId: String) =
        remoteDataSource.sendNewMessage(message, senderId, receiverId)

    override fun listenForMessages(child : String) =
        remoteDataSource.listenForMessages(child)


    override suspend fun updateChatLastMessage(lastMessage: LastMessage) {
        remoteDataSource.updateChatLastMessage(lastMessage)
    }

    override suspend fun getChatsLastMessage(): Map<String, Any> = remoteDataSource.getChatsLastMessage()

}