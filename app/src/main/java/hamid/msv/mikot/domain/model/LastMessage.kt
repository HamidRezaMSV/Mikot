package hamid.msv.mikot.domain.model

data class LastMessage(
    val id: String,
    val text: String,
    val time: String,
    val senderId: String,
    val receiverId: String,
    val senderUsername : String,
    val receiverUsername : String
)
