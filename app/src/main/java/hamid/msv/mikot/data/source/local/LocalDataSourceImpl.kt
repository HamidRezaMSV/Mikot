package hamid.msv.mikot.data.source.local

import hamid.msv.mikot.data.local.MikotDatabase
import hamid.msv.mikot.domain.model.RoomLastMessage
import hamid.msv.mikot.domain.model.RoomMessage
import hamid.msv.mikot.domain.model.RoomUser

class LocalDataSourceImpl(mikotDatabase: MikotDatabase) : LocalDataSource {

    private val messageDao = mikotDatabase.messageDao()
    private val lastMessageDao = mikotDatabase.lastMessageDao()
    private val userDao = mikotDatabase.userDao()

    override suspend fun addAllMessages(messages: List<RoomMessage>) =
        messageDao.addAllMessages(messages)

    override fun getAllMessages(path: String) =
        messageDao.getAllMessages(path)

    override suspend fun addAllLastMessages(lastMessages: List<RoomLastMessage>) =
        lastMessageDao.addAllLastMessages(lastMessages)

    override fun getAllLastMessages(currentUserId: String) =
        lastMessageDao.getAllLastMessages(currentUserId)

    override suspend fun addAllUsers(users: List<RoomUser>) =
        userDao.addAllUsers(users)

    override fun getAllUsers() = userDao.getAllUsers()

    override fun getUserById(userId: String) = userDao.getUserById(userId)

    override suspend fun deleteAllUsers() = userDao.deleteAllUsers()

    override suspend fun deleteAllMessages() = messageDao.deleteAllMessages()

    override suspend fun deleteAllLastMessages() = lastMessageDao.deleteAllLastMessages()

    override suspend fun addUser(user: RoomUser) = userDao.addUser(user)
}