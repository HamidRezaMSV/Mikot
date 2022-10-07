package hamid.msv.mikot.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class RoomMessage(
    @PrimaryKey
    val id: String,
    var text: String,
    val time: String,
    @ColumnInfo(name = "sender_id")
    val senderId: String,
    @ColumnInfo(name = "sender_username")
    val senderUsername: String,
    @ColumnInfo(name = "receiver_id")
    val receiverId: String,
    @ColumnInfo(name = "receiver_username")
    val receiverUsername: String,
    @ColumnInfo(name = "is_edited")
    var isEdited: Boolean = false,
    @ColumnInfo(name = "edit_time")
    var editTime: String? = null,
    @ColumnInfo(name = "is_reply")
    var isReply: Boolean = false,
    @ColumnInfo(name = "replied_msg_id")
    var repliedMessageId: String? = null,
    val key: String
) {

    fun mapToMessage() =
        Message(
            id = id,
            text = text,
            time = time,
            isReply = isReply,
            repliedMessageId = repliedMessageId,
            senderId = senderId,
            senderUsername = senderUsername,
            receiverId = receiverId,
            receiverUsername = receiverUsername,
            key = key
        )

}
