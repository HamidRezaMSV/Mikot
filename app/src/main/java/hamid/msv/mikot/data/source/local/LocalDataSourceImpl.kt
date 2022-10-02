package hamid.msv.mikot.data.source.local

import hamid.msv.mikot.data.local.MikotDatabase
import hamid.msv.mikot.domain.model.RoomLastMessage
import hamid.msv.mikot.domain.model.RoomMessage

class LocalDataSourceImpl(mikotDatabase: MikotDatabase) : LocalDataSource {

    private val messageDao = mikotDatabase.messageDao()
    private val lastMessageDao = mikotDatabase.lastMessageDao()

    override suspend fun addAllMessages(messages: List<RoomMessage>) =
        messageDao.addAllMessages(messages)

    override fun getAllMessages(path: String) =
        messageDao.getAllMessages(path)

    override suspend fun addAllLastMessages(lastMessages: List<RoomLastMessage>) =
        lastMessageDao.addAllLastMessages(lastMessages)

    override fun getAllLastMessages(currentUserId: String) =
        lastMessageDao.getAllLastMessages(currentUserId)

}