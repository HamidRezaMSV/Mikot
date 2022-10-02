package hamid.msv.mikot.domain.model

data class Message(
    var id: String? = null,
    var text: String? = null,
    var time: String? = null,
    val senderId: String? = null,
    val senderUsername : String? = null ,
    val receiverId: String? = null,
    val receiverUsername : String? = null ,
    var isEdited: Boolean = false,
    var editTime: String? = null,
    var isReply: Boolean = false,
    var repliedMessageId: String? = null,
    var key: String? = null
) {

    fun mapToLastMessage(): LastMessage =
        LastMessage(
            id = id,
            text = text,
            time = time,
            senderId = senderId,
            receiverId = receiverId,
            senderUsername = senderUsername,
            receiverUsername = receiverUsername,
            key = key
        )
}