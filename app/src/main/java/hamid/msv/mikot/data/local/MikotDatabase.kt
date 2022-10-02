package hamid.msv.mikot.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import hamid.msv.mikot.data.local.dao.LastMessageDao
import hamid.msv.mikot.data.local.dao.MessageDao
import hamid.msv.mikot.data.local.dao.UserDao
import hamid.msv.mikot.domain.model.RoomLastMessage
import hamid.msv.mikot.domain.model.RoomMessage
import hamid.msv.mikot.domain.model.RoomUser
import hamid.msv.mikot.util.ROOM_DATABASE_VERSION

@Database(
    entities = [RoomMessage::class,RoomLastMessage::class,RoomUser::class],
    version = ROOM_DATABASE_VERSION,
    exportSchema = false)
abstract class MikotDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun lastMessageDao(): LastMessageDao
    abstract fun userDao(): UserDao
}