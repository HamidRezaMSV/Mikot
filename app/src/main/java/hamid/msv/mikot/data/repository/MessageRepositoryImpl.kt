package hamid.msv.mikot.data.repository

import hamid.msv.mikot.data.source.local.LocalDataSource
import hamid.msv.mikot.data.source.remote.RemoteDataSource
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.model.RoomLastMessage
import hamid.msv.mikot.domain.model.RoomMessage
import hamid.msv.mikot.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepositoryImpl
@Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MessageRepository {

    override val messages = remoteDataSource.messages
    override val sendNewMessageResponse = remoteDataSource.sendNewMessageResponse

    override suspend fun sendNewMessage(message: Message, senderId: String, receiverId: String) =
        remoteDataSource.sendNewMessage(message, senderId, receiverId)

    override fun listenForMessages(child : String) =
        remoteDataSource.listenForMessages(child)

    override suspend fun getAllLastMessages(currentUserId: String) =
        remoteDataSource.getAllLastMessages(currentUserId)

    override suspend fun saveAllMessages(messages: List<RoomMessage>) =
        localDataSource.addAllMessages(messages)

    override suspend fun saveAllLastMessages(lastMessages: List<RoomLastMessage>) =
        localDataSource.addAllLastMessages(lastMessages)

    override fun getAllMessagesFromDB(currentUserId: String): Flow<List<RoomMessage>> =
        localDataSource.getAllMessages(currentUserId)

    override fun getAllLastMessagesFromDB(currentUserId: String): Flow<List<RoomLastMessage>> =
        localDataSource.getAllLastMessages(currentUserId
        )
}