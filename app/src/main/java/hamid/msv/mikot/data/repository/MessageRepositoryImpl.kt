package hamid.msv.mikot.data.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import hamid.msv.mikot.data.source.remote.RemoteDataSource
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class MessageRepositoryImpl
@Inject constructor(private val remoteDataSource: RemoteDataSource) : MessageRepository {

    override val createNewMessageResponse: LiveData<Task<Void>>
        get() = remoteDataSource.createNewMessageResponse
    override val updateLastMessageResponse: LiveData<Task<Void>>
        get() = remoteDataSource.updateLastMessageResponse

    override suspend fun createNewMessage(message: Message , child : String) {
        remoteDataSource.createNewMessage(message , child)
    }

    override suspend fun getAllMessages(child : String): Map<String, Any> = remoteDataSource.getAllMessages(child)


    override suspend fun updateChatLastMessage(lastMessage: LastMessage) {
        remoteDataSource.updateChatLastMessage(lastMessage)
    }

    override suspend fun getChatsLastMessage(): Map<String, Any> = remoteDataSource.getChatsLastMessage()

}