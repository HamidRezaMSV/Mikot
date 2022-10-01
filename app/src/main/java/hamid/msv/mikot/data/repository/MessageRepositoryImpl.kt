package hamid.msv.mikot.data.repository

import hamid.msv.mikot.data.source.remote.RemoteDataSource
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class MessageRepositoryImpl
@Inject constructor(private val remoteDataSource: RemoteDataSource) : MessageRepository {

    override val messages = remoteDataSource.messages
    override val sendNewMessageResponse = remoteDataSource.sendNewMessageResponse

    override suspend fun sendNewMessage(message: Message, senderId: String, receiverId: String) =
        remoteDataSource.sendNewMessage(message, senderId, receiverId)

    override fun listenForMessages(child : String) =
        remoteDataSource.listenForMessages(child)

    override suspend fun getChatsLastMessage(): Map<String, Any> = remoteDataSource.getChatsLastMessage()

}