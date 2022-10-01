package hamid.msv.mikot.domain.model

data class LastMessage(
    val id: String? = null,
    val text: String? = null,
    val time: String? = null,
    val senderId: String? = null,
    val receiverId: String? = null,
    val senderUsername : String? = null,
    val receiverUsername : String? = null
)
