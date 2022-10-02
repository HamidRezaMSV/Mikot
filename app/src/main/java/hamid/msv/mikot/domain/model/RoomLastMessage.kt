package hamid.msv.mikot.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_message_table")
data class RoomLastMessage(
    @PrimaryKey
    val id: String,
    val text: String,
    val time: String,
    @ColumnInfo(name = "sender_id")
    val senderId: String,
    @ColumnInfo(name = "receiver_id")
    val receiverId: String,
    @ColumnInfo(name = "sender_username")
    val senderUsername : String,
    @ColumnInfo(name = "receiver_username")
    val receiverUsername : String,
    val key: String
){

    fun mapToLastMessage() = LastMessage(id, text, time, senderId, receiverId, senderUsername, receiverUsername, key)

}
